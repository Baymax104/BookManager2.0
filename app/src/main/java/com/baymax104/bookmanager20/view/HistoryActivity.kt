package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View.OnClickListener
import androidx.databinding.ViewDataBinding
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.architecture.view.BaseActivity
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.databinding.ActivityHistoryBinding
import com.baymax104.bookmanager20.domain.HistoryMessenger
import com.baymax104.bookmanager20.domain.HistoryRequester
import com.baymax104.bookmanager20.domain.HistoryUnion
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.showOnce
import com.baymax104.bookmanager20.view.adapter.HistoryAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/14 13:23
 *@Version 1
 */
class HistoryActivity : BaseActivity() {

    private val states: States by activityViewModels()

    private val messenger: HistoryMessenger by applicationViewModels()

    private val requester: HistoryRequester by activityViewModels()


    class States : StateHolder() {
        val book = State(Book())
        val union = HistoryUnion(listOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messenger.book.observeSend(this, sticky = true) { book ->
            states.book.value = book.clone()
            requester.queryBookHistory(states.book.value) {
                success { states.union.list = it }
                fail { ToastUtils.showShort(it.message) }
            }
        }

        messenger.updateHistory.observeReply(this) { history ->
            requester.insertHistory(history, states.union) {
                success {
                    states.book.value.progress = it
                    states.union.state += history
                }
                fail { ToastUtils.showShort(it.message) }
            }
        }

    }

    override fun configureBinding(): DataBindingConfig {
        val adapter = HistoryAdapter()
        return DataBindingConfig(R.layout.activity_history, BR.state, states)
            .add(
                BR.adapter to adapter,
                BR.handler to Handler()
            )
    }

    inner class Handler {
        val update = OnClickListener {
            if (states.book.value.progress < 100) {
                messenger.updateHistory.send(states.book.value)
                this@HistoryActivity showOnce UpdateDialog(this@HistoryActivity)
            } else {
                // finish book
            }
        }
    }

    override fun initUIComponent(binding: ViewDataBinding) {
        binding as ActivityHistoryBinding
        BarUtils.setStatusBarLightMode(this, true)
        binding.toolbar.setNavigationIcon(R.drawable.back)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.historyList.itemAnimator = SlideInDownAnimator()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        messenger.book.reply(states.book.value)
    }
}