package com.baymax104.bookmanager20.architecture.domain

import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.util.MData
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/9 11:17
 *@Version 1
 */
open class StateHolder : ViewModel()

class State<E>(value: E) : MData<E>(value) {
    override fun getValue(): E =
        super.getValue() ?: throw NullPointerException("state value is null")
}

fun <E> State<out List<E>>.add(element: E) {
    val list = value
    list as MutableList<E>
    list.add(element)
    value = list
}

fun <E> State<out List<E>>.add(i: Int, element: E) {
    val list = value
    list as MutableList<E>
    list.add(i, element)
    value = list
}

fun <E> State<out List<E>>.remove(element: E) {
    val list = value
    list as MutableList<E>
    list.remove(element)
    value = list
}

fun <E> State<out List<E>>.swap(i: Int, j: Int) {
    val list = value
    list as MutableList<E>
    Collections.swap(list, i, j)
    value = list
}