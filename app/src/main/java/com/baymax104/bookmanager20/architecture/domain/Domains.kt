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

fun <E> State<out List<E>>.add(element: E) {
    val list = value
    list as MutableList<E>
    list.add(element)
    value = list
}

fun <E> State<out List<E>>.remove(element: E) {
    val list = value
    list as MutableList<E>
    list.remove(element)
    value = list
}

open class Requester : ViewModel() {
    class ResultCallback<T> {
        private var onSuccess: (T) -> Unit = {}
        private var onFail: (Exception) -> Unit = {}
        fun success(callback: (T) -> Unit) = apply { onSuccess = callback }
        fun fail(callback: (Exception) -> Unit) = apply { onFail = callback }
        operator fun invoke(resultState: ResultState<T>) {
            when (resultState) {
                is Success -> onSuccess(resultState.data)
                is Fail -> onFail(resultState.error)
            }
        }
    }

    sealed class ResultState<out T>
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Fail(val error: Exception) : ResultState<Nothing>()

}

open class Messenger : ViewModel()