package com.baymax104.bookmanager20.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.baymax104.bookmanager20.adapter.BaseAdapter
import com.baymax104.bookmanager20.adapter.FragmentAdapter
import com.baymax104.bookmanager20.entity.Book
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.text.SimpleDateFormat
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 23:41
 *@Version 1
 */
typealias MData<T> = MutableLiveData<T>
typealias MList<T> = MutableLiveData<MutableList<T>>

val DateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

val DateDetailFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("fragment_adapter", "fragments")
    fun ViewPager2.setFragmentAdapter(adapter: FragmentAdapter, fragments: List<Fragment>) {
        adapter.fragments = fragments
        this.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("recycler_adapter", "recycler_data")
    fun <T> RecyclerView.setRecyclerData(adapter: BaseAdapter<T>, data: MutableList<T>) {
        adapter.data = data
        this.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("book_finish_time")
    fun TextView.setFinishTime(book: Book) {
        val start = book.startTime?.let { DateFormatter.format(it) }
        val end = book.endTime?.let { DateFormatter.format(it) }
        text = "$start——$end"
    }

    @JvmStatic
    @BindingAdapter("book_progress")
    fun ProgressBar.setBookProgress(book: Book) {
        val progress = if (book.progress >= book.page) {
            100
        } else {
            (book.progress * 1.0 / (if (book.page == 0) 1 else book.page) * 100).toInt()
        }
        setProgress(progress)
    }

    @JvmStatic
    @BindingAdapter("book_photo", "book_photo_scaleType", requireAll = false)
    fun ImageView.setBookPhoto(uriPath: String?, scaleType: ScaleType?) {
        val scale = when (scaleType) {
            ScaleType.FIT_CENTER -> FitCenter()
            ScaleType.CENTER_INSIDE -> CenterInside()
            else -> CenterCrop()
        }
        val options = RequestOptions()
            .skipMemoryCache(true)
            .transform(scale, RoundedCorners(ConvertUtils.dp2px(10f)))
        Glide.with(this)
            .asBitmap()
            .load(uriPath)
            .apply(options)
            .into(this)
    }

    @JvmStatic
    @BindingConversion
    fun convertDateToString(date: Date?): String? = date?.let { DateFormatter.format(it) }

}

object PageDeserializer : JsonDeserializer<Int>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Int {
        if (p == null) return 0
        val value = p.codec.readValue(p, String::class.java)
        return when {
            value == "" -> 0
            value.isDigitsOnly() -> value.toInt()
            else -> value.substring(0..value.length - 2).toInt()
        }
    }
}