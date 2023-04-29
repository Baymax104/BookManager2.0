package com.baymax104.bookmanager20.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.baymax104.bookmanager20.architecture.domain.Requester
import java.util.*
import kotlin.collections.HashMap

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/11 23:42
 *@Version 1
 */
typealias MData<T> = MutableLiveData<T>
typealias LData<T> = LiveData<T>
typealias MLD<T> = MediatorLiveData<T>

typealias Callback<T> = Requester.ResultCallback<T>.() -> Unit
typealias Null = Nothing?
typealias TreeMap<T> = HashMap<TreeNode<T>, LinkedList<Node<T>>>
