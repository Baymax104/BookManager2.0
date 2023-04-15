package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.EventState
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.mainLaunch


/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/14 19:24
 *@Version 1
 */
class HistoryMessenger : Messenger() {
    val book = EventState<Book, Book>()
}

class HistoryRequester : Requester() {

    val repo = MainRepository

    inline fun queryBookHistory(
        book: Book,
        crossinline callback: Callback<List<History>>
    ) = mainLaunch {
        ResultCallback.build(callback)(runCatching { repo.queryBookHistory(book.id) })
    }
}