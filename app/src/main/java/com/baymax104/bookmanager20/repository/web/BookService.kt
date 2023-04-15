package com.baymax104.bookmanager20.repository.web

import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.entity.ResponseResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 13:35
 *@Version 1
 */
interface BookService {

    @GET("{isbn}")
    suspend fun requestBookInfo(@Path("isbn") isbn: String): ResponseResult<Book>

    @GET("https://api.ibook.tech/v1/book/isbn")
    suspend fun requestBook(@Query("isbn") isbn: String): ResponseResult<Book>
}