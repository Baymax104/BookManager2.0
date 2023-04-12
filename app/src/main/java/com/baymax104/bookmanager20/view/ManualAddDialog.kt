package com.baymax104.bookmanager20.view

import android.content.Context
import android.view.View.OnClickListener
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.State
import com.baymax104.bookmanager20.architecture.domain.StateHolder
import com.baymax104.bookmanager20.architecture.domain.activityViewModels
import com.baymax104.bookmanager20.architecture.domain.applicationViewModels
import com.baymax104.bookmanager20.architecture.view.bind
import com.baymax104.bookmanager20.domain.ProcessMessenger
import com.baymax104.bookmanager20.entity.Book
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

    private val states: States by activityViewModels()

    class States : StateHolder() {
        val book = State(Book())
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_manual_add

    override fun onCreate() {
        super.onCreate()
        bind(BR.state to states, BR.handler to Handler())

        messenger.photoUri.observeReply(this) {
            states.book.value.photo = it
        }
    }

    inner class Handler {

        val takePhoto = OnClickListener { messenger.photoUri.send() }

        val confirm = OnClickListener {
            val book = states.book.value
            if (book.page <= 0) {
                ToastUtils.showShort("页数必须大于0")
            } else {
                dismissWith { messenger.insertBook.send(book) }
            }
        }

        val cancel = OnClickListener { dismiss() }
    }
}