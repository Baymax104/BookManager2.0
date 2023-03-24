package com.baymax104.bookmanager20.dataSource.web

import com.baymax104.bookmanager20.repository.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/21 17:59
 *@Version 1
 */
@Singleton
class WebService @Inject constructor(
    logger: HttpLoggingInterceptor,
    apiKeyFilter: Interceptor
) {

    val retrofit: Retrofit

    init {

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