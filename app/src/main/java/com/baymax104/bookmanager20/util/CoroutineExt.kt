package com.baymax104.bookmanager20.util

import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 23:05
 *@Version 1
 */

lateinit var MainScope: CoroutineScope

lateinit var MainScopeContext: CoroutineContext

fun mainLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = MainScope.launch(context, start, block)

val BaseExceptionHandler = CoroutineExceptionHandler { context, throwable ->
    ToastUtils.showShort("$context: ${throwable.message}")
}



