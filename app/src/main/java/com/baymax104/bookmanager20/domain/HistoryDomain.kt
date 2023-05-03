package com.baymax104.bookmanager20.domain

import androidx.room.withTransaction
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.architecture.domain.State
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.repository.local.Database
import com.baymax104.bookmanager20.util.*
import java.util.*


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

    private val repo = MainRepository

    fun queryBookHistory(book: Book, callback: Callback<List<History>>) =
        mainLaunchCallback(callback) { repo.queryBookHistory(book.id) }

    fun insertHistory(
        history: History,
        union: HistoryUnion,
        callback: Callback<Int>
    ) = mainLaunchCallback(callback) {
        union.tree += history
        val duplicated = union.historyValue.asSequence()
            .onEach { it.duplicate = !union.tree.checkInFirst(it) }
            .filter { it.duplicate }
            .toList()
        history.duplicate = !union.tree.checkInFirst(history)
        val progress = union.progress
        val endTime = if (progress >= 100) Date() else null

        // database operation
        Database.withTransaction {
            try {
                repo.updateHistoryDuplicate(duplicated)
                repo.insertHistory(history)
                repo.updateBookProgress(history.bookId, progress, endTime)
            } catch (e: Exception) {
                // rollback
                union.tree -= history
                union.historyValue.forEach { it.duplicate = !union.tree.checkInFirst(it) }
                throw e
            }
        }

        progress
    }

    inline fun deleteHistory(
        history: History,
        historyList: List<History>,
        crossinline callback: Callback<Int>
    ) = mainLaunch {

    }
}

class HistoryUnion(list: List<History>) {

    private val callback = object : IntervalCallback<Int> {
        override fun shrinkLeft(leftEnd: Int) = leftEnd + 1
        override fun shrinkRight(rightStart: Int) = rightStart - 1
        override fun adjoin(left: Int, right: Int) = left + 1 == right
    }

    val historyState = State(list)

    var tree: IntervalTree<Int, History> = IntervalTree(callback)

    var historyValue: List<History>
        get() = historyState.value
        set(value) {
            historyState.value = value
            tree = IntervalTree(callback)
            value.takeIf { it.isNotEmpty() }
                ?.filter { it.type !is History.Start }
                ?.let { tree.create(it) }
        }

    val progress
        get() = tree.first.asSequence()
            .map { it.end - it.start + 1 }
            .sum()
            .let {
                val total = if (historyValue.isNotEmpty()) historyValue.first().total else 0
                if (total <= 0) {
                    0
                } else {
                    tree.first.asSequence()
                        .map { it.end - it.start + 1 }
                        .sum()
                        .let { (it * 1.0 / total * 100).toInt() }
                }
            }
}