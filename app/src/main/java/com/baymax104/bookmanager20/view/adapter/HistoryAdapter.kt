package com.baymax104.bookmanager20.view.adapter

import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.ItemHistoryBinding
import com.baymax104.bookmanager20.entity.History

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/14 13:19
 *@Version 1
 */
class HistoryAdapter : BaseAdapter<History, ItemHistoryBinding>(
    R.layout.item_history,
    { old, new -> old.id == new.id },
    { old, new -> old == new }
) {
    override fun onBind(binding: ItemHistoryBinding, item: History) {
        binding.history = item
    }

    override fun submitList(list: MutableList<History>?) {
        val copyList = list?.reversed()
        super.submitList(copyList)
    }
}