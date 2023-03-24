package com.baymax104.bookmanager20.view.process

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.databinding.DialogBookInfoBinding
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.ImageUtil
import com.baymax104.bookmanager20.util.mainLaunch
import com.baymax104.bookmanager20.view.MainActivity
import com.baymax104.bookmanager20.viewModel.ProcessViewModel
import com.blankj.utilcode.util.ToastUtils
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

        binding.setConfirm {
            dismissWith {
                val book: Book? = binding.book
                if (book == null) {
                    ToastUtils.showShort("添加为空")
                    return@dismissWith
                }
                mainLaunch {
                    book.photo?.let {
                        val file = ImageUtil.download(activity, it)
                        book.photo = file?.absolutePath
                    }
                    when (val state = vm.insertBook(book)) {
                        is Success -> ToastUtils.showShort("添加成功")
                        is FAIL -> ToastUtils.showShort("添加失败：${state.error}")
                    }
                }
            }
        }

        binding.setCancel { dismiss() }
    }
}