package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.ResultState
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.LiveList
import com.baymax104.bookmanager20.util.MData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
@HiltViewModel
class ProcessViewModel @Inject constructor(
    private val repo: MainRepository
) : ViewModel() {

    val processBooks = LiveList(repo.processBooks)

    // 手动添加，拍照图片路径
    val photoUri: MData<String> = MData()

    // 扫码添加，请求返回的Book
    val requestBook: MData<Book> = MData()


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
            processBooks.add(book)
            Success(null)
        } catch (e: Exception) {
            FAIL(e.message, e)
        }
    }

}