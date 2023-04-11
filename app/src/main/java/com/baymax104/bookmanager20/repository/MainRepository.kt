package com.baymax104.bookmanager20.repository

import androidx.room.Transaction
import com.baymax104.bookmanager20.dataSource.local.Database
import com.baymax104.bookmanager20.dataSource.web.BookService
import com.baymax104.bookmanager20.dataSource.web.WebService
import com.baymax104.bookmanager20.entity.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 12:14
 *@Version 1
 */
object MainRepository {

    private val bookService = WebService.create<BookService>()

    private val bookDao = Database.bookDao()


    @Transaction
    suspend fun insertProcessBook(book: Book) = withContext(Dispatchers.IO) {
        val all = bookDao.queryAllProcess()
        book.tableRank = all.size
        bookDao.insertProcess(book)
    }

    suspend fun requestBookInfo(isbn: String) = withContext(Dispatchers.IO) {
        bookService.requestBookInfo(isbn)
    }

    suspend fun queryAllProcessBook() = withContext(Dispatchers.IO) {
        bookDao.queryAllProcess()
    }

    suspend fun queryAllFinishBook() = withContext(Dispatchers.IO) {
        bookDao.queryAllFinish()
    }
}
