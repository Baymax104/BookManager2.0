package com.baymax104.bookmanager20.util

import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.SimpleCallback

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/24 23:25
 *@Version 1
 */
class PermissionCallback : SimpleCallback {
    var granted: () -> Unit = {}
    var denied: () -> Unit = {}

    companion object {
        inline fun build(block: PermissionCallback.() -> Unit) =
            PermissionCallback().apply(block)
    }

    fun granted(action: () -> Unit) = apply { granted = action }
    fun denied(action: () -> Unit) = apply { denied = action }

    override fun onGranted() = granted()
    override fun onDenied() = denied()
}

inline fun requestPermission(permission: String, block: PermissionCallback.() -> Unit) {
    val callback = PermissionCallback.build(block)
    if (PermissionUtils.isGranted(permission)) {
        callback.granted()
    } else {
        PermissionUtils.permission(permission).callback(callback).request()
    }
}