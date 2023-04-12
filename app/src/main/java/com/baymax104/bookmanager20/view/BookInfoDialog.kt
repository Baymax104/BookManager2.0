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
import com.baymax104.bookmanager20.util.ImageUtil
import com.baymax104.bookmanager20.util.mainLaunch
import com.baymax104.bookmanager20.util.showOnce
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.core.BottomPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 13:00
 *@Version 1
 */
class BookInfoDialog(context: Context) : BottomPopupView(context) {

    private val messenger: ProcessMessenger by applicationViewModels()

    private val states: States by activityViewModels()

    class States : StateHolder() {
        val book = State(Book())
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_book_info

    override fun onCreate() {
        super.onCreate()
        bind(BR.state to states, BR.handler to Handler())

        messenger.requestBook.observeReply(this) { states.book.value = it }

        messenger.modifyBook.observeSend(this) { activity showOnce ModifyInfoDialog(activity) }

        messenger.modifyBook.observeReply(this) { states.book.value = it }
    }

    inner class Handler {
        val modify = OnClickListener { messenger.modifyBook.post(states.book.value) }

        val confirm = OnClickListener {
            if (states.book.value.page <= 0) {
                ToastUtils.showShort("页数必须大于0")
                return@OnClickListener
            }
            dismissWith {
                mainLaunch {
                    val book = states.book.value
                    book.photo = book.photo?.let {
                        val file = ImageUtil.download(activity, it)
                        file?.absolutePath ?: it
                    }
                    messenger.insertBook.post(book)
                }
            }
        }

        val cancel = OnClickListener { dismiss() }
    }
}