package com.baymax104.bookmanager20.view.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.util.toDateString
import com.baymax104.bookmanager20.view.adapter.BaseAdapter
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.drake.statelayout.StateLayout
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/7 23:18
 *@Version 1
 */
@Suppress("Unchecked_Cast")
object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("recycler_data")
    fun <E> RecyclerView.recyclerData(data: List<E>) {
        // 对同一列表是否通过submitList更新视图，由各个adapter按需进行重写
        (adapter!! as ListAdapter<E, BaseAdapter.BaseViewHolder>).submitList(data)
    }

    @JvmStatic
    @BindingAdapter("state_hasContent")
    fun <E> StateLayout.stateChange(data: List<E>) {
        if (data.isNotEmpty()) {
            showContent()
        } else {
            showEmpty()
        }
    }

    @JvmStatic
    @BindingAdapter("img", "img_scaleType", "img_default", requireAll = false)
    fun ImageView.img(
        uriPath: String?,
        scaleType: ImageView.ScaleType?,
        default: Boolean
    ) {
        val scale = when (scaleType) {
            ImageView.ScaleType.FIT_CENTER -> FitCenter()
            ImageView.ScaleType.CENTER_INSIDE -> CenterInside()
            else -> CenterCrop()
        }
        var options = RequestOptions()
            .skipMemoryCache(true)
            .transform(scale, RoundedCorners(ConvertUtils.dp2px(10f)))
        if (default) {
            options = options.placeholder(R.drawable.no_cover)
        }
        Glide.with(this)
            .asBitmap()
            .load(uriPath)
            .apply(options)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("text_date")
    fun TextView.textDate(date: Date?) {
        text = date.toDateString()
    }

}