package com.baymax104.bookmanager20.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.baymax104.bookmanager20.entity.Book
import com.baymax104.bookmanager20.util.RoomConverter

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/24 19:16
 *@Version 1
 */
@TypeConverters(RoomConverter::class)
@Database(entities = [Book::class], version = 2)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}

/**
 * 版本1升级到版本2，Book表添加了tableRank字段，用于排序
 */
val Migrate1_2 = Migration(1, 2) {
    it.execSQL("alter table Book add column tableRank INTEGER not null default 0")
    val cursor = it.query("select * from Book")
    var rank = 1
    while (cursor.moveToNext()) {
        val id = cursor.getInt(0)
        it.execSQL(
            "update Book " +
                    "set tableRank = $rank " +
                    "where id = $id"
        )
        rank++
    }
    cursor.close()
}
