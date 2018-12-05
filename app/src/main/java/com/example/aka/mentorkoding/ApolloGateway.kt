package com.example.aka.mentorkoding

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import okhttp3.OkHttpClient
import java.io.File

class ApolloGateway(var context: Context) {

    fun createClient(): ApolloClient {
        return ApolloClient
            .builder()
            .serverUrl("https://mentor-microservice-gateway.herokuapp.com/graphql")
            .httpCache(createHttpCache())
            .okHttpClient(createOkHttp())
            .build()
    }

    private fun createOkHttp() : OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        val token = context.getSharedPreferences("auth", Context.MODE_PRIVATE).getString("token", "")
        if (token.isNotEmpty()) {
            okHttpClient.addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().header("authorization", "Bearer $token").build())
            }
        }
        return okHttpClient.build()
    }

    private fun createHttpCache(): ApolloHttpCache {
        val file = File("/cache/")
        val size = 1024 * 1024
        val cacheStore = DiskLruHttpCacheStore(file, size.toLong())
        return ApolloHttpCache(cacheStore)
    }
}