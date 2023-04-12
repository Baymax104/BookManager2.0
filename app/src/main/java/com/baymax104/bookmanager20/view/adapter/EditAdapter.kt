package com.baymax104.bookmanager20.view.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.ItemEditBookBinding
import com.baymax104.bookmanager20.entity.Book

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/12 11:02
 *@Version 1
 */
class EditAdapter : BaseAdapter<Book, ItemEditBookBinding>(
    R.layout.item_edit_book,
    { old, new -> old.id == new.id },
    { old, new -> old == new }
) {

    // 更新数据回调
    var onMoved: (Int, Int) -> Unit = { _, _ -> }
    var onSwiped: (RemovedItem) -> Unit = {}

    val touchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val i = viewHolder.absoluteAdapterPosition
            val j = target.absoluteAdapterPosition
            // 拖动交换是主动的连续操作，不可通过submitList来触发更新
            notifyItemMoved(i, j)
            onMoved(i, j)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            onSwiped(RemovedItem(position, currentList[position]))
        }
    }

    data class RemovedItem(val index: Int, val value: Book)

    override fun onBind(binding: ItemEditBookBinding, item: Book) {
        binding.book = item
    }
}