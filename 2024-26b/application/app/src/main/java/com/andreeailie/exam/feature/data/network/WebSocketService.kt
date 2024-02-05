package com.andreeailie.exam.feature.data.network

import android.util.Log
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.EntityServer
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.net.SocketException

data class WebSocketUpdate(
    val action: String,
    val entity: EntityServer
)

class WebSocketService(
    private val networkChecker: NetworkChecker,
    url: String
) {

    private val client = OkHttpClient()
    private val request = Request.Builder().url(url).build()
    private var webSocket: WebSocket? = null

    private val eventChannel = Channel<WebSocketUpdate>(Channel.BUFFERED)

    private var reconnectJob: Job? = null
    private var isConnected = false

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        if (networkChecker.isNetworkAvailable()) {
            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                    Log.i("WebSocketService", "WebSocket Connection Opened")
                    isConnected = true
                    reconnectJob?.cancel()
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.i("WebSocketService", "Received Message: $text")
                    val notification = parseUpdateNotification(text)
                    Log.i("WebSocketService", "UpdateNotification: $notification")
                    if (notification != null) {
                        val update =
                            WebSocketUpdate(action = "CreatedEntity", notification)
                        Log.i("WebSocketService", "Update: $update")
                        if (update.entity != null) {
                            eventChannel.trySend(update).isSuccess
                        }
                    }
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    // TODO
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.i("WebSocketService", "WebSocket Closing: $code / $reason")
                    isConnected = false
                    eventChannel.close()
                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: okhttp3.Response?
                ) {
                    Log.e("WebSocketService", "WebSocket Failure: ${t.message}")
                    isConnected = false

                    // Close the current WebSocket connection
                    webSocket.close(1000, null)

                    // Handle reconnection only if it's a network issue
                    if (t is SocketException && networkChecker.isNetworkAvailable()) {
                        Log.i("WebSocketService", "Attempting to reconnect...")
                        scheduleReconnect()
                    } else {
                        // For other exceptions, log and handle appropriately
                        Log.e("WebSocketService", "Non-recoverable error occurred: ${t.message}")
                    }

                    eventChannel.close(t)
                }
            })
        } else {
            Log.i("WebSocketService", "Network unavailable, not connecting to WebSocket")
        }
    }

    private fun reconnect() {
        reconnectJob?.cancel()
        reconnectJob = GlobalScope.launch {
            while (!isConnected) {
                delay(5000)
                if (!isConnected) {
                    Log.i("WebSocketService", "Reconnecting...")
                    connectWebSocket()
                }
            }
        }
    }

    private fun scheduleReconnect() {
        var retryDelay = 1000L // 1 second
        reconnectJob?.cancel()
        reconnectJob = GlobalScope.launch {
            while (!isConnected && networkChecker.isNetworkAvailable()) {
                delay(retryDelay)
                retryDelay = (retryDelay * 2).coerceAtMost(60000) // Max 60 seconds
                if (!isConnected) {
                    Log.i("WebSocketEventDataSource", "Reconnecting...")
                    connectWebSocket()
                }
            }
        }
    }

    fun observeWebSocketUpdates(): Flow<WebSocketUpdate> {
        return eventChannel.receiveAsFlow()
    }

    fun sendWebSocketUpdate(entity: Entity) {
        val entityServer = EntityServer(
            name = entity.name,
            team = entity.team,
            details = entity.details,
            status = entity.status,
            participants = entity.participants,
            type = entity.type
        )
        val update = WebSocketUpdate("CreatedEntity", entityServer)
        val json = Gson().toJson(update)
        webSocket?.send(json)
    }

    private fun parseUpdateNotification(jsonString: String): EntityServer {
        return Gson().fromJson(jsonString, EntityServer::class.java)
    }

    fun close() {
        webSocket?.close(1000, null)
        eventChannel.close()
    }
}