package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
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

    private val books: LiveData<MutableList<Book>> = liveData {
        emit(repo.queryAllBook())
    }

    var observerBuilder = LiveList.Builder<Book>()

    val processBooks: MediatorLiveData<LiveList<Book>> = MediatorLiveData()

    // 手动添加，拍照图片路径
    val photoUri: MData<String> = MData()

    // 扫码添加，请求返回的Book
    val requestBook: MData<Book> = MData()

    init {
        processBooks.addSource(books) {
            processBooks.value = observerBuilder.bind(LiveList(it))
        }
    }

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

    suspend fun insertBook(book: Book): ResultState<Nothing?> {
        return try {
            val i = repo.insertBook(book)
            book.id = i.toInt()
            processBooks.value?.add(book)
            Success(null)
        } catch (e: Exception) {
            FAIL(e.message, e)
        }
    }


}