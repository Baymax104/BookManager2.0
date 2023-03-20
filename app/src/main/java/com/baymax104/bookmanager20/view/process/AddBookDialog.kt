package com.baymax104.bookmanager20.view.process

import android.content.Context
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.DialogAddBookBinding
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/20 10:41
 *@Version 1
 */
class AddBookDialog(context: Context) : CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_add_book

    override fun onCreate() {
        super.onCreate()
        val binding = DialogAddBookBinding.bind(popupImplView)
        binding.lifecycleOwner = popupImplView.findViewTreeLifecycleOwner()
        binding.scanAdd.setOnClickListener {
            dismissWith { CaptureActivity.actionStart(context) }
        }
        binding.manualAdd.setOnClickListener {
            dismissWith {
                XPopup.Builder(context).asCustom(ManualAddDialog(context)).show()
            }
        }
    }

}