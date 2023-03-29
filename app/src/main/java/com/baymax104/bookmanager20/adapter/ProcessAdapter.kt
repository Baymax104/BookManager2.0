package com.baymax104.bookmanager20.adapter

import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.ItemProgressBookBinding
import com.baymax104.bookmanager20.entity.Book
import com.blankj.utilcode.util.ToastUtils

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 20:46
 *@Version 1
 */
class ProcessAdapter : BaseAdapter<Book, ItemProgressBookBinding>(R.layout.item_progress_book) {

    var isEdit = false

    override fun onBind(binding: ItemProgressBookBinding?, item: Book?) {
        binding?.apply {
            book = item
            isEdit = this@ProcessAdapter.isEdit
            root.setOnClickListener { ToastUtils.showShort("点击") }
        }
    }
}