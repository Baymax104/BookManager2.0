package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.View
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.*
import com.baymax104.bookmanager20.architecture.view.BaseFragment
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.domain.EditMessenger
import com.baymax104.bookmanager20.domain.FinishRequester
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.view.adapter.FinishAdapter
import com.blankj.utilcode.util.ToastUtils

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:56
 *@Version 1
 */
class FinishFragment : BaseFragment() {

    private val states: States by fragmentViewModels()

    private val requester: FinishRequester by activityViewModels()

    private val editMessenger: EditMessenger by applicationViewModels()

    class States : StateHolder() {
        val books = State(listOf<Book>())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requester.queryAllBook {
            success { states.books.value = it }
            fail { ToastUtils.showShort(it.message) }
        }

        editMessenger.isEdit.observeSend(viewLifecycleOwner) {
            if (it == 1) {
                editMessenger.books.post(states.books.value)
            }
        }
    }

    override fun configureBinding(): DataBindingConfig {
        val adapter = FinishAdapter()
        return DataBindingConfig(R.layout.fragment_finish, BR.state, states)
            .add(BR.adapter to adapter)
    }
}