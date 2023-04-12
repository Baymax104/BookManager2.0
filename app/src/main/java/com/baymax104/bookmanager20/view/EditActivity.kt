package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.architecture.view.BaseActivity
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.databinding.ActivityEditBinding
import com.baymax104.bookmanager20.domain.EditMessenger
import com.baymax104.bookmanager20.domain.EditRequester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.lightClone
import com.baymax104.bookmanager20.util.showSnackBar
import com.baymax104.bookmanager20.view.adapter.EditAdapter
import com.drake.statusbar.immersive
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/12 10:58
 *@Version 1
 */
class EditActivity : BaseActivity() {

    private val states: States by activityViewModels()

    private val messenger: EditMessenger by applicationViewModels()

    private val requester: EditRequester by activityViewModels()

    class States : StateHolder() {
        val books = State(listOf<Book>())
        val stack = State(ArrayList<EditAdapter.RemovedItem>())
        var stackLength = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messenger.books.observeSend(this) {
            states.books.value = it.lightClone()
        }

        states.stack.observe(this) {
            states.apply {
                // stackLength 为一轮添加中 stack 的最大长度，当点击撤销时一轮添加停止
                // 撤销时最新的数据将被删除，确定存入的个数为 stack.size - 1 ，stackLength 记录删除前的 stack.size
                // 当 stack.size >= stackLength 时，表示有新数据添加
                // 有数据添加时才显示 SnackBar ，防止撤销时对 stack 的操作触发观察者更新
                if (stack.value.size != 0 && stack.value.size >= stackLength) {
                    this@EditActivity showSnackBar {
                        stackLength = stack.value.size
                        val last = stack.value.last()
                        stack.remove(last)
                        books.add(last.index, last.value)
                    }
                }
            }
        }
    }

    override fun configureBinding(): DataBindingConfig {
        val adapter = EditAdapter()
        adapter.onMoved = { i, j -> Collections.swap(states.books.value, i, j) }
        adapter.onSwiped = {
            states.books.remove(it.value)
            states.stack.add(it)
        }

        return DataBindingConfig(R.layout.activity_edit, BR.state, states)
            .add(BR.adapter to adapter)
    }

    override fun initUIComponent(binding: ViewDataBinding) {
        binding as ActivityEditBinding
        immersive(binding.toolbar, true)
        binding.toolbar.setNavigationIcon(R.drawable.back)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val itemTouchHelper = ItemTouchHelper(binding.adapter!!.touchCallback)
        itemTouchHelper.attachToRecyclerView(binding.bookList)
        binding.bookList.itemAnimator = SlideInRightAnimator()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        requester.deleteBooks(states.stack.value) {
            messenger.books.reply(states.books.value)
        }
    }
}