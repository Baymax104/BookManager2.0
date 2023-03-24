package com.baymax104.bookmanager20.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.core.LiveEventBusCore
import com.jeremyliao.liveeventbus.core.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 21:13
 *@Version 1
 */

object Bus {
    val core: LiveEventBusCore = LiveEventBusCore.get()

    inline fun <reified E> with(key: String): Observable<E> = core.with(key, E::class.java)!!

    fun postTag(key: String) = with<Boolean>(key).post(true)

    fun observeTag(
        key: String,
        owner: LifecycleOwner,
        block: (Boolean) -> Unit
    ) = with<Boolean>(key).observe(owner, block)
}

infix fun KClass<out Any>.todo(action: String): String = "${simpleName ?: "NoPoster"}-$action"

fun <T> Observable<T>.observeCoroutine(
    owner: LifecycleOwner,
    context: CoroutineContext,
    action: suspend CoroutineScope.(T) -> Unit
) = observe(owner, CoroutineObserver(context, action))


internal class CoroutineObserver<T>(
    override val coroutineContext: CoroutineContext,
    val action: suspend CoroutineScope.(T) -> Unit
) : Observer<T>, CoroutineScope {

    override fun toString(): String = "CoroutineScope(coroutineContext=$coroutineContext)"

    override fun onChanged(value: T) {
        launch { action(value) }
    }
}
