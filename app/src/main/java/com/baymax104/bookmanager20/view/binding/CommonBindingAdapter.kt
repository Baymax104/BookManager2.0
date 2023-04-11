package com.baymax104.bookmanager20.view.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.bookmanager20.adapter.BaseAdapter
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
        (adapter!! as ListAdapter<E, BaseAdapter.BaseViewHolder>).submitList(data)
    }

    @JvmStatic
    @BindingAdapter("state_hasContent")
    fun StateLayout.stateChange(hasContent: Boolean) {
        if (hasContent) {
            showContent()
        } else {
            showEmpty()
        }
    }
}