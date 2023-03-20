package com.baymax104.bookmanager20.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.baymax104.bookmanager20.BR
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
class Book private constructor(builder: Builder) : BaseObservable() {

    var id = builder.id

    @get:Bindable
    var name = builder.name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var page = builder.page
        set(value) {
            field = value
            notifyPropertyChanged(BR.page)
        }

    @get:Bindable
    var author = builder.author
        set(value) {
            field = value
            notifyPropertyChanged(BR.author)
        }

    @get:Bindable
    var progress = builder.progress  // 已看页数
        set(value) {
            field = value
            notifyPropertyChanged(BR.progress)
        }

    @get:Bindable
    var startTime = builder.startTime
        set(value) {
            field = value
            notifyPropertyChanged(BR.startTime)
        }

    @get:Bindable
    var endTime = builder.endTime
        set(value) {
            field = value
            notifyPropertyChanged(BR.endTime)
        }

    companion object {
        inline fun build(block: Builder.() -> Unit = {}) = Builder().apply(block).build()
    }

    class Builder {
        var id = -1
        var name = ""
        var page = 0
        var author: String? = null
        var progress = 0
        var startTime: Date? = null
        var endTime: Date? = null
        fun build() = Book(this)
    }
}