package com.baymax104.bookmanager20.repository

import android.app.Application
import androidx.room.Room
import com.baymax104.bookmanager20.repository.local.Database
import com.baymax104.bookmanager20.repository.local.LocalDatabase

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/23 21:50
 *@Version 1
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Database = Room.databaseBuilder(
            this,
            LocalDatabase::class.java,
            LocalDatabase.DatabaseName
        ).fallbackToDestructiveMigration().build()
    }
}