package com.baymax104.bookmanager20.view.process

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.architecture.view.bind
import com.baymax104.bookmanager20.architecture.view.viewModelScope
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.databinding.DialogManualAddBinding
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.Bus
import com.baymax104.bookmanager20.util.MData
import com.baymax104.bookmanager20.util.mainLaunch
import com.baymax104.bookmanager20.util.todo
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

    private val vm: ProcessViewModel = viewModelScope.getActivityViewModel(activity)

    private var binding: DialogManualAddBinding? = null

    object State : ViewModel() {
        val book: MData<Book> = MData(Book())
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_manual_add

    override fun onCreate() {
        super.onCreate()
        val config = DataBindingConfig(R.layout.dialog_manual_add, BR.state, State)
            .add(BR.clickHandler to ClickHandler())
        binding = bind(config)

        vm.photoUri.observe(this) { State.book.value?.apply { photo = it } }
    }

    private fun insertBook(book: Book) = mainLaunch {
        when (val state = vm.insertProcessBook(book)) {
            is Success -> ToastUtils.showShort("添加成功")
            is FAIL -> ToastUtils.showShort("添加失败：${state.error}")
        }
    }


    inner class ClickHandler {
        fun takePhoto(v: View): Unit = Bus.postTag(ManualAddDialog::class todo "takePhoto")

        fun confirm(v: View) {
            val book = State.book.value ?: return
            if (book.page <= 0) {
                ToastUtils.showShort("页数必须大于0")
            } else {
                dismissWith { insertBook(book) }
            }
        }

        fun cancel(v: View) {
            dismiss()
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        State.book.value = Book()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
        binding = null
    }
}