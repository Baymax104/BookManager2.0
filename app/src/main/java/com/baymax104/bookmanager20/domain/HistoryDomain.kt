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
import kotlin.collections.HashSet


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
        union += history
        history.duplicate = history !in union.uniqueSet
        val duplicateList = union.duplicateList
        val progress = union.progress
        val endTime = if (progress >= 100) Date() else null

        Database.withTransaction {
            try {
                repo.updateHistoryDuplicate(duplicateList)
                repo.insertHistory(history)
                repo.updateBookProgress(history.bookId, progress, endTime)
            } catch (e: Exception) {
                union -= history  // rollback
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

    class HistoryTree() : IntervalTree<Int, History>(object : IntervalCallback<Int> {
        override fun shrinkLeft(leftEnd: Int) = leftEnd + 1
        override fun shrinkRight(rightStart: Int) = rightStart - 1
        override fun adjoin(left: Int, right: Int) = left + 1 == right
    }) {
        constructor(histories: List<History>) : this() {
            histories.takeIf { it.isNotEmpty() }
                ?.filter { it.type !is History.Start }
                ?.let { create(it) }
        }
    }

    private var tree = HistoryTree(list)

    val state = State(list)

    var list: List<History>
        get() = state.value
        set(value) {
            state.value = value
            tree = HistoryTree(value)
        }

    val progress: Int
        get() = tree.firstSet.asSequence()
            .map { it.end - it.start + 1 }
            .sum()
            .let {
                val total = if (list.isNotEmpty()) list.first().total else 0
                if (total > 0) (it * 1.0 / total * 100).toInt() else 0
            }

    val duplicateList: List<History>
        get() = list.filter { it.duplicate }

    val uniqueSet: HashSet<History>
        get() = tree.firstSet

    operator fun plusAssign(history: History) {
        tree += history
        refreshDuplicate()
    }

    operator fun minusAssign(history: History) {
        tree -= history
        refreshDuplicate()
    }

    private fun refreshDuplicate() = list.forEach { it.duplicate = it !in uniqueSet }
}