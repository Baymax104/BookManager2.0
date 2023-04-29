package com.baymax104.bookmanager20.domain

import androidx.room.withTransaction
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.architecture.interfaces.deepClone
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.repository.local.Database
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.IntervalTree
import com.baymax104.bookmanager20.util.mainLaunch
import java.util.Date


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

    // 更新进度
    val updateHistory = BiEvent<Book, History>()
}

class HistoryRequester : Requester() {

    val repo = MainRepository

    val historyTree = IntervalTree<Int, History>(
        { it + 1 },
        { it - 1 },
        { left, right -> left + 1 == right }
    )

    inline fun queryBookHistory(
        book: Book,
        crossinline callback: Callback<List<History>>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            repo.queryBookHistory(book.id).also {
                // clear tree
                historyTree.create(it)
            }
        }
    }

    /**
     * preprocessing inserted history and current history list
     *
     * it does:
     * * check if there are duplicated histories in list after history inserts into list presumably
     * and set duplicated histories' duplicate value true
     * * check if the inserted history is duplicated and set its duplicate value
     * * if the inserted history is not duplicated, shrink its boundary
     *
     * it changes:
     * * history: start, end, duplicate
     * * historyList: item's duplicate
     */
    @PublishedApi
    internal fun preprocessing(history: History, historyList: List<History>) {
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
                histories.subList(previous.first + 1, next.first)
                    .forEach { it.duplicate = true }
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
    }

    inline fun insertHistory(
        history: History,
        historyList: List<History>,
        crossinline callback: Callback<Int>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            val historyCopy = history.clone()
            val histories = historyList.deepClone()
            preprocessing(historyCopy, histories)

            // get duplicated item's index
            val indexes = histories.asSequence()
                .mapIndexed { index, history -> if (history.duplicate) index else -1 }
                .filter { it != -1 }
                .toList()

            val duplicated = indexes.map { histories[it] }

            // calculate progress
            val progress = histories.apply { add(historyCopy) }
                .asSequence()
                .filter { it.type is History.Process }
                .map { it.end - it.start + 1 }
                .sum()
                .let { (it * 1.0 / historyCopy.total * 100).toInt() }

            val endTime = if (progress >= 100) Date() else null

            // database operation
            Database.withTransaction {
                repo.updateHistoryDuplicate(duplicated)
                repo.insertHistory(historyCopy).let { historyCopy.id = it.toInt() }
                repo.updateBookProgress(historyCopy.bookId, progress, endTime)
            }

            // update original history
            history.apply {
                id = historyCopy.id
                start = historyCopy.start
                end = historyCopy.end
                duplicate = historyCopy.duplicate
            }

            // update original list's duplicate status
            indexes.forEach { historyList[it].duplicate = true }

            progress
        }
    }

    inline fun deleteHistory(
        history: History,
        historyList: List<History>,
        crossinline callback: Callback<Int>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            if (history.duplicate) {
                // delete straightly
            }
            val histories = historyList.asSequence()
                .sortedBy { it.start }
                .toMutableList()

            val index = histories.indexOf(history)
            // find previous
            var previous: Pair<Int, History>? = null
            for (i in index - 1 downTo 0) {
                if (!histories[i].duplicate) {
                    previous = Pair(i, histories[i])
                }
            }
            previous!!  // previous must exist
            var next: Pair<Int, History>? = null
            for (i in index + 1 until histories.size) {
                if (!histories[i].duplicate) {
                    next = Pair(i, histories[i])
                }
            }

            0
        }
    }

    inline fun insert(
        history: History,
        crossinline callback: Callback<Int>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            0
        }
    }
}