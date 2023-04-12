package com.baymax104.bookmanager20.architecture.view

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.lxj.xpopup.core.BasePopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 18:42
 *@Version 1
 */
fun BasePopupView.bind(vararg params: Pair<Int, Any>) {
    // binding
    val binding: ViewDataBinding = DataBindingUtil.bind(popupImplView) ?: return
    binding.lifecycleOwner = this
    params.forEach { binding.setVariable(it.first, it.second) }

    // register unbind
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            binding.unbind()
        }
    })
}