package com.simpleshopping.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {
    @Query("SELECT * FROM sections ORDER BY sort_order ASC")
    fun getAllSections(): Flow<List<Section>>

    @Query("SELECT * FROM sections ORDER BY sort_order ASC")
    suspend fun getAllSectionsList(): List<Section>

    @Insert
    suspend fun insert(section: Section): Long

    @Update
    suspend fun update(section: Section)

    @Delete
    suspend fun delete(section: Section)

    @Query("SELECT COALESCE(MAX(sort_order), -1) + 1 FROM sections")
    suspend fun getNextSortOrder(): Int

    @Query("UPDATE sections SET sort_order = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)
}
