package com.baymax104.bookmanager20.view.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.entity.History
import com.baymax104.bookmanager20.util.DateFormatter
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/14 12:21
 *@Version 1
 */
@Suppress("SetTextI18n")
object HistoryInfoAdapter {

    @JvmStatic
    @BindingAdapter("history_type")
    fun TextView.historyPage(type: History.Type) {
        text = when (type) {
            is History.Start -> "开始阅读"
            is History.Process -> "读完第${type.start}~${type.end}页"
        }
    }

    @JvmStatic
    @BindingAdapter("history_time")
    fun TextView.historyTime(date: Date?) {
        val d = date?.let { DateFormatter.format(it) }
        text = "更新时间：$d"
    }

    @JvmStatic
    @BindingAdapter("history_time_dot")
    fun ImageView.historyTimeDot(history: History) {
        when (history.type) {
            is History.Start -> R.drawable.time_dot_start
            is History.Process -> R.drawable.time_dot
        }.let { setImageResource(it) }
    }

}