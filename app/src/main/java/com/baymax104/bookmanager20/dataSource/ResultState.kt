package com.baymax104.bookmanager20.dataSource

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 19:28
 *@Version 1
 */
sealed class ResultState<out T>

data class Success<out T>(val data: T) : ResultState<T>()

data class FAIL(
    val message: String?,
    val error: Exception?
) : ResultState<Nothing>()