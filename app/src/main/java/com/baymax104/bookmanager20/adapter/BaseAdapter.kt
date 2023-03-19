package com.baymax104.bookmanager20.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 20:30
 *@Version 1
 */
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    var data: MutableList<T>? = null

    var onClick: (T) -> Unit = {}

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = data?.size ?: 0
}