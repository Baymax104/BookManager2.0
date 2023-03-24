package com.baymax104.bookmanager20.dataSource

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.baymax104.bookmanager20.dataSource.local.LocalDatabase
import com.baymax104.bookmanager20.repository.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 19:22
 *@Version 1
 */
@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "BookManagerDB"
        ).build()


    @Provides
    @Singleton
    fun logger() = HttpLoggingInterceptor {
        Log.i("BookManager-log-web", it)
    }.setLevel(HttpLoggingInterceptor.Level.BASIC)


    @Provides
    @Singleton
    fun apiKeyFilter() = Interceptor {
        val url = it.request().url.newBuilder()
            .addQueryParameter("apikey", API_KEY)
            .build()
        val request = it.request()
            .newBuilder()
            .url(url)
            .build()
        it.proceed(request)
    }

}