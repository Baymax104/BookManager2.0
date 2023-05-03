package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.mainLaunchCallback
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
    val books = BiEvent<List<Book>, List<Book>>()
}

class EditRequester : Requester() {

    val repo = MainRepository

    fun deleteBooks(removedItems: List<EditAdapter.RemovedItem>, callback: Callback<Unit>) =
        mainLaunchCallback(callback) {
            // there are items removed
            if (removedItems.isNotEmpty()) {
                val ids = removedItems.map { it.value.id }
                repo.deleteBooks(ids)
            }
        }

    fun updateBookRank(books: List<Book>, callback: Callback<Unit>) =
        mainLaunchCallback(callback) {
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