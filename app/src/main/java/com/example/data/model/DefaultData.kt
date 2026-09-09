package com.example.data.model

import com.example.data.local.AccountEntity
import com.example.data.local.ModItemEntity
import com.example.data.local.ModpackEntity
import com.example.data.local.PerformanceConfigEntity
import java.util.UUID

object DefaultData {
    val initialAccount = AccountEntity(
        id = 1,
        username = "PojavPlayer",
        accountType = "OFFLINE",
        uuid = UUID.randomUUID().toString().substring(0, 18),
        skinModel = "STEVE",
        skinPresetId = "steve",
        capePresetId = "migrator",
        isActive = true
    )

    val initialPerformanceConfig = PerformanceConfigEntity(
        id = 1,
        preset = "BALANCED",
        allocatedRamMb = 2560,
        renderer = "Holy GL4ES 1.1.5",
        javaVersion = "Java 21 (LTS Modern)",
        resolutionScalePercent = 85,
        maxFps = 60,
        enableVsync = true,
        customJvmArgs = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200",
        touchControlOpacity = 0.85f,
        touchControlScale = 1.0f
    )

    val initialModpacks = listOf(
        ModpackEntity(
            id = 1,
            name = "Fabulously Optimized",
            mcVersion = "1.20.4",
            loader = "Fabric",
            loaderVersion = "0.15.11",
            description = "The definitive FPS & performance boost modpack for PojavLauncher Android. High FPS & smooth frame pacing.",
            iconTag = "bolt",
            isSelected = true,
            memoryMb = 2560
        ),
        ModpackEntity(
            id = 2,
            name = "Simply Optimized (1.21)",
            mcVersion = "1.21",
            loader = "Fabric",
            loaderVersion = "0.16.0",
            description = "Ultra lightweight configuration designed for maximum battery saving & lowest frame drops.",
            iconTag = "leaf",
            isSelected = false,
            memoryMb = 2048
        ),
        ModpackEntity(
            id = 3,
            name = "Forge TechCraft",
            mcVersion = "1.20.1",
            loader = "Forge",
            loaderVersion = "47.2.20",
            description = "Performance-tuned Forge profile with Embeddium, ModernFix, and JEI for heavy mods.",
            iconTag = "cube",
            isSelected = false,
            memoryMb = 3072
        ),
        ModpackEntity(
            id = 4,
            name = "Vanilla Java 1.20.4",
            mcVersion = "1.20.4",
            loader = "Vanilla",
            loaderVersion = "Release",
            description = "Clean official Minecraft Java client without mods.",
            iconTag = "star",
            isSelected = false,
            memoryMb = 1536
        )
    )

    fun getInitialMods(modpackId: Long): List<ModItemEntity> {
        return when (modpackId) {
            1L -> listOf(
                ModItemEntity(
                    modpackId = 1L,
                    name = "Sodium",
                    fileName = "sodium-fabric-0.5.8+mc1.20.4.jar",
                    version = "0.5.8",
                    category = "Performance",
                    description = "Modern OpenGL rendering engine dramatically replacing vanilla renderer for 200-400% FPS boost.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "Lithium",
                    fileName = "lithium-fabric-0.12.1.jar",
                    version = "0.12.1",
                    category = "Performance",
                    description = "General-purpose physics, mob AI, and chunk ticking optimization engine.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "Iris Shaders",
                    fileName = "iris-fabric-1.6.17.jar",
                    version = "1.6.17",
                    category = "Graphics",
                    description = "Modern shaderpack loader compatible with OptiFine shaders (BSL, Complementary).",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "FerriteCore",
                    fileName = "ferritecore-6.0.3-fabric.jar",
                    version = "6.0.3",
                    category = "Memory",
                    description = "Memory usage reducer that cuts Minecraft RAM footprint by up to 40%.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "ImmediatelyFast",
                    fileName = "immediatelyfast-fabric-1.2.11.jar",
                    version = "1.2.11",
                    category = "Performance",
                    description = "Optimizes immediate mode rendering of GUI, HUD, text and entity models.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "Entity Culling",
                    fileName = "entityculling-fabric-1.6.2.jar",
                    version = "1.6.2",
                    category = "Performance",
                    description = "Skips rendering entities behind solid walls and underground caves.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "ModernFix",
                    fileName = "modernfix-fabric-5.14.0.jar",
                    version = "5.14.0",
                    category = "Optimization",
                    description = "All-in-one fix for memory leaks, slow world loading, and stuttering.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 1L,
                    name = "Dynamic FPS",
                    fileName = "dynamic-fps-3.4.3.jar",
                    version = "3.4.3",
                    category = "Utility",
                    description = "Reduces resource and battery consumption when Pojav is in background.",
                    isEnabled = true
                )
            )
            2L -> listOf(
                ModItemEntity(
                    modpackId = 2L,
                    name = "Sodium 1.21",
                    fileName = "sodium-fabric-0.6.0.jar",
                    version = "0.6.0",
                    category = "Performance",
                    description = "Core graphics optimization engine.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 2L,
                    name = "Lithium 1.21",
                    fileName = "lithium-fabric-0.13.0.jar",
                    version = "0.13.0",
                    category = "Performance",
                    description = "Server tick and entity calculation optimizer.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 2L,
                    name = "Krypton",
                    fileName = "krypton-0.2.3.jar",
                    version = "0.2.3",
                    category = "Network",
                    description = "Optimizes network packet compression for smoother multiplayer.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 2L,
                    name = "ModernFix",
                    fileName = "modernfix-5.18.jar",
                    version = "5.18",
                    category = "Memory",
                    description = "Prevents stuttering and RAM spikes.",
                    isEnabled = true
                )
            )
            3L -> listOf(
                ModItemEntity(
                    modpackId = 3L,
                    name = "Embeddium",
                    fileName = "embeddium-0.3.18+mc1.20.1.jar",
                    version = "0.3.18",
                    category = "Performance",
                    description = "Unofficial Forge port of Sodium with broad mod compatibility.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 3L,
                    name = "FerriteCore (Forge)",
                    fileName = "ferritecore-6.0.1-forge.jar",
                    version = "6.0.1",
                    category = "Memory",
                    description = "RAM usage reduction for heavy Forge modded gameplay.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 3L,
                    name = "ModernFix (Forge)",
                    fileName = "modernfix-forge-5.14.jar",
                    version = "5.14",
                    category = "Optimization",
                    description = "Forge memory leak and launch-speed accelerator.",
                    isEnabled = true
                ),
                ModItemEntity(
                    modpackId = 3L,
                    name = "Just Enough Items (JEI)",
                    fileName = "jei-1.20.1-forge-15.3.0.jar",
                    version = "15.3.0",
                    category = "Utility",
                    description = "In-game recipe and item viewer.",
                    isEnabled = true
                )
            )
            else -> emptyList()
        }
    }
}
