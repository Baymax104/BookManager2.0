package com.baymax104.bookmanager20.architecture.view

import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.baymax104.bookmanager20.architecture.ActivityViewModelScope
import com.lxj.xpopup.core.BasePopupView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 18:42
 *@Version 1
 */
val BasePopupView.viewModelScope: ActivityViewModelScope
    get() = ActivityViewModelScope()

inline fun <reified T> BasePopupView.bind(config: DataBindingConfig): T? {
    val binding: ViewDataBinding = DataBindingUtil.bind(popupImplView) ?: return null
    binding.lifecycleOwner = this
    val (_, vmId, stateVM) = config
    binding.setVariable(vmId, stateVM)
    config.params.forEach { key, value -> binding.setVariable(key, value) }
    return binding as T
}