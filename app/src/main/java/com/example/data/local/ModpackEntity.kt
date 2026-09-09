package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modpacks")
data class ModpackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mcVersion: String, // e.g. "1.20.4", "1.21", "1.16.5", "1.12.2"
    val loader: String, // "Fabric", "Forge", "NeoForge", "Quilt", "Vanilla"
    val loaderVersion: String,
    val description: String,
    val iconTag: String, // "performance", "sword", "bolt", "cube", "star"
    val isSelected: Boolean = false,
    val jvmArgs: String = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled",
    val memoryMb: Int = 2560,
    val lastPlayed: Long = System.currentTimeMillis()
)
