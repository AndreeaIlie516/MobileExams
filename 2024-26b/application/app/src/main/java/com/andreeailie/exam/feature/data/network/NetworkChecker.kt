package com.andreeailie.exam.feature.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkChecker(
    private val context: Context
) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected

//        var isConnected: Boolean = false
//        val networkRequest = NetworkRequest.Builder()
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .build()
//
//        val networkCallback = object : ConnectivityManager.NetworkCallback() {
//            // network is available for use
//            override fun onAvailable(network: Network) {
//                isConnected = true
//                super.onAvailable(network)
//            }
//
//            // Network capabilities have changed for the network
//            override fun onCapabilitiesChanged(
//                network: Network,
//                networkCapabilities: NetworkCapabilities
//            ) {
//                super.onCapabilitiesChanged(network, networkCapabilities)
//                val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
//            }
//
//            // lost network connection
//            override fun onLost(network: Network) {
//                isConnected = false
//                super.onLost(network)
//            }
//        }
//
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.requestNetwork(networkRequest, networkCallback)
//        return isConnected
    }
 }