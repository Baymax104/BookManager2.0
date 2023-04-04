package com.baymax104.bookmanager20.viewModel

import androidx.lifecycle.ViewModel
import com.baymax104.bookmanager20.repository.MainRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/2 12:07
 *@Version 1
 */

// TODO 两大核心问题
//  列表更新的回调
//  ViewModel的数据共享：将一个页面的ViewModel分为页面状态VM和事件VM
//  使用一个处理事件的中央ViewModel，事件VM是全局的

@Singleton
class EditViewModel @Inject constructor(val repo: MainRepository) : ViewModel() {

}