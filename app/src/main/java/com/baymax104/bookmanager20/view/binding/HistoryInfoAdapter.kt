package com.baymax104.bookmanager20.view.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.entity.History

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/14 12:21
 *@Version 1
 */
object HistoryInfoAdapter {

    @JvmStatic
    @BindingAdapter("history_page_start", "history_page_end", "history_page_total")
    fun TextView.historyPage(start: Int, end: Int, total: Int) {
        text = when {
            start == 0 && end == 1 -> "开始阅读"
            end == total -> "完成阅读"
            else -> "读完第${start}~${end}页"
        }
    }

    @JvmStatic
    @BindingAdapter("history_time_dot")
    fun ImageView.historyTimeDot(history: History) {
        when {
            history.start == 0 && history.end == 1 -> R.drawable.time_dot_start
            history.end == history.total -> R.drawable.time_dot_end
            else -> R.drawable.time_dot
        }.let { setImageResource(it) }
    }

}