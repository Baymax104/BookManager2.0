package com.baymax104.bookmanager20.repository

import com.baymax104.bookmanager20.dataSource.local.LocalDatabase
import com.baymax104.bookmanager20.dataSource.web.BookService
import com.baymax104.bookmanager20.dataSource.web.WebService
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 12:14
 *@Version 1
 */
@Singleton
class MainRepository @Inject constructor(
    webService: WebService,
    localDatabase: LocalDatabase
) {

    var processBooks: MutableList<Book> = mutableListOf()

    var finishBooks: MutableList<Book> = mutableListOf()

    private val bookService = webService.create<BookService>()

    private val bookDao = localDatabase.bookDao()

    init {
        for (i in 1..10) {
            processBooks.add(Book.build {
                name = "来了"
                page = 20
                progress = 10
            })
        }
    }

    suspend fun insertBook(book: Book) = withContext(Dispatchers.IO) {
        bookDao.insert(book)
    }

    suspend fun requestBookInfo(isbn: String): Result<Book> = withContext(Dispatchers.IO) {
        bookService.requestBookInfo(isbn)
    }
}
