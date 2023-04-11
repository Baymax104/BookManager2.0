package com.baymax104.bookmanager20.view.process

import android.content.Context
import android.view.View.OnClickListener
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.State
import com.baymax104.bookmanager20.architecture.domain.StateHolder
import com.baymax104.bookmanager20.architecture.domain.activityViewModels
import com.baymax104.bookmanager20.architecture.domain.applicationViewModels
import com.baymax104.bookmanager20.architecture.view.bind
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.domain.ProcessMessenger
import com.baymax104.bookmanager20.domain.ProcessRequester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.mainLaunch
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

    private val messenger: ProcessMessenger by applicationViewModels()

    private val requester: ProcessRequester by activityViewModels()

    object States : StateHolder() {
        val book = State(Book())
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_manual_add

    override fun onCreate() {
        super.onCreate()
        bind(BR.state to States, BR.handler to Handler())

        messenger.photoUri.observeReply(this) {
            States.book.value.photo = it
        }
    }

    inner class Handler {

        val takePhoto = OnClickListener { messenger.photoUri.post() }

        val confirm = OnClickListener {
            val book = States.book.value
            if (book.page <= 0) {
                ToastUtils.showShort("页数必须大于0")
            } else {
                dismissWith { insertBook(book) }
            }
        }

        val cancel = OnClickListener { dismiss() }
    }


    private fun insertBook(book: Book) = mainLaunch {
        when (val state = requester.insertProcessBook(book)) {
            is Success -> ToastUtils.showShort("添加成功")
            is FAIL -> ToastUtils.showShort("添加失败：${state.error}")
        }
    }
}