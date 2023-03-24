package com.baymax104.bookmanager20.view.process

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.DialogModifyInfoBinding
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.view.MainActivity
import com.baymax104.bookmanager20.viewModel.ProcessViewModel
import com.lxj.xpopup.core.BottomPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 14:57
 *@Version 1
 */
class ModifyInfoDialog(context: Context) : BottomPopupView(context) {

    private val vm = ViewModelProvider(activity as MainActivity)[ProcessViewModel::class.java]

    override fun getImplLayoutId(): Int = R.layout.dialog_modify_info

    override fun onCreate() {
        super.onCreate()
        val binding = DialogModifyInfoBinding.bind(popupImplView)
        binding.lifecycleOwner = this

        binding.book = Book.copy(vm.requestBook.value)

        binding.setCancel { dismiss() }

        binding.setConfirm {
            vm.requestBook.value = Book.copy(binding.book)
            dismiss()
        }
    }
}