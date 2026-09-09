package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LauncherDao {
    // Accounts
    @Query("SELECT * FROM accounts ORDER BY isActive DESC, createdAt DESC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE isActive = 1 LIMIT 1")
    fun getActiveAccount(): Flow<AccountEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE accounts SET isActive = CASE WHEN id = :selectedId THEN 1 ELSE 0 END")
    suspend fun setActiveAccount(selectedId: Long)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccountById(id: Long)

    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun getAccountCount(): Int

    // Modpacks
    @Query("SELECT * FROM modpacks ORDER BY isSelected DESC, id ASC")
    fun getAllModpacks(): Flow<List<ModpackEntity>>

    @Query("SELECT * FROM modpacks WHERE isSelected = 1 LIMIT 1")
    fun getSelectedModpack(): Flow<ModpackEntity?>

    @Query("SELECT * FROM modpacks WHERE id = :id LIMIT 1")
    suspend fun getModpackById(id: Long): ModpackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModpack(modpack: ModpackEntity): Long

    @Update
    suspend fun updateModpack(modpack: ModpackEntity)

    @Query("UPDATE modpacks SET isSelected = CASE WHEN id = :selectedId THEN 1 ELSE 0 END")
    suspend fun setSelectedModpack(selectedId: Long)

    @Query("DELETE FROM modpacks WHERE id = :id")
    suspend fun deleteModpackById(id: Long)

    @Query("SELECT COUNT(*) FROM modpacks")
    suspend fun getModpackCount(): Int

    // Mods
    @Query("SELECT * FROM mods WHERE modpackId = :modpackId ORDER BY name ASC")
    fun getModsForModpack(modpackId: Long): Flow<List<ModItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMod(mod: ModItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMods(mods: List<ModItemEntity>)

    @Update
    suspend fun updateMod(mod: ModItemEntity)

    @Query("DELETE FROM mods WHERE id = :id")
    suspend fun deleteModById(id: Long)

    @Query("DELETE FROM mods WHERE modpackId = :modpackId")
    suspend fun deleteModsByModpackId(modpackId: Long)

    // Performance
    @Query("SELECT * FROM performance_config WHERE id = 1")
    fun getPerformanceConfig(): Flow<PerformanceConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformanceConfig(config: PerformanceConfigEntity)
}
