package com.baymax104.bookmanager20.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baymax104.bookmanager20.BR
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 12:19
 *@Version 1
 */
@Entity
class History(book: Book) : BaseObservable() {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    var bookId = book.id

    @get:Bindable
    var start = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.start)
        }

    @get:Bindable
    var end = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.end)
        }

    @get:Bindable
    var updateTime: Date? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.updateTime)
        }

    var total = book.page

    constructor() : this(Book())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as History

        if (id != other.id) return false
        if (bookId != other.bookId) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (updateTime != other.updateTime) return false
        if (total != other.total) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + bookId
        result = 31 * result + start
        result = 31 * result + end
        result = 31 * result + (updateTime?.hashCode() ?: 0)
        result = 31 * result + total
        return result
    }


}

