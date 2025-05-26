package com.example.ocr_cam

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanDao {
    @Insert
    suspend fun insert(scan: Scan)

    @Query("SELECT * FROM scans ORDER BY timestamp DESC")
    suspend fun getAll(): List<Scan>

    @Query("SELECT * FROM scans WHERE id = :id")
    suspend fun getById(id: Int): Scan?
}

