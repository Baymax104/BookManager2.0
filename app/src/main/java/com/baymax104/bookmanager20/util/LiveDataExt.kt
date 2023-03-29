package com.baymax104.bookmanager20.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/29 12:41
 *@Version 1
 */
typealias MData<T> = MutableLiveData<T>
typealias LData<T> = LiveData<T>
typealias MLD<T> = MediatorLiveData<T>

fun <E> MLD<LiveList<E>>.add(element: E) {
    val v = value
    v?.add(element)
    value = v
}

fun <E> MLD<LiveList<E>>.remove(element: E) {
    val v = value
    value?.remove(element)
    value = v
}