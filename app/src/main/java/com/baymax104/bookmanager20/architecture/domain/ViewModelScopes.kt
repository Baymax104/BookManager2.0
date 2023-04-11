package com.baymax104.bookmanager20.architecture.domain

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 16:34
 *@Version 1
 */

object ApplicationViewModelStore : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

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


@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.activityViewModels(): Lazy<VM> =
    viewModels()

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.applicationViewModels(): Lazy<VM> =
    ViewModelLazy(
        VM::class,
        { ApplicationViewModelStore.viewModelStore },
        { defaultViewModelProviderFactory },
        { defaultViewModelCreationExtras }
    )

@MainThread
inline fun <reified VM : ViewModel> Fragment.applicationViewModels(): Lazy<VM> =
    viewModels({ ApplicationViewModelStore })

@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModels(): Lazy<VM> =
    activityViewModels()

@MainThread
inline fun <reified VM : ViewModel> Fragment.fragmentViewModels(): Lazy<VM> =
    viewModels()

@MainThread
inline fun <reified VM : ViewModel> BasePopupView.activityViewModels(): Lazy<VM> {
    val activity = XPopupUtils.context2Activity(context) as ComponentActivity
    return activity.viewModels()
}

@MainThread
inline fun <reified VM : ViewModel> BasePopupView.applicationViewModels(): Lazy<VM> {
    val activity = XPopupUtils.context2Activity(context) as ComponentActivity
    return activity.applicationViewModels()
}