package com.baymax104.bookmanager20.architecture

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Singleton

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

@Singleton
open class ApplicationViewModelScope @Inject constructor() {
    var applicationProvider = ViewModelProvider(ApplicationViewModelStore)
    inline fun <reified T : ViewModel> getApplicationViewModel() = applicationProvider.get<T>()
}

@ActivityScoped
open class ActivityViewModelScope @Inject constructor() : ApplicationViewModelScope() {
    var activityProvider: ViewModelProvider? = null
    inline fun <reified T : ViewModel> getActivityViewModel(activity: AppCompatActivity): T {
        if (activityProvider == null) {
            activityProvider = ViewModelProvider(activity)
        }
        return activityProvider!!.get()
    }

    inline fun <reified T : ViewModel> getActivityViewModel(activity: Activity): T =
        getActivityViewModel(activity as AppCompatActivity)
}

@FragmentScoped
class FragmentViewModelScope @Inject constructor() : ActivityViewModelScope() {
    var fragmentProvider: ViewModelProvider? = null
    inline fun <reified T : ViewModel> getFragmentViewModel(fragment: Fragment): T {
        if (fragmentProvider == null) {
            fragmentProvider = ViewModelProvider(fragment)
        }
        return fragmentProvider!!.get()
    }
}