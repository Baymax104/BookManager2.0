package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.mainLaunch

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
class ProcessMessenger : Messenger() {
    // 手动添加，拍照图片路径
    val photoUri = Replier<String>()

    // 扫码添加，请求返回的Book
    val requestBook = EventState<String, Book>()

    // 修改对话框修改后返回Book
    val modifyBook = EventState<Book, Book>()

    // 对话框请求插入Book
    val insertBook = Sender<Book>()
}

class ProcessRequester : Requester() {

    val repo = MainRepository

    inline fun requestBookInfo(isbn: String, crossinline callback: Callback<Book>) =
        mainLaunch {
            ResultCallback.build(callback).runCoroutine {
                val (code, message, data) = repo.requestBookInfo(isbn)
                when (code) {
                    0 -> data
                    else -> throw Exception(message)
                }
            }
        }

    inline fun insertProcessBook(book: Book, crossinline callback: Callback<Book>) =
        mainLaunch {
            ResultCallback.build(callback).runCoroutine { repo.insertProcessBook(book) }
        }

    inline fun queryAllBook(crossinline callback: Callback<List<Book>>) =
        mainLaunch {
            ResultCallback.build(callback).runCoroutine { repo.queryAllProcessBook() }
        }

}