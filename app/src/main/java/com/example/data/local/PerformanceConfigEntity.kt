package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "performance_config")
data class PerformanceConfigEntity(
    @PrimaryKey val id: Int = 1,
    val preset: String = "BALANCED", // POTATO, BALANCED, ULTRA, CUSTOM
    val allocatedRamMb: Int = 2560,
    val renderer: String = "Holy GL4ES 1.1.5", // Holy GL4ES 1.1.5, VirGL Mesa 3D, Zink (Vulkan), ANGLE
    val javaVersion: String = "Java 21 (LTS Modern)", // Java 21, Java 17, Java 8
    val resolutionScalePercent: Int = 85, // 50 to 100
    val maxFps: Int = 60,
    val enableVsync: Boolean = true,
    val customJvmArgs: String = "-XX:+UseG1GC -XX:MaxGCPauseMillis=200",
    val touchControlOpacity: Float = 0.85f,
    val touchControlScale: Float = 1.0f
)
