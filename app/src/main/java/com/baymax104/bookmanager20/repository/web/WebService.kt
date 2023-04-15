package com.baymax104.bookmanager20.repository.web

import android.util.Log
import com.baymax104.bookmanager20.repository.API_KEY
import com.baymax104.bookmanager20.repository.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/21 17:59
 *@Version 1
 */
object WebService {

    val retrofit: Retrofit

    init {

        val logger = HttpLoggingInterceptor {
            Log.i("BookManager-log-web", it)
        }.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val apiKeyFilter = Interceptor {
            val url = it.request().url.newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build()
            val request = it.request()
                .newBuilder()
                .url(url)
                .build()
            it.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addInterceptor(apiKeyFilter)
            .retryOnConnectionFailure(true)
            .build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
    }

    inline fun <reified T> create(): T = retrofit.create()
}