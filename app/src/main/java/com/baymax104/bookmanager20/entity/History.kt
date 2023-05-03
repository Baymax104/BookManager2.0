package com.baymax104.bookmanager20.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.architecture.interfaces.Clone
import com.baymax104.bookmanager20.architecture.interfaces.ObjectRange
import com.baymax104.bookmanager20.util.toDateString
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 12:19
 *@Version 1
 */
@Entity
class History(
    book: Book
) : BaseObservable(), Clone<History>, ObjectRange<Int> {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    var bookId = book.id

    @get:Bindable
    var updateTime = Date()
        set(value) {
            field = value
            notifyPropertyChanged(BR.updateTime)
        }

    @Ignore
    override var start = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.type)
        }

    @Ignore
    override var end = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.type)
        }

    var total = book.page
        set(value) {
            field = value
            notifyPropertyChanged(BR.type)
        }

    var duplicate = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.type)
        }

    @get:Ignore
    @get:Bindable
    val type: Type
        get() = when {
            start == 0 && end == 0 -> Start
            duplicate -> Duplicate(start, end, total)
            else -> Process(start, end, total)
        }

    @ColumnInfo(name = "start")
    var startDB = 0
        get() = start
        set(value) {
            field = value
            start = value
        }

    @ColumnInfo(name = "end")
    var endDB = 0
        get() = end
        set(value) {
            field = value
            end = value
        }

    constructor() : this(Book())

    sealed class Type
    object Start : Type()
    data class Process(var start: Int, val end: Int, val total: Int) : Type()
    data class Duplicate(var start: Int, val end: Int, val total: Int) : Type()

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
        result = 31 * result + updateTime.hashCode()
        result = 31 * result + total
        return result
    }


    override fun clone(): History {
        return History().apply {
            id = this@History.id
            bookId = this@History.bookId
            updateTime = this@History.updateTime
            start = this@History.start
            end = this@History.end
            total = this@History.total
            duplicate = this@History.duplicate
        }
    }

    override fun toString(): String {
        return "History(id=$id, bookId=$bookId, start=$start, end=$end, updateTime=${updateTime.toDateString()}, total=$total, duplicate=$duplicate)"
    }
}

