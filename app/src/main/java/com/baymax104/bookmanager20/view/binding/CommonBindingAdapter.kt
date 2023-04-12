package com.baymax104.bookmanager20.view.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.bookmanager20.view.adapter.BaseAdapter
import com.drake.statelayout.StateLayout

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/7 23:18
 *@Version 1
 */
@Suppress("Unchecked_Cast")
object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("recycler_data")
    fun <E> RecyclerView.recyclerData(data: List<E>) {
        // submitList在新旧list是同一个对象时会直接返回
        val copyList = ArrayList(data)
        (adapter!! as ListAdapter<E, BaseAdapter.BaseViewHolder>).submitList(copyList)
    }

    @JvmStatic
    @BindingAdapter("state_hasContent")
    fun <E> StateLayout.stateChange(data: List<E>) {
        if (data.isNotEmpty()) {
            showContent()
        } else {
            showEmpty()
        }
    }
}