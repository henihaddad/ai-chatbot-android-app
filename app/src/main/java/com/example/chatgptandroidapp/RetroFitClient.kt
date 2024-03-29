
package com.example.chatgptandroidapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import com.example.chatgptandroidapp.BuildConfig


object RetrofitClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val apiKey = BuildConfig.OPENAI_API_KEY

            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer $apiKey")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
