package com.baymax104.bookmanager20.view.process

import android.content.Context
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.DialogManualAddBinding
import com.baymax104.bookmanager20.entity.Book
import com.lxj.xpopup.core.BottomPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/20 19:25
 *@Version 1
 */
class ManualAddDialog(context: Context) : BottomPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_manual_add

    override fun onCreate() {
        super.onCreate()
        val binding = DialogManualAddBinding.bind(popupImplView)
        val book = Book.build()
        binding.book = book

        binding.confirm.setOnClickListener { }
        binding.cancel.setOnClickListener { dismiss() }
    }
}