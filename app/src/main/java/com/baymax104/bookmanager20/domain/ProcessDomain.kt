package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Requester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.Callback
import com.baymax104.bookmanager20.util.mainLaunchCallback

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
class ProcessMessenger : Messenger() {
    // 手动添加，拍照图片路径
    val photoUri = UniEvent<String>()

    // 扫码添加，请求返回的Book
    val requestBook = BiEvent<String, Book>()

    // 修改对话框修改后返回Book
    val modifyBook = BiEvent<Book, Book>()

    // 对话框请求插入Book
    val insertBook = UniEvent<Book>()
}

class ProcessRequester : Requester() {

    private val repo = MainRepository

    fun requestBookInfo(isbn: String, callback: Callback<Book>) =
        mainLaunchCallback(callback) {
            val (code, message, data) = repo.requestBookInfo(isbn)
            when (code) {
                0 -> data
                else -> throw Exception(message)
            }
        }

    fun insertProcessBook(book: Book, callback: Callback<Book>) =
        mainLaunchCallback(callback) { repo.insertProcessBook(book) }

    fun queryAllBook(callback: Callback<List<Book>>) =
        mainLaunchCallback(callback) { repo.queryAllProcessBook() }

}