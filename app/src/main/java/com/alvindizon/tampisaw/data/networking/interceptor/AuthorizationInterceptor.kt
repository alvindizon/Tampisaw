package com.alvindizon.tampisaw.data.networking.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val accessKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(ACCEPT_VERSION_HEADER, ACCEPT_VERSION)
            .addHeader(AUTHORIZATION_HEADER, "$CLIENT_ID $accessKey")
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val ACCEPT_VERSION_HEADER = "Accept-Version"
        private const val ACCEPT_VERSION = "v1"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val CLIENT_ID = "Client-ID"
    }
}
