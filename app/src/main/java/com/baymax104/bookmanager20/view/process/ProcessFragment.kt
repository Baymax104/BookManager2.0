package com.baymax104.bookmanager20.view.process

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.activityViewModels
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.adapter.ProcessAdapter
import com.baymax104.bookmanager20.architecture.*
import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.architecture.view.BaseFragment
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.domain.ProcessMessenger
import com.baymax104.bookmanager20.domain.ProcessRequester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.*
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import kotlinx.coroutines.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:39
 *@Version 1
 */
@RuntimePermissions
class ProcessFragment : BaseFragment() {

    private val requester: ProcessRequester by activityViewModels()

    private val states: States by fragmentViewModels()

    private val messenger: ProcessMessenger by applicationViewModels()

    private val cameraLauncher = registerLauncher {
        ImageUtil.compress(activity, states.uriPath) { file ->
            messenger.photoUri.reply(file.absolutePath)
        }
    }

    class States : StateHolder() {
        val books = State(listOf<Book>())
        lateinit var uriPath: String
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messenger.photoUri.observeSend(viewLifecycleOwner) { takePhoto() }

        messenger.requestBook.observeReply(viewLifecycleOwner) {
            this@ProcessFragment showOnce BookInfoDialog(activity)
        }

        messenger.requestBook.observeSend(viewLifecycleOwner) { code ->
            requester.requestBookInfo(code) {
                success { reply(it) }
                fail { ToastUtils.showShort(it.message) }
            }
        }

        messenger.insertBook.observeSend(viewLifecycleOwner) { book ->
            requester.insertProcessBook(book) {
                success {
                    states.books.add(it)
                    ToastUtils.showShort("添加成功")
                }
                fail { ToastUtils.showShort("添加失败：${it.message}") }
            }
        }

        requester.queryAllBook {
            success { states.books.value = it }
            fail { ToastUtils.showShort(it.message) }
        }
    }

    override fun configureBinding(): DataBindingConfig {
        val adapter = ProcessAdapter()
        return DataBindingConfig(R.layout.fragment_progress, BR.state, states)
            .add(
                BR.adapter to adapter,
                BR.handler to Handler()
            )
    }

    inner class Handler {
        val add = OnClickListener {
            this@ProcessFragment showOnce AddWayDialog(activity)
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun takePhoto() {
        val file = ImageUtil.createFile()
        if (file == null) {
            ToastUtils.showShort("创建文件错误")
            return
        }
        states.uriPath = file.absolutePath
        val uri = UriUtils.file2Uri(file)
        val intent = IntentUtils.getCaptureIntent(uri)
        cameraLauncher.launch(intent)
    }
}

