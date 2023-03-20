package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
class ProcessViewModel : ViewModel() {

    val processBooks: MData<MutableList<Book>> = MutableLiveData(MainRepository.processBooks)

}