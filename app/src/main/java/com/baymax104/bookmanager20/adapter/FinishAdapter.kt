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
class FinishAdapter : BaseAdapter<Book, ItemFinishBookBinding>(R.layout.item_finish_book) {

    var isEdit = false

    override fun onBind(binding: ItemFinishBookBinding?, item: Book?) {
        binding?.apply {
            book = item
            isEdit = this@FinishAdapter.isEdit
        }
    }
}