package com.baymax104.bookmanager20.util

import kotlin.properties.Delegates

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/25 14:12
 *@Version 1
 */

typealias ListObserverList<T> = MutableList<ListObserver<T>>

class LiveList<T>(override var size: Int) : AbstractMutableList<T>() {

    private val data: MutableList<T> = mutableListOf()

    var observers: Map<EventType, ListObserverList<T>> = mutableMapOf(
        Insert to mutableListOf(),
        Remove to mutableListOf(),
        Set to mutableListOf()
    )

    private var listEvent: ListEvent<T> by Delegates.observable(StaticEvent()) { _, _, event ->
        when (event) {
            is StaticEvent -> {}
            is InsertEvent -> observers[Insert]!!.forEach {
                (it as InsertObserver).onAdd(
                    event.index,
                    event.value
                )
            }
            is RemoveEvent -> observers[Remove]!!.forEach {
                (it as RemoveObserver).onRemove(
                    event.index,
                    event.value
                )
            }
            is SetEvent -> observers[Set]!!.forEach {
                (it as SetObserver).onUpdate(
                    event.index,
                    event.value
                )
            }
        }
    }

    constructor() : this(0)

    constructor(collection: Collection<T>) : this(collection.size) {
        data.addAll(collection)
    }


    override fun add(element: T): Boolean {
        val add = data.add(element)
        if (add) {
            size = data.size
            listEvent = InsertEvent(size - 1, element)
        }
        return add
    }

    override fun add(index: Int, element: T) {
        data.add(index, element)
        listEvent = InsertEvent(index, element)
    }

    override fun remove(element: T): Boolean {
        val index = data.indexOf(element)
        val remove = data.remove(element)
        if (remove) {
            size = data.size
            listEvent = RemoveEvent(index, element)
        }
        return remove
    }

    override fun removeAt(index: Int): T {
        val value = data.removeAt(index)
        size = data.size
        listEvent = RemoveEvent(index, value)
        return value
    }

    override fun get(index: Int): T = data[index]

    override fun set(index: Int, element: T): T {
        val previous = data.set(index, element)
        listEvent = SetEvent(index, previous)
        return previous
    }

    fun observeAdd(onAdd: (index: Int, value: T) -> Unit): LiveList<T> {
        observers[Insert]!!.add(InsertObserver(onAdd))
        return this
    }

    fun observeRemove(onRemove: (index: Int, value: T) -> Unit): LiveList<T> {
        observers[Remove]!!.add(RemoveObserver(onRemove))
        return this
    }

    fun observeSet(onUpdate: (index: Int, value: T) -> Unit): LiveList<T> {
        observers[Set]!!.add(SetObserver(onUpdate))
        return this
    }

    fun removeObserver(observer: ListObserver<T>) {
        when (observer) {
            is InsertObserver -> observers[Insert]!!.remove(observer)
            is RemoveObserver -> observers[Remove]!!.remove(observer)
            is SetObserver -> observers[Set]!!.remove(observer)
        }
    }

    class Builder<T> {

        private val observers: Map<EventType, ListObserverList<T>> = mutableMapOf(
            Insert to mutableListOf(),
            Remove to mutableListOf(),
            Set to mutableListOf()
        )

        fun add(onAdd: (index: Int, value: T) -> Unit) {
            observers[Insert]!!.add(InsertObserver(onAdd))
        }

        fun remove(onRemove: (index: Int, value: T) -> Unit) {
            observers[Remove]!!.add(RemoveObserver(onRemove))
        }

        fun set(onUpdate: (index: Int, value: T) -> Unit) {
            observers[Set]!!.add(SetObserver(onUpdate))
        }

        fun bind(list: LiveList<T>) = list.apply {
            observers = this@Builder.observers.toMap()
        }

        fun observe(block: Builder<T>.() -> Unit) = also(block)
    }
}

sealed class EventType
object Static : EventType()
object Insert : EventType()
object Remove : EventType()
object Set : EventType()

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
