package com.example.ocr_cam

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class Scan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val ocrText: String,
    val timestamp: Long = System.currentTimeMillis()
)

