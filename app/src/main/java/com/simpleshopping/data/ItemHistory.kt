package com.simpleshopping.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_history",
    foreignKeys = [
        ForeignKey(
            entity = Section::class,
            parentColumns = ["id"],
            childColumns = ["section_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("section_id"), Index(value = ["name", "section_id"], unique = true)]
)
data class ItemHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "section_id")
    val sectionId: Long,
    @ColumnInfo(name = "usage_count")
    val usageCount: Int = 1,
    @ColumnInfo(name = "last_check_position")
    val lastCheckPosition: Int? = null
)
