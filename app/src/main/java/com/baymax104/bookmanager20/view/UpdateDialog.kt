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
import com.baymax104.bookmanager20.domain.HistoryMessenger
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.util.showTimePicker
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.core.CenterPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/16 0:56
 *@Version 1
 */
class UpdateDialog(context: Context) : CenterPopupView(context) {

    private val messenger: HistoryMessenger by applicationViewModels()

    private val states: States by activityViewModels()

    class States : StateHolder() {
        val history = State(History())
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_update

    override fun onCreate() {
        super.onCreate()
        bind(BR.state to states, BR.handler to Handler())

        messenger.updateHistory.observeSendSticky(this) {
            states.history.value = History(it)
        }
    }

    inner class Handler {

        val selectDate = OnClickListener {
            activity showTimePicker { states.history.value.updateTime = it }
        }

        val confirm = OnClickListener {
            val history = states.history.value
            if (history.start < 1 || history.start > history.total) {
                ToastUtils.showShort("起始页数错误")
                return@OnClickListener
            }
            if (history.end < 1 || history.end > history.total || history.end < history.start) {
                ToastUtils.showShort("结束页数错误")
                return@OnClickListener
            }
            dismissWith { messenger.updateHistory.reply(history) }
        }

        val cancel = OnClickListener { dismiss() }
    }

    override fun onDismiss() {
        states.history.value = History()
    }
}