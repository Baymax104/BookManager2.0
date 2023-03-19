package com.baymax104.bookmanager20.entity

import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
class Book(var name: String, var page: Int) {
    var id = -1
    var author = "佚名"
    var startTime: Date? = null
    var progress = 0  // 已看页数
    var endTime: Date? = null
    var histories: List<History>? = null

    constructor(name: String, page: Int, author: String) : this(name, page) {
        this.author = author
    }

    constructor(name: String, page: Int, startTime: Date, progress: Int) : this(name, page) {
        this.startTime = startTime
        this.progress = progress
    }

    constructor(name: String, page: Int, startTime: Date, endTime: Date) : this(name, page) {
        this.startTime = startTime
        this.endTime = endTime
    }
}