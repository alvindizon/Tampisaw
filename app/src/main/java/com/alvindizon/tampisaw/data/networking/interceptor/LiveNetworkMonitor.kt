package com.alvindizon.tampisaw.data.networking.interceptor

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkMonitor {
    fun isConnected(): Boolean
}

@Singleton
class LiveNetworkMonitor @Inject constructor(private val connectivityManager: ConnectivityManager) :
    NetworkMonitor {

    override fun isConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
            else -> false
        }
    }
}
