package com.baymax104.bookmanager20.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlin.properties.Delegates

/**
 *@Description 异步数据源可观察列表
 *@Author John
 *@email
 *@Date 2023/3/25 14:12
 *@Version 1
 */
typealias MData<T> = MutableLiveData<T>
typealias LData<T> = LiveData<T>
typealias MLD<T> = MediatorLiveData<T>

typealias ObserverList<T> = MutableList<ListObserver<T>>

/**
 * 使用观察者模式实现的单线程简易可观察列表
 * 支持数据源与观察者异步设置，数据整体只能由设置的可观察数据源来改变
 */
@Suppress("unused")
class LiveList<E>(observableSource: LData<MutableList<E>>) {

    private var source: MutableList<E> = mutableListOf()

    private val observers: Map<EventType, ObserverList<E>> = mutableMapOf(
        Insert to mutableListOf(),
        Remove to mutableListOf(),
        Set to mutableListOf(),
        Whole to mutableListOf()
    )

    val sourceObserver: MLD<Nothing> = MLD()

    var lifecycleOwner: LifecycleOwner? = null
        set(value) {
            field = value
            if (value != null && !sourceObserver.hasObservers()) {
                sourceObserver.observe(value) {}
            }
        }

    private var listEvent: ListEvent<E> by Delegates.observable(StaticEvent()) { _, _, event ->
        when (event) {
            is StaticEvent -> {}
            is InsertEvent -> observers[Insert]!!.forEach {
                (it as InsertObserver).onAdd(event.index, event.value)
            }
            is RemoveEvent -> observers[Remove]!!.forEach {
                (it as RemoveObserver).onRemove(event.index, event.value)
            }
            is SetEvent -> observers[Set]!!.forEach {
                (it as SetObserver).onUpdate(event.index, event.value)
            }
            is WholeEvent -> observers[Whole]!!.forEach {
                (it as WholeObserver).onChange(event.source)
            }
        }
    }

    init {
        sourceObserver.addSource(observableSource) {
            source = it
            listEvent = WholeEvent(it)
        }
    }

    constructor(observableSource: LData<MutableList<E>>, collection: Collection<E>) : this(
        observableSource
    ) {
        source.addAll(collection)
    }

    fun add(element: E) {
        source.add(element)
        listEvent = InsertEvent(source.size - 1, element)
    }

    fun add(index: Int, element: E) {
        source.add(index, element)
        listEvent = InsertEvent(index, element)
    }

    fun remove(element: E): Boolean {
        val index = source.indexOf(element)
        return if (index == -1) {
            false
        } else {
            source.remove(element)
            listEvent = RemoveEvent(index, element)
            true
        }
    }

    fun removeAt(index: Int): E {
        val value = source.removeAt(index)
        listEvent = RemoveEvent(index, value)
        return value
    }

    fun set(index: Int, element: E): E {
        val previous = source.set(index, element)
        listEvent = SetEvent(index, previous)
        return previous
    }

    fun get(index: Int): E = source[index]

    fun size(): Int = source.size

    inline fun observe(block: Observer.() -> Unit) = Observer().apply(block)

    inline fun observe(owner: LifecycleOwner, block: Observer.() -> Unit) {
        if (lifecycleOwner == null && !sourceObserver.hasObservers()) {
            sourceObserver.observe(owner) {}
        }
        observe(block)
    }


    inner class Observer {
        fun add(onAdd: (index: Int, value: E) -> Unit) {
            observers[Insert]!!.add(InsertObserver(onAdd))
        }

        fun remove(onRemove: (index: Int, value: E) -> Unit) {
            observers[Remove]!!.add(RemoveObserver(onRemove))
        }

        fun set(onUpdate: (index: Int, value: E) -> Unit) {
            observers[Set]!!.add(SetObserver(onUpdate))
        }

        fun whole(onChange: (List<E>) -> Unit) {
            observers[Whole]!!.add(WholeObserver(onChange))
        }
    }

    fun removeObserver(observer: ListObserver<E>) {
        when (observer) {
            is InsertObserver -> observers[Insert]!!.remove(observer)
            is RemoveObserver -> observers[Remove]!!.remove(observer)
            is SetObserver -> observers[Set]!!.remove(observer)
            is WholeObserver -> observers[Whole]!!.remove(observer)
        }
    }

}

sealed class EventType
object Static : EventType()
object Insert : EventType()
object Remove : EventType()
object Set : EventType()
object Whole : EventType()

sealed class ListEvent<V>(
    var type: EventType
)

class StaticEvent<V> : ListEvent<V>(Static)

data class InsertEvent<V>(
    val index: Int,
    val value: V
) : ListEvent<V>(Insert)

data class RemoveEvent<V>(
    val index: Int,
    val value: V
) : ListEvent<V>(Remove)

data class SetEvent<V>(
    val index: Int,
    val value: V
) : ListEvent<V>(Set)

data class WholeEvent<V>(
    val source: List<V>
) : ListEvent<V>(Whole)


sealed interface ListObserver<T>

class InsertObserver<T>(
    val onAdd: (Int, T) -> Unit
) : ListObserver<T>

class RemoveObserver<T>(
    val onRemove: (Int, T) -> Unit
) : ListObserver<T>

class SetObserver<T>(
    val onUpdate: (Int, T) -> Unit
) : ListObserver<T>

class WholeObserver<T>(
    val onChange: (List<T>) -> Unit
) : ListObserver<T>
