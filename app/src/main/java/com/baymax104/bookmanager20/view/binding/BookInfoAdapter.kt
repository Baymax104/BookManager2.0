package com.baymax104.bookmanager20.view.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.toDateString
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
    fun TextView.finishTime(book: Book) {
        val start = book.startTime.toDateString()
        val end = book.endTime.toDateString()
        text = "$start——$end"
    }

    @JvmStatic
    @BindingAdapter("book_type")
    fun ImageView.bookType(book: Book) {
        val resource = if (book.progress >= 100) R.drawable.restart else R.drawable.update
        setImageResource(resource)
    }

}
