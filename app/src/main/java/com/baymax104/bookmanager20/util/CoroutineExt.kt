package com.baymax104.bookmanager20.util

import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 23:05
 *@Version 1
 */

val MainScope = MainScope()
val MainScopeContext = MainScope.coroutineContext

fun mainLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = MainScope.launch(context, start, block)

val BaseExceptionHandler = CoroutineExceptionHandler { context, throwable ->
    ToastUtils.showShort("$context: ${throwable.message}")
}



