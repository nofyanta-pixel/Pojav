package com.example.data.model

import androidx.compose.ui.graphics.Color

data class CapePreset(
    val id: String,
    val name: String,
    val description: String,
    val baseColor: Color,
    val emblemColor: Color,
    val borderCol: Color,
    val emblemType: String // "STAR", "CREEPER", "PICKAXE", "PISTON", "GOLEM", "ENDERMAN", "VANILLA", "OPTIFINE", "CHERRY", "NONE"
)

object CapePresets {
    val allPresets = listOf(
        CapePreset(
            id = "migrator",
            name = "Migrator Cape",
            description = "Awarded to players migrating from Mojang accounts to Microsoft accounts.",
            baseColor = Color(0xFF8B1E1E),
            emblemColor = Color(0xFFD4AF37),
            borderCol = Color(0xFF5A1414),
            emblemType = "STAR"
        ),
        CapePreset(
            id = "minecon_2011",
            name = "Minecon 2011 (Creeper)",
            description = "Exclusive Cape from the first official MINECON in Las Vegas.",
            baseColor = Color(0xFF8B0000),
            emblemColor = Color(0xFF1B1B1B),
            borderCol = Color(0xFF4A0000),
            emblemType = "CREEPER"
        ),
        CapePreset(
            id = "minecon_2012",
            name = "Minecon 2012 (Pickaxe)",
            description = "Disneyland Paris Minecon attendee commemorative cape.",
            baseColor = Color(0xFF1E3A8A),
            emblemColor = Color(0xFFE2E8F0),
            borderCol = Color(0xFF172554),
            emblemType = "PICKAXE"
        ),
        CapePreset(
            id = "minecon_2013",
            name = "Minecon 2013 (Piston)",
            description = "Orlando Minecon featuring the famous redstone piston graphic.",
            baseColor = Color(0xFF14532D),
            emblemColor = Color(0xFFCA8A04),
            borderCol = Color(0xFF052E16),
            emblemType = "PISTON"
        ),
        CapePreset(
            id = "minecon_2015",
            name = "Minecon 2015 (Iron Golem)",
            description = "London Minecon featuring the benevolent Iron Golem face.",
            baseColor = Color(0xFF0284C7),
            emblemColor = Color(0xFFE2E8F0),
            borderCol = Color(0xFF0369A1),
            emblemType = "GOLEM"
        ),
        CapePreset(
            id = "minecon_2016",
            name = "Minecon 2016 (Enderman)",
            description = "Anaheim Minecon cape displaying menacing purple Enderman gaze.",
            baseColor = Color(0xFF1E1B4B),
            emblemColor = Color(0xFFA855F7),
            borderCol = Color(0xFF0F0E2A),
            emblemType = "ENDERMAN"
        ),
        CapePreset(
            id = "vanilla",
            name = "Vanilla Cape",
            description = "Dual Bedrock & Java celebration cape with gradient split.",
            baseColor = Color(0xFF7C3AED),
            emblemColor = Color(0xFFF59E0B),
            borderCol = Color(0xFF4C1D95),
            emblemType = "VANILLA"
        ),
        CapePreset(
            id = "optifine",
            name = "OptiFine Cape",
            description = "Legendary custom donor cape with classic bold OF lettering.",
            baseColor = Color(0xFF991B1B),
            emblemColor = Color(0xFFFFFFFF),
            borderCol = Color(0xFF7F1D1D),
            emblemType = "OPTIFINE"
        ),
        CapePreset(
            id = "cherry",
            name = "Cherry Blossom Cape",
            description = "15th Anniversary celebration cape inspired by cherry groves.",
            baseColor = Color(0xFFF472B6),
            emblemColor = Color(0xFFFFFFFF),
            borderCol = Color(0xFFDB2777),
            emblemType = "CHERRY"
        ),
        CapePreset(
            id = "none",
            name = "No Cape",
            description = "Clean look without cape equipped.",
            baseColor = Color.Transparent,
            emblemColor = Color.Transparent,
            borderCol = Color.Transparent,
            emblemType = "NONE"
        )
    )

    fun getById(id: String): CapePreset {
        return allPresets.find { it.id == id } ?: allPresets.first()
    }
}
