package com.baymax104.bookmanager20.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.baymax104.bookmanager20.entity.History

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/14 23:38
 *@Version 1
 */
@Dao
interface HistoryDao {

    @Insert
    suspend fun insertHistory(history: History): Long

    @Query("select * from History where bookId = :bookId")
    suspend fun queryBookHistories(bookId: Int): MutableList<History>

    @Query("delete from History where bookId in (:bookIds)")
    suspend fun deleteBooksHistories(bookIds: List<Int>): Int

    @Update
    suspend fun updateHistoryDuplicate(histories: List<History>): Int
}