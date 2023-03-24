package com.baymax104.bookmanager20.repository

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
class MainRepository @Inject constructor() {
    var processBooks: MutableList<Book> = mutableListOf()

    var finishBooks: MutableList<Book> = mutableListOf()

    private val bookService = WebService.create<BookService>()

    init {
        for (i in 1..10) {
            processBooks.add(Book.build {
                name = "来了"
                page = 20
                progress = 10
            })
        }
    }

    suspend fun requestBookInfo(isbn: String): Result<Book> = withContext(Dispatchers.IO) {
        bookService.requestBookInfo(isbn)
    }
}
