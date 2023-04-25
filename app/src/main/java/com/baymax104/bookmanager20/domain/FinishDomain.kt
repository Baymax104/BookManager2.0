package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.mainLaunch

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 21:47
 *@Version 1
 */
class FinishRequester : Requester() {

    val repo = MainRepository

    inline fun queryAllBook(crossinline callback: Callback<List<Book>>) =
        mainLaunch {
            ResultCallback.build(callback).runCoroutine { repo.queryAllFinishBook() }
        }
}