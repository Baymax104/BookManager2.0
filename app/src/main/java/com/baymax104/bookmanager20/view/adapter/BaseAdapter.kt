package com.baymax104.bookmanager20.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 20:30
 *@Version 1
 */
@Suppress("NotifyDataSetChanged")
abstract class BaseAdapter<E : Any, B : ViewDataBinding>(
    @LayoutRes val layoutId: Int,
    areItemsSame: (old: E, new: E) -> Boolean,
    areContentsSame: (old: E, new: E) -> Boolean
) : ListAdapter<E, BaseAdapter.BaseViewHolder>(object : DiffUtil.ItemCallback<E>() {
    override fun areItemsTheSame(oldItem: E, newItem: E) = areItemsSame(oldItem, newItem)
    override fun areContentsTheSame(oldItem: E, newItem: E) = areContentsSame(oldItem, newItem)
}) {

    var onItemClick: (E) -> Unit = {}

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding: B =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        return BaseViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: B? = DataBindingUtil.getBinding(holder.itemView)
        val pos = holder.absoluteAdapterPosition
        val data = currentList[pos]
        if (binding != null && data != null) {
            onBind(binding, data)
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = currentList.size

    abstract fun onBind(binding: B, item: E)
}