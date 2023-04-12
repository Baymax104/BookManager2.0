package com.baymax104.bookmanager20.domain

import com.baymax104.bookmanager20.architecture.domain.EventState
import com.baymax104.bookmanager20.architecture.domain.Messenger
import com.baymax104.bookmanager20.architecture.domain.Sender
import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/12 11:54
 *@Version 1
 */
class EditMessenger : Messenger() {
    // Edit页与主页共享的Book列表
    val books = EventState<List<Book>, List<Book>>()

    // 打开Edit页
    val isEdit = Sender<Int>()
}