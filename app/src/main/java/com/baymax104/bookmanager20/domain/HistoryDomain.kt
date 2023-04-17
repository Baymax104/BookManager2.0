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

    // 主页点击的Book
    val book = EventState<Book, Book>()

    val updateHistory = EventState<Book, History>()
}

class HistoryRequester : Requester() {

    val repo = MainRepository

    inline fun queryBookHistory(
        book: Book,
        crossinline callback: Callback<List<History>>
    ) = mainLaunch {
        ResultCallback.build(callback)(runCatching { repo.queryBookHistory(book.id) })
    }

    inline fun insertHistory(
        history: History,
        historyList: List<History>,
        crossinline callback: Callback<Pair<History, Int>>
    ) = mainLaunch {
        val result = runCatching {
            val mutableList = historyList.toMutableList()
            mutableList.add(history)

            // the new value must be behind the value whose start is equal to it
            val histories = mutableList.asSequence()
                .filter { !it.duplicate }
                .sortedBy { it.start }
                .toMutableList()
            val index = histories.indexOf(history)
            val previous = histories[index - 1]
            val next = histories.getOrNull(index + 1)  // the next may not exist

            // check duplicate
            // previous and next have no interval or the interval's length is 1
            if (next != null && previous.end >= next.start - 1) {
                // there is no space to insert new range, set it duplicate
                history.duplicate = true
            }
            // the new range is within previous range
            if (history.end <= previous.end) {
                history.duplicate = true
            }

            // the new range isn't duplicate, cope with it's boundary
            if (!history.duplicate) {
                // cope with start overlap
                if (history.start <= previous.end) {
                    history.start = previous.end + 1  // shrink the new value range
                }
                // cope with end overlap
                if (next != null && next.start <= history.end) {
                    history.end = next.start - 1  // shrink the new value range
                }
            }

            // insert
            val id = repo.insertHistory(history)
            history.id = id.toInt()

            // calculate read pages and update book progress
            val read = histories.asSequence()
                .filter { !it.duplicate && it.type is History.Process }
                .map { it.end - it.start + 1 }
                .sum()
            val progress = (read * 1.0 / history.total * 100).toInt()
            repo.updateBookProgress(history.bookId, progress)

            history to progress
        }
        ResultCallback.build(callback)(result)
    }
}