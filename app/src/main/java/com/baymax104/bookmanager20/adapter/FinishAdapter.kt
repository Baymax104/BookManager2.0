package com.baymax104.bookmanager20.adapter

import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.ItemFinishBookBinding
import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 22:27
 *@Version 1
 */
class FinishAdapter : BaseAdapter<Book, ItemFinishBookBinding>(
    R.layout.item_finish_book,
    { old, new -> old.id == new.id },
    { old, new -> old == new }
) {

    var isEdit = false

    override fun onBind(binding: ItemFinishBookBinding, item: Book) {
        binding.book = item
        binding.isEdit = isEdit
    }
}