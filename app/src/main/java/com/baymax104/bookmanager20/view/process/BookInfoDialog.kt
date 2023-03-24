package com.baymax104.bookmanager20.view.process

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.DialogBookInfoBinding
import com.baymax104.bookmanager20.view.MainActivity
import com.baymax104.bookmanager20.viewModel.ProcessViewModel
import com.lxj.xpopup.core.BottomPopupView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 13:00
 *@Version 1
 */
@AndroidEntryPoint
class BookInfoDialog(context: Context) : BottomPopupView(context) {

    private val vm = ViewModelProvider(activity as MainActivity)[ProcessViewModel::class.java]

    @Inject
    lateinit var modifyInfoDialog: ModifyInfoDialog

    override fun getImplLayoutId(): Int = R.layout.dialog_book_info

    override fun onCreate() {
        super.onCreate()
        val binding = DialogBookInfoBinding.bind(popupImplView)
        binding.lifecycleOwner = this

        vm.requestBook.observe(this) { binding.book = it }

        binding.setModify { modifyInfoDialog.show() }

        binding.setConfirm { }

        binding.setCancel { dismiss() }
    }
}