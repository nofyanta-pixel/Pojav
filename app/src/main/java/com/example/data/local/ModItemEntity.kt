package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mods")
data class ModItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val modpackId: Long,
    val name: String,
    val fileName: String,
    val version: String,
    val category: String, // "Performance", "Optimization", "Graphics", "Utility", "Content"
    val description: String,
    val isEnabled: Boolean = true
)
