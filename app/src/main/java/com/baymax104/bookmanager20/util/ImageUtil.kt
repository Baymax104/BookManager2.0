package com.baymax104.bookmanager20.util

import android.content.Context
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/21 23:25
 *@Version 1
 */
object ImageUtil {

    fun createFile(): File? {
        val filename = "${DateDetailFormatter.format(Date())}.jpg"
        val parent = PathUtils.getExternalAppFilesPath()
        if (parent == "") {
            return null
        }
        val file = File(parent, filename)
        file.createNewFile()
        return file
    }

    fun createCacheFile(): File? {
        val filename = "${DateDetailFormatter.format(Date())}.jpg"
        val parent = PathUtils.getExternalAppCachePath()
        if (parent == "") {
            return null
        }
        val file = File(parent, filename)
        file.createNewFile()
        return file
    }

    fun compress(context: Context, path: String, onSuccess: (File?) -> Unit) {
        val parent = PathUtils.getExternalAppFilesPath()
        Luban.with(context)
            .load(path)
            .setTargetDir(parent)
            .ignoreBy(80)
            .setRenameListener { "${DateDetailFormatter.format(Date())}.jpg" }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(file: File?) {
                    onSuccess(file)
                }

                override fun onError(e: Throwable?) {
                    ToastUtils.showShort("图片压缩失败：$e")
                }
            })
            .launch()
    }

    fun download() {

    }
}