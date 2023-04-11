package com.baymax104.bookmanager20.adapter

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
class ProcessAdapter : BaseAdapter<Book, ItemProgressBookBinding>(
    R.layout.item_progress_book,
    { old, new -> old.id == new.id },
    { old, new -> old == new }
) {

    var isEdit = false

    override fun onBind(binding: ItemProgressBookBinding, item: Book) {
        binding.book = item
        binding.isEdit = isEdit
        binding.root.setOnClickListener { onItemClick(item) }
    }
}