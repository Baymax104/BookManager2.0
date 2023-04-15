package com.baymax104.bookmanager20.repository

import androidx.room.withTransaction
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.repository.local.Database
import com.baymax104.bookmanager20.repository.web.BookService
import com.baymax104.bookmanager20.repository.web.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

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

    private val historyDao = Database.historyDao()


    suspend fun requestBookInfo(isbn: String) = withContext(Dispatchers.IO) {
        bookService.requestBookInfo(isbn)
    }

    suspend fun queryAllProcessBook() = Database.withTransaction {
        bookDao.queryAllProcess()
    }

    suspend fun queryAllFinishBook() = Database.withTransaction {
        bookDao.queryAllFinish()
    }

    suspend fun queryBookHistory(bookId: Int) = Database.withTransaction {
        historyDao.queryBookHistories(bookId)
    }

    suspend fun insertProcessBook(book: Book) = Database.withTransaction {
        // set book
        book.startTime = Date()
        val all = bookDao.queryAllProcess()
        book.tableRank = all.size

        // insert book
        val id = bookDao.insertProcess(book)
        book.id = id.toInt()

        // insert initial history
        val history = History(book).apply { updateTime = Date() }
        // no need to obtain id, query again while entering History Page
        historyDao.insertHistory(history)

        book
    }

    suspend fun insertHistory(history: History): Unit = Database.withTransaction {
        historyDao.insertHistory(history)
    }

    suspend fun updateBookRank(books: List<Book>): Unit = Database.withTransaction {
        val i = bookDao.updateBookRank(books)
        if (i != books.size) throw Exception("更新顺序错误")
    }

    suspend fun deleteBooks(bookIds: List<Int>): Unit = Database.withTransaction {
        val i = bookDao.deleteBooks(bookIds)
        if (i != bookIds.size) throw Exception("删除图书错误")
        historyDao.deleteBooksHistories(bookIds)
    }
}
