package com.baymax104.bookmanager20.view.finish

import android.os.Bundle
import android.view.View
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.adapter.FinishAdapter
import com.baymax104.bookmanager20.architecture.domain.State
import com.baymax104.bookmanager20.architecture.domain.StateHolder
import com.baymax104.bookmanager20.architecture.domain.fragmentViewModels
import com.baymax104.bookmanager20.architecture.view.BaseFragment
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:56
 *@Version 1
 */
class FinishFragment : BaseFragment() {

    private val states: States by fragmentViewModels()

    class States : StateHolder() {
        val books = State(listOf<Book>())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun configureBinding() = DataBindingConfig(R.layout.fragment_finish, BR.state, states)
        .add(
            BR.adapter to FinishAdapter()
        )
}