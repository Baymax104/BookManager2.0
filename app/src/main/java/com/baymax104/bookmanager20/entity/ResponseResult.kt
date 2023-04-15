package com.baymax104.bookmanager20.entity

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/22 13:32
 *@Version 1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseResult<T> @JsonCreator constructor(
    @JsonAlias("errcode", "ret")
    @JsonProperty("code")
    val code: Int,

    @JsonAlias("errmsg", "msg")
    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: T
)
