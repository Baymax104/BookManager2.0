package com.baymax104.bookmanager20.repository

import android.content.Context
import com.baymax104.bookmanager20.view.process.AddWayDialog
import com.baymax104.bookmanager20.view.process.BookInfoDialog
import com.baymax104.bookmanager20.view.process.ManualAddDialog
import com.lxj.xpopup.XPopup
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/23 23:18
 *@Version 1
 */
@Module
@InstallIn(ActivityComponent::class)
class DialogModule {

    @Provides
    @ActivityScoped
    fun manualAddDialog(@ActivityContext context: Context) =
        XPopup.Builder(context)
            .asCustom(ManualAddDialog(context)) as ManualAddDialog

    @Provides
    @ActivityScoped
    fun addWayDialog(@ActivityContext context: Context) =
        XPopup.Builder(context)
            .asCustom(AddWayDialog(context)) as AddWayDialog

    @Provides
    @ActivityScoped
    fun bookInfoDialog(@ActivityContext context: Context) =
        XPopup.Builder(context)
            .asCustom(BookInfoDialog(context)) as BookInfoDialog
}