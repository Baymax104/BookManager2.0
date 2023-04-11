package com.baymax104.bookmanager20.view.process

import android.content.Context
import android.view.View.OnClickListener
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.view.bind
import com.baymax104.bookmanager20.util.showOnce
import com.baymax104.bookmanager20.util.start
import com.lxj.xpopup.core.CenterPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/20 10:41
 *@Version 1
 */
class AddWayDialog(context: Context) : CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_add_way

    override fun onCreate() {
        super.onCreate()
        bind(BR.handler to Handler())
    }

    inner class Handler {
        val scan = OnClickListener {
            dismissWith { activity start CaptureActivity::class }
        }
        val manual = OnClickListener {
            dismissWith { activity showOnce ManualAddDialog(activity) }
        }
    }

}