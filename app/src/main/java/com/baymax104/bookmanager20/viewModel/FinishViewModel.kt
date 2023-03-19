package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.repository.MainRepository

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 21:47
 *@Version 1
 */
class FinishViewModel : ViewModel() {

    var finishBooks: MData<MutableList<Book>> = MutableLiveData(MainRepository.finishBooks)


}