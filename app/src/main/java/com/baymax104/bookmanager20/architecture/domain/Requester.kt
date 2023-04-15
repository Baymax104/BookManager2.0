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
        private var onFail: (Throwable) -> Unit = {}

        companion object {
            inline fun <T> build(block: ResultCallback<T>.() -> Unit) =
                ResultCallback<T>().apply(block)
        }

        fun success(callback: (T) -> Unit) = apply { onSuccess = callback }
        fun fail(callback: (Throwable) -> Unit) = apply { onFail = callback }
        operator fun invoke(result: Result<T>) {
            when {
                result.isSuccess -> result.onSuccess(onSuccess)
                result.isFailure -> result.onFailure(onFail)
            }
        }
    }

}
