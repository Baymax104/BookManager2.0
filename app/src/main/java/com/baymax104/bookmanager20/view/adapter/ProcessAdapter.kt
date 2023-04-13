package com.baymax104.bookmanager20.view.adapter

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

    override fun onBind(binding: ItemProgressBookBinding, item: Book) {
        binding.book = item
        binding.root.setOnClickListener { onItemClick(item) }
    }

    override fun submitList(list: MutableList<Book>?) {
        val copyList = if (list != null) ArrayList(list) else null
        super.submitList(copyList)
    }
}