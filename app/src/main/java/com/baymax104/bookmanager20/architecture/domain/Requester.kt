package com.baymax104.bookmanager20.architecture.domain

import androidx.lifecycle.ViewModel

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/13 20:28
 *@Version 1
 */
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
