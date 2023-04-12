package com.baymax104.bookmanager20.util

import androidx.core.text.isDigitsOnly
import androidx.room.TypeConverter
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.text.SimpleDateFormat
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 19:38
 *@Version 1
 */
val DateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

val DateDetailFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)

object PageDeserializer : JsonDeserializer<Int>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Int {
        if (p == null) return 0
        val value = p.codec.readValue(p, String::class.java)
        return when {
            value == "" -> 0
            value.isDigitsOnly() -> value.toInt()
            else -> value.substring(0..value.length - 2).toInt()
        }
    }
}

class RoomConverter {

    @TypeConverter
    fun convertDateToString(date: Date?): String? =
        date?.let { DateFormatter.format(it) }

    @TypeConverter
    fun convertStringToDate(dateString: String?): Date? =
        dateString?.let { DateFormatter.parse(it) }
}

interface LightClone<T> {
    fun clone(): T
}

fun <T : LightClone<T>> List<T>.lightClone(): List<T> {
    val copyList = mutableListOf<T>()
    forEach {
        copyList.add(it.clone())
    }
    return copyList
}