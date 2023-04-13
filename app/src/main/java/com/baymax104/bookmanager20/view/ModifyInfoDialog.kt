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
import com.lxj.xpopup.core.BottomPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 14:57
 *@Version 1
 */
class ModifyInfoDialog(context: Context) : BottomPopupView(context) {

    private val messenger: ProcessMessenger by applicationViewModels()

    private val states: States by activityViewModels()

    class States : StateHolder() {
        val book = State(Book())
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_modify_info

    override fun onCreate() {
        super.onCreate()
        bind(BR.state to states, BR.handler to Handler())

        messenger.modifyBook.observeSendSticky(this) {
            states.book.value = it.clone()
        }
    }

    inner class Handler {
        val confirm = OnClickListener {
            messenger.modifyBook.reply(states.book.value)
            dismiss()
        }

        val cancel = OnClickListener { dismiss() }
    }
}