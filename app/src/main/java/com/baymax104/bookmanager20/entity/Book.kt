package com.baymax104.bookmanager20.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
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
@JsonIgnoreProperties(ignoreUnknown = true)
class Book(builder: Builder) : BaseObservable() {

    @JsonIgnore
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

        fun copy(book: Book?) = Builder().apply {
            book?.let {
                id = it.id
                name = it.name
                page = it.page
                author = it.author
                progress = it.progress
                startTime = it.startTime
                endTime = it.endTime
                photo = it.photo
                publisher = it.publisher
                isbn = it.isbn
                description = it.description
            }
        }.build()
    }

    class Builder {
        var id = -1
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

    override fun toString(): String {
        return "Book(id=$id, name='$name', page=$page, author=$author, progress=$progress, startTime=$startTime, endTime=$endTime, photo=$photo, publisher=$publisher, description=$description, isbn=$isbn)"
    }
}