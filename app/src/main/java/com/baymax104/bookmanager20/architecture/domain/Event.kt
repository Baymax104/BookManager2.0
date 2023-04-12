package com.baymax104.bookmanager20.architecture.domain

import androidx.lifecycle.LifecycleOwner
import com.baymax104.bookmanager20.util.MData
import com.baymax104.bookmanager20.util.Null

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/9 19:33
 *@Version 1
 */
sealed class Event {
    data class SendEvent<E>(val value: E) : Event()
    data class ReplyEvent<E>(val value: E) : Event()
}

open class EventState<S, R> {
    private val sender = MData<Event.SendEvent<S>>()
    private val replier = MData<Event.ReplyEvent<R>>()

    fun send(value: S) {
        sender.value = Event.SendEvent(value)
    }

    fun reply(value: R) {
        replier.value = Event.ReplyEvent(value)
    }

    fun observeSend(owner: LifecycleOwner, action: EventState<S, R>.(S) -> Unit) {
        sender.observe(owner) { action(it.value) }
    }

    fun observeReply(owner: LifecycleOwner, action: EventState<S, R>.(R) -> Unit) {
        replier.observe(owner) { action(it.value) }
    }
}

class Sender<S> : EventState<S, Null>() {
    fun reply() {
        super.reply(null)
    }
}

class Replier<R> : EventState<Null, R>() {
    fun send() {
        super.send(null)
    }
}

class Notifier : EventState<Null, Null>() {
    fun send() {
        super.send(null)
    }

    fun reply() {
        super.reply(null)
    }
}
