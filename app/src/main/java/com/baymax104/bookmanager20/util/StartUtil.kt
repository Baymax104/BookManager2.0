package com.baymax104.bookmanager20.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View.OnClickListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.blankj.utilcode.util.SnackbarUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlin.reflect.KClass

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/23 23:18
 *@Version 1
 */

infix fun Context.showOnce(dialog: BasePopupView) {
    XPopup.Builder(this)
        .isDestroyOnDismiss(true)
        .asCustom(dialog)
        .show()
}

infix fun Fragment.showOnce(dialog: BasePopupView) {
    requireActivity() showOnce dialog
}

infix fun Context.start(activity: KClass<*>) {
    startActivity(Intent(this, activity.java))
}

infix fun AppCompatActivity.showSnackBar(onClickListener: OnClickListener) {
    SnackbarUtils.with(window.decorView)
        .setMessage("已删除")
        .setMessageColor(Color.WHITE)
        .setBgColor(Color.parseColor("#1e90ff"))
        .setDuration(SnackbarUtils.LENGTH_LONG)
        .setBottomMargin(50)
        .setAction("撤销", Color.WHITE, onClickListener)
        .show()
}

fun Fragment.registerLauncher(onSuccess: () -> Unit): ActivityResultLauncher<Intent> {
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == -1) {
            onSuccess()
        }
    }
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            launcher.unregister()
        }
    })
    return launcher
}
