package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.repository.MainRepository
import com.baymax104.bookmanager20.util.LiveList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 21:47
 *@Version 1
 */
@HiltViewModel
class FinishViewModel @Inject constructor(
    private val repo: MainRepository
) : ViewModel() {

    val finishBooks = LiveList(repo.finishBooks)

}