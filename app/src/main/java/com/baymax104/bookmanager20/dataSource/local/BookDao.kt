package com.baymax104.bookmanager20.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
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

    @Insert
    suspend fun insertProcess(book: Book): Long

    @Query("select * from Book where progress < page order by tableRank asc")
    suspend fun queryAllProcess(): MutableList<Book>

    @Query("select * from Book where progress >= page order by tableRank asc")
    suspend fun queryAllFinish(): MutableList<Book>
}