package com.baymax104.bookmanager20.architecture.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.util.Null
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/9 19:33
 *@Version 1
 */

open class Messenger : ViewModel()

sealed class Event {
    data class SendEvent<E>(val value: E) : Event()
    data class ReplyEvent<E>(val value: E) : Event()
}

open class EventState<S, R> {
    private val sender = UnPeekLiveData<Event.SendEvent<S>>()
    private val replier = UnPeekLiveData<Event.ReplyEvent<R>>()

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

    fun observeSendSticky(owner: LifecycleOwner, action: EventState<S, R>.(S) -> Unit) {
        sender.observeSticky(owner) { action(it.value) }
    }

    fun observeReplySticky(owner: LifecycleOwner, action: EventState<S, R>.(R) -> Unit) {
        replier.observeSticky(owner) { action(it.value) }
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
