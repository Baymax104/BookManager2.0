package com.baymax104.bookmanager20.architecture.interfaces

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/27 22:28
 *@Version 1
 */
interface Clone<T> {
    fun clone(): T
}

fun <T : Clone<T>> List<T>.deepClone(): MutableList<T> {
    val copyList = mutableListOf<T>()
    forEach { copyList.add(it.clone()) }
    return copyList
}
