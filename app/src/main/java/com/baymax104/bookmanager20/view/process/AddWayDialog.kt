package com.baymax104.bookmanager20.view.process

import android.content.Context
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.DialogAddWayBinding
import com.lxj.xpopup.core.CenterPopupView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/20 10:41
 *@Version 1
 */
@AndroidEntryPoint
class AddWayDialog(context: Context) : CenterPopupView(context) {

    @Inject
    lateinit var manualAddDialog: ManualAddDialog

    override fun getImplLayoutId(): Int = R.layout.dialog_add_way

    override fun onCreate() {
        super.onCreate()
        val binding = DialogAddWayBinding.bind(popupImplView)
        binding.lifecycleOwner = this

        binding.setScan { dismissWith { CaptureActivity.actionStart(context) } }

        binding.setManual { dismissWith { manualAddDialog.show() } }
    }

}