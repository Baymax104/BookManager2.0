package com.baymax104.bookmanager20.util

import android.annotation.SuppressLint
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.baymax104.bookmanager20.adapter.BaseAdapter
import com.baymax104.bookmanager20.adapter.FragmentAdapter
import com.baymax104.bookmanager20.entity.Book
import java.text.SimpleDateFormat
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 23:41
 *@Version 1
 */
object BindingUtil {

    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

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
        val start = book.startTime?.let { dateFormatter.format(it) }
        val end = book.endTime?.let { dateFormatter.format(it) }
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
        setProgress(progress, true)
    }

    @JvmStatic
    @BindingConversion
    fun convertDateToString(date: Date?): String? = date?.let { dateFormatter.format(it) }

}