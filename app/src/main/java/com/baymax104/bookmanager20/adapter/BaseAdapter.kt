package com.baymax104.bookmanager20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.bookmanager20.util.LiveList

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 20:30
 *@Version 1
 */
@Suppress("NotifyDataSetChanged")
abstract class BaseAdapter<E, B : ViewDataBinding>(
    @LayoutRes
    val layoutId: Int
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private var source: List<E>? = null

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
        onBind(binding, source?.get(pos))
    }

    override fun getItemCount(): Int = source?.size ?: 0

    fun registerSource(list: LiveList<E>) {
        list.observe {
            whole {
                // 赋值引用，与原数据修改同步
                source = it
                notifyDataSetChanged()
            }
            add { index, _ -> notifyItemInserted(index) }
        }
    }

    abstract fun onBind(binding: B?, item: E?)
}