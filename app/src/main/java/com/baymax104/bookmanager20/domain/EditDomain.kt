package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.EventState
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.mainLaunch
import com.baymax104.bookmanager20.view.adapter.EditAdapter

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/12 11:54
 *@Version 1
 */
class EditMessenger : Messenger() {
    // Edit页与主页共享的Book列表
    val books = EventState<List<Book>, List<Book>>()
}

class EditRequester : Requester() {

    val repo = MainRepository

    inline fun deleteBooks(
        removedItems: List<EditAdapter.RemovedItem>,
        crossinline callback: Callback<Unit>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            // there are items removed
            if (removedItems.isNotEmpty()) {
                val ids = removedItems.map { it.value.id }
                repo.deleteBooks(ids)
            }
        }
    }

    inline fun updateBookRank(
        books: List<Book>,
        crossinline callback: Callback<Unit>
    ) = mainLaunch {
        ResultCallback.build(callback).runCoroutine {
            // check if list has been swapped
            var hasSwapped = false
            for ((i, book) in books.withIndex()) {
                if (book.tableRank != i) {
                    hasSwapped = true
                }
                book.tableRank = i
            }
            if (hasSwapped) {
                repo.updateBookRank(books)
            }
        }
    }
}