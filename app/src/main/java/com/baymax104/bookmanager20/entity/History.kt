package com.baymax104.bookmanager20.entity

import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 12:19
 *@Version 1
 */
class History(
    var bookId: Int,
    var start: Int,
    var updateTime: Date,
    var end: Int
) {
    var id = -1
    lateinit var book: Book
}

