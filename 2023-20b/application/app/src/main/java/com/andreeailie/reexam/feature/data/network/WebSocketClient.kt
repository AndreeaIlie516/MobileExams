package com.andreeailie.reexam.feature.data.network

import android.os.Handler
import android.os.Looper
import com.andreeailie.reexam.feature.domain.model.Entity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class WebSocketClient(private val okHttpClient: OkHttpClient) : WebSocketListener() {

    private var webSocket: WebSocket? = null
    private val connectionRetries = 3
    private var retryCount = 0

    private val _events = Channel<Entity>()
    val events = _events.receiveAsFlow()

    private val mainHandler = Handler(Looper.getMainLooper())

    fun connect(url: String) {
        retryCount = 0
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        this.webSocket = webSocket
        retryCount = 0
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val data = JSONObject(text)
            val id = data.getInt("id")
            val name = data.getString("name")
            val type = data.getString("type")
            val calories = data.getInt("calories")
            val date = data.getString("date")
            val notes = data.getString("notes")

            mainHandler.post {
                _events.trySend(
                    Entity(
                        idLocal = 0,
                        id = id,
                        name = name,
                        type = type,
                        calories = calories,
                        date = date,
                        notes = notes,
                        action = null)
                )
            }
        } catch (e: Exception) {
            // Handle JSON parsing errors
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        webSocketClosed()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        webSocketClosed()
    }

    private fun webSocketClosed() {
        this.webSocket = null
        if (retryCount < connectionRetries) {
            retryCount++
            reconnect()
        }
    }

    private fun reconnect() {
        val retryDelayMillis = 2000L
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            connect("ws://10.0.2.2:2320/ws")
        }, retryDelayMillis)
    }

}