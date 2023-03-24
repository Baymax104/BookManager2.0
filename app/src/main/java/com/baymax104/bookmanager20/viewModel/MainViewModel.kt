package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.util.MData
import com.baymax104.bookmanager20.view.finish.FinishFragment
import com.baymax104.bookmanager20.view.process.ProcessFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 11:45
 *@Version 1
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val page: MData<Int> = MData(0)

    val fragments = listOf(ProcessFragment(), FinishFragment())

}