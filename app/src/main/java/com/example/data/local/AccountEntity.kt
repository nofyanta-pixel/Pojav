package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val accountType: String = "OFFLINE", // OFFLINE, LOCAL, CRACKED
    val uuid: String,
    val skinModel: String = "STEVE", // STEVE (Classic 4px arm), ALEX (Slim 3px arm)
    val skinPresetId: String = "steve", // steve, alex, cyber_knight, ender_mage, netherite_slayer, hacker, anime_hero, panda
    val capePresetId: String = "migrator", // none, migrator, minecon_2011, minecon_2012, minecon_2013, minecon_2015, minecon_2016, vanilla, optifine, cherry
    val customSkinColorPrimary: Long = 0xFF2A52BE, // Hex ARGB
    val customSkinColorSecondary: Long = 0xFF5C93ED,
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
