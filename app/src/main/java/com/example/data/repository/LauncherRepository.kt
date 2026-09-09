package com.example.data.repository

import com.example.data.local.AccountEntity
import com.example.data.local.LauncherDao
import com.example.data.local.ModItemEntity
import com.example.data.local.ModpackEntity
import com.example.data.local.PerformanceConfigEntity
import com.example.data.model.DefaultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class LauncherRepository(private val dao: LauncherDao) {

    val allAccounts: Flow<List<AccountEntity>> = dao.getAllAccounts()
    val activeAccount: Flow<AccountEntity?> = dao.getActiveAccount()

    val allModpacks: Flow<List<ModpackEntity>> = dao.getAllModpacks()
    val selectedModpack: Flow<ModpackEntity?> = dao.getSelectedModpack()

    val performanceConfig: Flow<PerformanceConfigEntity?> = dao.getPerformanceConfig()

    fun getModsForModpack(modpackId: Long): Flow<List<ModItemEntity>> =
        dao.getModsForModpack(modpackId)

    suspend fun initializeIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.getAccountCount() == 0) {
            dao.insertAccount(DefaultData.initialAccount)
        }
        if (dao.getModpackCount() == 0) {
            DefaultData.initialModpacks.forEach { pack ->
                val insertedId = dao.insertModpack(pack)
                val initialMods = DefaultData.getInitialMods(pack.id).map { it.copy(modpackId = insertedId) }
                if (initialMods.isNotEmpty()) {
                    dao.insertMods(initialMods)
                }
            }
        }
        val currentPerf = dao.getPerformanceConfig().firstOrNull()
        if (currentPerf == null) {
            dao.insertPerformanceConfig(DefaultData.initialPerformanceConfig)
        }
    }

    // Account methods
    suspend fun createAccount(username: String, accountType: String, skinModel: String, skinPresetId: String, capePresetId: String): Long =
        withContext(Dispatchers.IO) {
            val shortUuid = java.util.UUID.randomUUID().toString().substring(0, 18)
            val newAccount = AccountEntity(
                username = username.trim().ifEmpty { "PojavCrafter" },
                accountType = accountType,
                uuid = shortUuid,
                skinModel = skinModel,
                skinPresetId = skinPresetId,
                capePresetId = capePresetId,
                isActive = false
            )
            val id = dao.insertAccount(newAccount)
            dao.setActiveAccount(id)
            id
        }

    suspend fun updateAccount(account: AccountEntity) = withContext(Dispatchers.IO) {
        dao.updateAccount(account)
    }

    suspend fun setActiveAccount(id: Long) = withContext(Dispatchers.IO) {
        dao.setActiveAccount(id)
    }

    suspend fun deleteAccount(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteAccountById(id)
    }

    // Modpack methods
    suspend fun createModpack(name: String, mcVersion: String, loader: String, description: String, memoryMb: Int): Long =
        withContext(Dispatchers.IO) {
            val newPack = ModpackEntity(
                name = name.trim().ifEmpty { "Custom Modpack" },
                mcVersion = mcVersion,
                loader = loader,
                loaderVersion = if (loader == "Fabric") "0.15.11" else "Latest",
                description = description.trim().ifEmpty { "Custom Minecraft $mcVersion instance" },
                iconTag = "cube",
                isSelected = false,
                memoryMb = memoryMb
            )
            val id = dao.insertModpack(newPack)
            dao.setSelectedModpack(id)
            id
        }

    suspend fun setSelectedModpack(id: Long) = withContext(Dispatchers.IO) {
        dao.setSelectedModpack(id)
    }

    suspend fun deleteModpack(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteModsByModpackId(id)
        dao.deleteModpackById(id)
    }

    // Mods
    suspend fun addMod(modpackId: Long, name: String, fileName: String, category: String, version: String, description: String) =
        withContext(Dispatchers.IO) {
            val mod = ModItemEntity(
                modpackId = modpackId,
                name = name.trim().ifEmpty { "Custom Mod" },
                fileName = if (fileName.endsWith(".jar")) fileName else "$fileName.jar",
                version = version.ifEmpty { "1.0.0" },
                category = category,
                description = description.ifEmpty { "User-installed mod" },
                isEnabled = true
            )
            dao.insertMod(mod)
        }

    suspend fun toggleModEnabled(mod: ModItemEntity) = withContext(Dispatchers.IO) {
        dao.updateMod(mod.copy(isEnabled = !mod.isEnabled))
    }

    suspend fun deleteMod(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteModById(id)
    }

    // Performance
    suspend fun updatePerformanceConfig(config: PerformanceConfigEntity) = withContext(Dispatchers.IO) {
        dao.insertPerformanceConfig(config)
    }
}
