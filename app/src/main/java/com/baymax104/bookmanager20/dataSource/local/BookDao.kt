package com.baymax104.bookmanager20.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 19:05
 *@Version 1
 */
@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book): Long

    @Query("select * from book")
    suspend fun queryAll(): MutableList<Book>
}