package com.baymax104.bookmanager20.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.util.LightClone
import com.baymax104.bookmanager20.util.PageDeserializer
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/19 11:53
 *@Version 1
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Book(builder: Builder) : BaseObservable(), LightClone<Book> {

    @JsonIgnore
    @PrimaryKey(autoGenerate = true)
    var id = builder.id

    @JsonAlias("title", "name")
    @get:Bindable
    var name = builder.name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @JsonDeserialize(using = PageDeserializer::class)
    @JsonAlias("page", "pages")
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

    @JsonAlias("photoUrl", "img")
    @get:Bindable
    var photo = builder.photo
        set(value) {
            field = value
            notifyPropertyChanged(BR.photo)
        }

    @JsonAlias("publisher", "publishing")
    @get:Bindable
    var publisher = builder.publisher
        set(value) {
            field = value
            notifyPropertyChanged(BR.publisher)
        }

    @JsonAlias("description", "summary")
    @get:Bindable
    var description = builder.description
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @JsonAlias("isbn", "code")
    @get:Bindable
    var isbn = builder.isbn
        set(value) {
            field = value
            notifyPropertyChanged(BR.isbn)
        }

    @JsonIgnore
    var tableRank = builder.tableRank

    @JsonCreator
    constructor() : this(Builder())

    companion object {
        inline fun build(block: Builder.() -> Unit = {}) = Builder().apply(block).build()
    }

    class Builder {
        var id = 0
        var name: String? = null
        var page = 0
        var author: String? = null
        var progress = 0
        var startTime: Date? = null
        var endTime: Date? = null
        var photo: String? = null
        var publisher: String? = null
        var isbn: String? = null
        var description: String? = null
        var tableRank = -1
        fun build() = Book(this)
    }

    override fun toString(): String {
        return "Book(name='$name', page=$page, author=$author, progress=$progress, startTime=$startTime, endTime=$endTime, photo=$photo, publisher=$publisher, description=$description, isbn=$isbn, tableRank=$tableRank)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book
        if (id != other.id) return false
        if (name != other.name) return false
        if (page != other.page) return false
        if (author != other.author) return false
        if (progress != other.progress) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (photo != other.photo) return false
        if (isbn != other.isbn) return false
        if (tableRank != other.tableRank) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + page
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + progress
        result = 31 * result + (startTime?.hashCode() ?: 0)
        result = 31 * result + (endTime?.hashCode() ?: 0)
        result = 31 * result + (photo?.hashCode() ?: 0)
        result = 31 * result + (isbn?.hashCode() ?: 0)
        result = 31 * result + tableRank
        return result
    }

    override fun clone(): Book {
        return build {
            id = this@Book.id
            name = this@Book.name
            page = this@Book.page
            author = this@Book.author
            progress = this@Book.progress
            startTime = this@Book.startTime
            endTime = this@Book.endTime
            photo = this@Book.photo
            publisher = this@Book.publisher
            isbn = this@Book.isbn
            description = this@Book.description
            tableRank = this@Book.tableRank
        }
    }
}