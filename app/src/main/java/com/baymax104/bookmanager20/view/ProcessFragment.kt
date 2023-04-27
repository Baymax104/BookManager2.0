package com.baymax104.bookmanager20.view

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.architecture.view.BaseFragment
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.databinding.FragmentProcessBinding
import com.baymax104.bookmanager20.domain.EditMessenger
import com.baymax104.bookmanager20.domain.HistoryMessenger
import com.baymax104.bookmanager20.domain.ProcessMessenger
import com.baymax104.bookmanager20.domain.ProcessRequester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.*
import com.baymax104.bookmanager20.view.adapter.ProcessAdapter
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:39
 *@Version 1
 */
class ProcessFragment : BaseFragment() {

    private val requester: ProcessRequester by activityViewModels()

    private val states: States by fragmentViewModels()

    private val messenger: ProcessMessenger by applicationViewModels()

    private val editMessenger: EditMessenger by applicationViewModels()

    private val mainState: MainActivity.States by activityViewModels()

    private val historyMessenger: HistoryMessenger by applicationViewModels()

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

        with(messenger) {

            photoUri.observeSend(viewLifecycleOwner) {
                requestPermission(Manifest.permission.CAMERA) {
                    granted { takePhoto() }
                    denied { ToastUtils.showShort("权限被拒绝，请到权限中心开启权限") }
                }
            }

            requestBook.observeReply(viewLifecycleOwner) {
                this@ProcessFragment showOnce BookInfoDialog(activity)
            }

            requestBook.observeSend(viewLifecycleOwner) { code ->
                requester.requestBookInfo(code) {
                    success { reply(it) }
                    fail { ToastUtils.showShort(it.message) }
                }
            }

            insertBook.observeReply(viewLifecycleOwner) { book ->
                requester.insertProcessBook(book) {
                    success {
                        states.books.add(it)
                        ToastUtils.showShort("添加成功")
                    }
                    fail { ToastUtils.showShort("添加失败：${it.message}") }
                }
            }

        }

        mainState.enterEdit.observe(viewLifecycleOwner) {
            if (mainState.page.value == 0) {
                editMessenger.books.send(states.books.value)
            }
        }

        editMessenger.books.observeReply(viewLifecycleOwner) {
            if (mainState.page.value == 0) {
                states.books.value = it
            }
        }

        historyMessenger.book.observeReply(viewLifecycleOwner) { book ->
            val item = states.books.value.find { it.id == book.id }
            if (item == null) {
                ToastUtils.showShort("更新信息错误")
                return@observeReply
            }
            if (book.progress >= 100) {
                // TODO 通知Finish页添加
                states.books.remove(item)  // 图书已完成，删除该项
            } else {
                item.progress = book.progress
            }
        }

        requester.queryAllBook {
            success { states.books.value = it }
            fail { ToastUtils.showShort(it.message) }
        }
    }

    override fun configureBinding(): DataBindingConfig {
        val adapter = ProcessAdapter()
        adapter.onItemClick = {
            historyMessenger.book.send(it)
            this start HistoryActivity::class
        }

        return DataBindingConfig(R.layout.fragment_process, BR.state, states)
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

    private fun takePhoto() {
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

    override fun initUIComponent(binding: ViewDataBinding) {
        binding as FragmentProcessBinding
        binding.bookList.itemAnimator = SlideInRightAnimator()
    }
}

