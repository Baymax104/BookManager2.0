package com.baymax104.bookmanager20.view.process

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.databinding.DialogManualAddBinding
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.Bus
import com.baymax104.bookmanager20.util.mainLaunch
import com.baymax104.bookmanager20.util.todo
import com.baymax104.bookmanager20.view.MainActivity
import com.baymax104.bookmanager20.viewModel.ProcessViewModel
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.core.BottomPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/20 19:25
 *@Version 1
 */
class ManualAddDialog(context: Context) : BottomPopupView(context) {

    private val vm = ViewModelProvider(activity as MainActivity)[ProcessViewModel::class.java]

    override fun getImplLayoutId(): Int = R.layout.dialog_manual_add

    override fun onCreate() {
        super.onCreate()
        val binding = DialogManualAddBinding.bind(popupImplView)

        val book = Book()
        binding.book = book

        vm.photoUri.observe(this) { book.photo = it }

        binding.setTakePhoto { Bus.postTag(this::class todo "takePhoto") }

        binding.setConfirm {
            dismissWith {
                mainLaunch {
                    when (val state = vm.insertBook(book)) {
                        is Success -> ToastUtils.showShort("添加成功")
                        is FAIL -> ToastUtils.showShort("添加失败：${state.error}")
                    }
                    binding.book = Book()
                }
            }
        }

        binding.setCancel {
            binding.book = Book()
            dismiss()
        }
    }
}