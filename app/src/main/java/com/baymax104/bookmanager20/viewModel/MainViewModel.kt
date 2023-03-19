package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.view.FinishFragment
import com.baymax104.bookmanager20.view.ProcessFragment

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 11:45
 *@Version 1
 */
typealias MData<T> = MutableLiveData<T>

class MainViewModel : ViewModel() {

    val repo = MainRepository

    val page: MData<Int> = MData(0)

    val fragments = listOf(ProcessFragment(), FinishFragment())

    fun setPage(page: Int) {
        this.page.value = page
    }

}