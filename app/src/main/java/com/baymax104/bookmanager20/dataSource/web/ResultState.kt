package com.baymax104.bookmanager20.dataSource.web

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 19:28
 *@Version 1
 */
sealed class ResultState<out T>

data class WebSuccess<out T>(val data: T) : ResultState<T>()

data class WebError(
    val message: String,
) : ResultState<Nothing>()