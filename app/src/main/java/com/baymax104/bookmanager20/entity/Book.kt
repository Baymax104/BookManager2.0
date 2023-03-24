package com.baymax104.bookmanager20.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.baymax104.bookmanager20.BR
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
data class Book(
    @JsonIgnore
    @Ignore
    val builder: Builder
) : BaseObservable() {

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

    @JsonCreator
    constructor() : this(Builder())

    companion object {
        inline fun build(block: Builder.() -> Unit = {}) = Builder().apply(block).build()

        /**
         * 使用book.copy()复制，若book==null，则使用默认Builder构造
         */
        fun copyNotNull(book: Book?) = book?.copy() ?: Builder().build()
    }

    class Builder {
        var id = 0
        var name = ""
        var page = 0
        var author: String? = null
        var progress = 0
        var startTime: Date? = null
        var endTime: Date? = null
        var photo: String? = null
        var publisher: String? = null
        var isbn: String? = null
        var description: String? = null
        fun build() = Book(this)
    }
}