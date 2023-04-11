package com.baymax104.bookmanager20.util

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
    XPopup.Builder(requireActivity())
        .isDestroyOnDismiss(true)
        .asCustom(dialog)
        .show()
}

infix fun Context.start(activity: KClass<*>) {
    startActivity(Intent(this, activity.java))
}

fun Fragment.registerLauncher(onSuccess: () -> Unit): ActivityResultLauncher<Intent> {
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == -1) {
            onSuccess()
        }
    }
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            Log.i("BM-", "Launcher解除")
            launcher.unregister()
        }
    })
    return launcher
}
