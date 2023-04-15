package com.baymax104.bookmanager20.view.binding

import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.DateFormatter
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/4 16:19
 *@Version 1
 */
@Suppress("unchecked_cast", "NotifyDataSetChanged", "SetTextI18n")
object BookInfoAdapter {
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

}
