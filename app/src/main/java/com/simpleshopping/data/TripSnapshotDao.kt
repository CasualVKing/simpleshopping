package com.simpleshopping.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TripSnapshotDao {
    @Query("SELECT * FROM trip_snapshot")
    suspend fun getAll(): List<TripSnapshot>

    @Insert
    suspend fun insertAll(snapshots: List<TripSnapshot>)

    @Query("DELETE FROM trip_snapshot")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) > 0 FROM trip_snapshot")
    suspend fun hasSnapshot(): Boolean
}
