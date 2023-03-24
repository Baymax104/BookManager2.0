package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.dataSource.web.ResultState
import com.baymax104.bookmanager20.dataSource.web.WebError
import com.baymax104.bookmanager20.dataSource.web.WebSuccess
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.MData
import com.baymax104.bookmanager20.util.MList
import com.blankj.utilcode.util.ToastUtils
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

    val processBooks: MList<Book> = MutableLiveData(repo.processBooks)

    // 手动添加，拍照图片路径
    val photoUri: MData<String?> = MData(null)

    // 扫码添加，请求返回的Book
    val requestBook: MData<Book?> = MData(null)

    fun addBook(book: Book) {
        ToastUtils.showShort(book.toString())
    }

    suspend fun requestBookInfo(isbn: String): ResultState<Book> {
        val (code, message, data) = repo.requestBookInfo(isbn)
        return when (code) {
            0 -> WebSuccess(data)
            else -> WebError(message)
        }
    }
}