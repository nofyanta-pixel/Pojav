package com.example.data.model

import androidx.compose.ui.graphics.Color

data class SkinPreset(
    val id: String,
    val name: String,
    val model: String, // "STEVE" or "ALEX"
    val description: String,
    val headSkinColor: Color,
    val hairColor: Color,
    val eyeColor: Color,
    val torsoColor: Color,
    val armColor: Color,
    val legColor: Color,
    val accentColor: Color
)

object SkinPresets {
    val allPresets = listOf(
        SkinPreset(
            id = "steve",
            name = "Steve (Classic)",
            model = "STEVE",
            description = "The timeless Minecraft default character with cyan tee & jeans.",
            headSkinColor = Color(0xFFC48B69),
            hairColor = Color(0xFF4A3222),
            eyeColor = Color(0xFF2C4A75),
            torsoColor = Color(0xFF00A2A2),
            armColor = Color(0xFFC48B69),
            legColor = Color(0xFF233682),
            accentColor = Color(0xFF008080)
        ),
        SkinPreset(
            id = "alex",
            name = "Alex (Slim)",
            model = "ALEX",
            description = "The classic survivalist with orange hair & green tunic.",
            headSkinColor = Color(0xFFE2AE8B),
            hairColor = Color(0xFFB54C1E),
            eyeColor = Color(0xFF336C38),
            torsoColor = Color(0xFF5A783A),
            armColor = Color(0xFFE2AE8B),
            legColor = Color(0xFF42372B),
            accentColor = Color(0xFF6F9447)
        ),
        SkinPreset(
            id = "cyber_knight",
            name = "Cyber Knight",
            model = "STEVE",
            description = "Futuristic tech samurai coated in high-tier carbon alloy.",
            headSkinColor = Color(0xFF1E293B),
            hairColor = Color(0xFF0F172A),
            eyeColor = Color(0xFF38BDF8),
            torsoColor = Color(0xFF0F172A),
            armColor = Color(0xFF334155),
            legColor = Color(0xFF1E293B),
            accentColor = Color(0xFF00E5FF)
        ),
        SkinPreset(
            id = "ender_mage",
            name = "Ender Mage",
            model = "STEVE",
            description = "Void traveler empowered by Dragon breath and End runes.",
            headSkinColor = Color(0xFF171026),
            hairColor = Color(0xFF0D061A),
            eyeColor = Color(0xFFD946EF),
            torsoColor = Color(0xFF25163D),
            armColor = Color(0xFF361C5B),
            legColor = Color(0xFF171026),
            accentColor = Color(0xFFA855F7)
        ),
        SkinPreset(
            id = "netherite_slayer",
            name = "Netherite Slayer",
            model = "STEVE",
            description = "Forged in ancient debris with burning blaze accents.",
            headSkinColor = Color(0xFF312E35),
            hairColor = Color(0xFF1E1B21),
            eyeColor = Color(0xFFFF5722),
            torsoColor = Color(0xFF433F49),
            armColor = Color(0xFF312E35),
            legColor = Color(0xFF222026),
            accentColor = Color(0xFFFF6D00)
        ),
        SkinPreset(
            id = "hacker",
            name = "Zero-Day Hacker",
            model = "STEVE",
            description = "Ghost operative with neon green terminal code hoodie.",
            headSkinColor = Color(0xFF18181B),
            hairColor = Color(0xFF09090B),
            eyeColor = Color(0xFF22C55E),
            torsoColor = Color(0xFF18181B),
            armColor = Color(0xFF27272A),
            legColor = Color(0xFF09090B),
            accentColor = Color(0xFF4ADE80)
        ),
        SkinPreset(
            id = "anime_hero",
            name = "Crimson Shogun",
            model = "ALEX",
            description = "Battle-ready warrior draped in flowing royal crimson robes.",
            headSkinColor = Color(0xFFF3C5A8),
            hairColor = Color(0xFFE2E8F0),
            eyeColor = Color(0xFFDC2626),
            torsoColor = Color(0xFFB91C1C),
            armColor = Color(0xFF991B1B),
            legColor = Color(0xFF18181B),
            accentColor = Color(0xFFFACC15)
        ),
        SkinPreset(
            id = "panda",
            name = "Pixel Panda",
            model = "ALEX",
            description = "Comfy black and white panda onesie with bamboo green scarf.",
            headSkinColor = Color(0xFFF4F4F5),
            hairColor = Color(0xFF18181B),
            eyeColor = Color(0xFF16A34A),
            torsoColor = Color(0xFFFAFAFA),
            armColor = Color(0xFF27272A),
            legColor = Color(0xFF18181B),
            accentColor = Color(0xFF22C55E)
        )
    )

    fun getById(id: String): SkinPreset {
        return allPresets.find { it.id == id } ?: allPresets.first()
    }
}
