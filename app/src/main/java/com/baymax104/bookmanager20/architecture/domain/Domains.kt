package com.baymax104.bookmanager20.architecture.domain

import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.util.MData

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/9 11:17
 *@Version 1
 */
open class StateHolder : ViewModel()

class State<E>(value: E) : MData<E>(value) {
    override fun getValue(): E =
        super.getValue() ?: throw NullPointerException("state value is null")
}

open class Requester : ViewModel()

open class Messenger : ViewModel()