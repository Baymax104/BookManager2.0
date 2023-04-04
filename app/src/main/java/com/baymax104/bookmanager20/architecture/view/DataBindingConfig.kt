package com.baymax104.bookmanager20.architecture.view

import android.util.SparseArray
import androidx.lifecycle.ViewModel

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 15:14
 *@Version 1
 */
data class DataBindingConfig(
    val layout: Int,
    val vmId: Int,
    val stateVM: ViewModel
) {
    val params: SparseArray<Any> = SparseArray()

    fun add(vararg param: Pair<Int, Any>): DataBindingConfig {
        for (p in param) {
            if (params[p.first] == null) {
                params.put(p.first, p.second)
            }
        }
        return this
    }
}