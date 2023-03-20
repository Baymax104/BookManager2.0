package com.baymax104.bookmanager20.repository

import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 12:14
 *@Version 1
 */
object MainRepository {
    var processBooks: MutableList<Book> = mutableListOf()

    var finishBooks: MutableList<Book> = mutableListOf()

    init {
        for (i in 1..10) {
            processBooks.add(Book.build {
                name = "来了"
                page = 20
                progress = 10
            })
        }
    }

}