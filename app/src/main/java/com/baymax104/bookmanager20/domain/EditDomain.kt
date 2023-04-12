package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.EventState
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.Null
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
        crossinline callback: Callback<Null>
    ) = mainLaunch {
        try {
            val ids = removedItems.map { it.value.id }
            val items = repo.deleteBooks(ids)
            if (items == ids.size) {
                Success(null)
            } else {
                Fail(Exception("删除个数错误"))
            }
        } catch (e: Exception) {
            Fail(e)
        }.let {
            ResultCallback<Null>().apply(callback)(it)
        }
    }
}