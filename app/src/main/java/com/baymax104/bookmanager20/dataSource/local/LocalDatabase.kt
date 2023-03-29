package com.baymax104.bookmanager20.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.RoomConverter

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 19:16
 *@Version 1
 */
@TypeConverters(RoomConverter::class)
@Database(entities = [Book::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    companion object {
        const val DatabaseName = "BookManager"
    }

    abstract fun bookDao(): BookDao
}