package com.baymax104.bookmanager20.domain

import androidx.room.withTransaction
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.repository.local.Database
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
    val book = BiEvent<Book, Book>()

    val updateHistory = BiEvent<Book, History>()
}

class HistoryRequester : Requester() {

    val repo = MainRepository

    inline fun queryBookHistory(
        book: Book,
        crossinline callback: Callback<List<History>>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            repo.queryBookHistory(book.id)
        }
    }

    inline fun insertHistory(
        history: History,
        historyList: List<History>,
        crossinline callback: Callback<Pair<History, Int>>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            // the new value must be behind the value whose start is equal to it
            val histories = historyList.asSequence()
                .filter { !it.duplicate }
                .sortedBy { it.start }
                .toMutableList()

            // find previous
            val previous = histories.indexOfFirst { it.start > history.start }
                .takeIf { it != -1 }
                ?.let { Pair(it - 1, histories[it - 1]) }
                ?: Pair(histories.lastIndex, histories.last())
            // find next, it may have several ranges between previous and next
            val next = histories.indexOfFirst { it.end >= history.end }
                .takeIf { it != -1 }
                ?.let { Pair(it, histories[it]) }

            // check duplicate
            // the new range is within previous range
            if (history.end <= previous.second.end) {
                history.duplicate = true
            }
            // there are intervals between previous and next
            if (next != null && history.end > previous.second.end) {
                var hasSpace = false
                for (i in previous.first + 1..next.first) {
                    // there are spaces between two interval
                    if (histories[i - 1].end < histories[i].start - 1) {
                        hasSpace = true
                    }
                }
                if (!hasSpace) {
                    history.duplicate = true
                } else {  // merge the ranges between previous and next
                    for (i in previous.first + 1 until next.first) {
                        histories[i].duplicate = true
                    }
                }
            }
            // the new range includes some ranges, but no next range
            if (next == null && previous.first + 1 <= histories.size) {
                histories.listIterator(previous.first + 1)
                    .forEach { it.duplicate = true }
            }

            // the new range isn't duplicate, shrink its boundary
            if (!history.duplicate) {
                // shrink start boundary
                if (history.start <= previous.second.end) {
                    history.start = previous.second.end + 1
                }
                // shrink end boundary
                if (next != null && next.second.start <= history.end) {
                    history.end = next.second.start - 1
                }
            }

            val progress = Database.withTransaction {
                // update duplicate
                val duplicateHistories = histories.filter { it.duplicate }
                repo.updateHistoryDuplicate(duplicateHistories)

                // insert
                val id = repo.insertHistory(history)
                history.id = id.toInt()
                histories.add(history)

                // calculate read pages and update book progress
                val read = histories.asSequence()
                    .filter { !it.duplicate && it.type is History.Process }
                    .map { it.end - it.start + 1 }
                    .sum()
                val progress = (read * 1.0 / history.total * 100).toInt()
                repo.updateBookProgress(history.bookId, progress)

                progress
            }

            history to progress
        }
    }

    inline fun deleteHistory(
        history: History,
        historyList: List<History>,
        crossinline callback: Callback<Pair<History, Int>>
    ) = mainLaunch {
        val result = runCatching {
            val histories = historyList.asSequence()
                .filter { !it.duplicate }
                .sortedBy { it.start }
                .toMutableList()
        }
    }
}