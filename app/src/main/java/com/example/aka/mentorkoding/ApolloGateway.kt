package com.example.aka.mentorkoding

import android.content.Context
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class ApolloGateway(var context: Context) {

    fun createClient(): ApolloClient {
        return ApolloClient
            .builder()
            .serverUrl("https://mentor-microservice-gateway.herokuapp.com/graphql")
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
}