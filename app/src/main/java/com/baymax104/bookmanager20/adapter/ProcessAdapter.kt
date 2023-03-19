package com.baymax104.bookmanager20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.ItemProgressBookBinding
import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 20:46
 *@Version 1
 */
class ProcessAdapter : BaseAdapter<Book>() {

    var isEdit = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_progress_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val book = data?.get(holder.absoluteAdapterPosition)
        val binding = (holder as ViewHolder).binding
        binding.book = book
        binding.isEdit = isEdit
    }

    private class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val binding: ItemProgressBookBinding = ItemProgressBookBinding.bind(itemView)
    }
}