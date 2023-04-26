package com.baymax104.bookmanager20.architecture.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.util.Null
import com.jeremyliao.liveeventbus.core.LiveEventBusCore

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/9 19:33
 *@Version 1
 */

open class Messenger : ViewModel() {

    protected val bus = LiveEventBusCore.get()!!
    inline fun <reified T> LiveEventBusCore.with(key: String) = with(key, T::class.java)!!

    data class Event<E>(val value: E)

    open inner class BiEvent<S, R> {
        @PublishedApi
        internal val sender = bus.with<Event<S>>("bi-sender-${super.hashCode()}")

        @PublishedApi
        internal val replier = bus.with<Event<R>>("bi-replier-${super.hashCode()}")

        fun send(value: S) = apply { sender.post(Event(value)) }
        fun reply(value: R) = apply { replier.post(Event(value)) }

        inline fun observeSend(
            owner: LifecycleOwner,
            sticky: Boolean = false,
            crossinline action: BiEvent<S, R>.(S) -> Unit
        ) {
            if (sticky) {
                sender.observeSticky(owner) { action(it!!.value) }
            } else {
                sender.observe(owner) { action(it!!.value) }
            }
        }

        inline fun observeReply(
            owner: LifecycleOwner,
            sticky: Boolean = false,
            crossinline action: BiEvent<S, R>.(R) -> Unit
        ) {
            if (sticky) {
                replier.observeSticky(owner) { action(it!!.value) }
            } else {
                replier.observe(owner) { action(it!!.value) }
            }
        }
    }

    inner class UniEvent<R> : BiEvent<Null, R>() {
        fun send() = super.send(null)
    }

}