package com.baymax104.bookmanager20.practice

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.baymax104.bookmanager20.architecture.domain.ApplicationViewModelStore

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/12 10:40
 *@Version 1
 */
@Deprecated("Use Kotlin extend function：viewModels and activityViewModels...")
open class ApplicationViewModelScope {
    var applicationProvider = ViewModelProvider(ApplicationViewModelStore)
    inline fun <reified T : ViewModel> getFromApplication() = applicationProvider.get<T>()
}

@Deprecated("Use Kotlin extend function：viewModels and activityViewModels...")
open class ActivityViewModelScope(activity: AppCompatActivity) : ApplicationViewModelScope() {
    constructor(activity: Activity) : this(activity as AppCompatActivity)

    val activityProvider = ViewModelProvider(activity)
    inline fun <reified T : ViewModel> getFromActivity() = activityProvider.get<T>()
}

@Deprecated("Use Kotlin extend function：viewModels and activityViewModels...")
class FragmentViewModelScope(fragment: Fragment) :
    ActivityViewModelScope(fragment.requireActivity()) {
    val fragmentProvider = ViewModelProvider(fragment)
    inline fun <reified T : ViewModel> getFromFragment() = fragmentProvider.get<T>()
}
