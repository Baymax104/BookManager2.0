package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.ResultState
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository

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
    val modifyBook = Sender<Book>()
}

class ProcessRequester : Requester() {

    val repo = MainRepository

    suspend fun requestBookInfo(isbn: String): ResultState<Book> {
        return try {
            val (code, message, data) = repo.requestBookInfo(isbn)
            when (code) {
                0 -> Success(data)
                else -> FAIL(message, null)
            }
        } catch (e: Exception) {
            FAIL(e.message, e)
        }
    }

    suspend fun insertProcessBook(book: Book): ResultState<Nothing?> {
        return try {
            val i = repo.insertProcessBook(book)
            book.id = i.toInt()
            Success(null)
        } catch (e: Exception) {
            FAIL(e.message, e)
        }
    }

    suspend fun queryAllBook(): ResultState<List<Book>> {
        return try {
            val books = repo.queryAllProcessBook()
            Success(books)
        } catch (e: Exception) {
            FAIL(e.message, e)
        }
    }

}