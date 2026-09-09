package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AccountEntity
import com.example.data.local.AppDatabase
import com.example.data.local.ModItemEntity
import com.example.data.local.ModpackEntity
import com.example.data.local.PerformanceConfigEntity
import com.example.data.model.CapePreset
import com.example.data.model.CapePresets
import com.example.data.model.SkinPreset
import com.example.data.model.SkinPresets
import com.example.data.repository.LauncherRepository
import com.example.ui.components.ConsoleLogLine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class GameState {
    LAUNCHER,
    BOOTING,
    IN_GAME
}

data class InGameStats(
    val fps: Int = 90,
    val x: Double = 124.5,
    val y: Double = 68.0,
    val z: Double = -356.2,
    val biome: String = "minecraft:cherry_grove",
    val memoryUsedMb: Int = 1120,
    val memoryMaxMb: Int = 2560,
    val activeSlotIndex: Int = 0,
    val inventoryOpen: Boolean = false,
    val f3DebugOpen: Boolean = true,
    val f5Perspective: Int = 0, // 0: 1st person, 1: 3rd person back, 2: 3rd person front
    val hotbarItems: List<String> = listOf("Diamond Sword", "Netherite Pickaxe", "Golden Apple", "Crafting Table", "Oak Planks", "Torches", "Bow", "Cooked Beef", "Water Bucket"),
    val recentActionMessage: String = "Joined world 'Survival Singleplayer'"
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LauncherRepository
    init {
        val db = AppDatabase.getDatabase(application)
        repository = LauncherRepository(db.launcherDao())
        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    val accounts: StateFlow<List<AccountEntity>> = repository.allAccounts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeAccount: StateFlow<AccountEntity?> = repository.activeAccount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val modpacks: StateFlow<List<ModpackEntity>> = repository.allModpacks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedModpack: StateFlow<ModpackEntity?> = repository.selectedModpack
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val performanceConfig: StateFlow<PerformanceConfigEntity?> = repository.performanceConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Dynamic mods for the selected modpack
    val currentMods: StateFlow<List<ModItemEntity>> = selectedModpack
        .flatMapLatest { pack ->
            if (pack != null) repository.getModsForModpack(pack.id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Game lifecycle state
    private val _gameState = MutableStateFlow(GameState.LAUNCHER)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // Console logs during Pojav boot
    private val _bootLogs = MutableStateFlow<List<ConsoleLogLine>>(emptyList())
    val bootLogs: StateFlow<List<ConsoleLogLine>> = _bootLogs.asStateFlow()

    private val _bootProgress = MutableStateFlow(0f)
    val bootProgress: StateFlow<Float> = _bootProgress.asStateFlow()

    private val _bootStatus = MutableStateFlow("Ready to launch")
    val bootStatus: StateFlow<String> = _bootStatus.asStateFlow()

    // In-game simulation state
    private val _gameStats = MutableStateFlow(InGameStats())
    val gameStats: StateFlow<InGameStats> = _gameStats.asStateFlow()

    private var launchJob: Job? = null

    // Account Actions
    fun createAccount(
        username: String,
        type: String,
        skinModel: String,
        skinPresetId: String,
        capePresetId: String
    ) {
        viewModelScope.launch {
            repository.createAccount(username, type, skinModel, skinPresetId, capePresetId)
        }
    }

    fun switchAccount(accountId: Long) {
        viewModelScope.launch {
            repository.setActiveAccount(accountId)
        }
    }

    fun deleteAccount(accountId: Long) {
        viewModelScope.launch {
            repository.deleteAccount(accountId)
        }
    }

    fun updateSkinAndCape(account: AccountEntity, skinId: String, capeId: String, model: String) {
        viewModelScope.launch {
            repository.updateAccount(
                account.copy(
                    skinPresetId = skinId,
                    capePresetId = capeId,
                    skinModel = model
                )
            )
        }
    }

    // Modpack Actions
    fun selectModpack(packId: Long) {
        viewModelScope.launch {
            repository.setSelectedModpack(packId)
        }
    }

    fun createModpack(name: String, version: String, loader: String, description: String, ramMb: Int) {
        viewModelScope.launch {
            repository.createModpack(name, version, loader, description, ramMb)
        }
    }

    fun deleteModpack(packId: Long) {
        viewModelScope.launch {
            repository.deleteModpack(packId)
        }
    }

    fun addMod(modpackId: Long, name: String, fileName: String, category: String, version: String, description: String) {
        viewModelScope.launch {
            repository.addMod(modpackId, name, fileName, category, version, description)
        }
    }

    fun toggleMod(mod: ModItemEntity) {
        viewModelScope.launch {
            repository.toggleModEnabled(mod)
        }
    }

    fun deleteMod(modId: Long) {
        viewModelScope.launch {
            repository.deleteMod(modId)
        }
    }

    // Performance Actions
    fun applyPreset(presetName: String) {
        val current = performanceConfig.value ?: return
        val updated = when (presetName) {
            "POTATO" -> current.copy(
                preset = "POTATO",
                allocatedRamMb = 1536,
                renderer = "Holy GL4ES 1.1.5",
                resolutionScalePercent = 70,
                maxFps = 60,
                customJvmArgs = "-XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+AggressiveOpts -XX:+ParallelRefProcEnabled"
            )
            "BALANCED" -> current.copy(
                preset = "BALANCED",
                allocatedRamMb = 2560,
                renderer = "Holy GL4ES 1.1.5",
                resolutionScalePercent = 85,
                maxFps = 90,
                customJvmArgs = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200"
            )
            "ULTRA" -> current.copy(
                preset = "ULTRA",
                allocatedRamMb = 4096,
                renderer = "Zink (Vulkan)",
                resolutionScalePercent = 100,
                maxFps = 120,
                customJvmArgs = "-XX:+UseZGC -XX:+UnlockExperimentalVMOptions -XX:ZAllocationSpikeTolerance=5"
            )
            else -> current.copy(preset = "CUSTOM")
        }
        viewModelScope.launch {
            repository.updatePerformanceConfig(updated)
        }
    }

    fun updatePerformance(config: PerformanceConfigEntity) {
        viewModelScope.launch {
            repository.updatePerformanceConfig(config)
        }
    }

    // Game Launch & Pojav Runtime Simulation
    fun launchGame() {
        if (_gameState.value != GameState.LAUNCHER) return

        val pack = selectedModpack.value
        val acc = activeAccount.value
        val perf = performanceConfig.value

        _gameState.value = GameState.BOOTING
        _bootLogs.value = emptyList()
        _bootProgress.value = 0.05f

        launchJob?.cancel()
        launchJob = viewModelScope.launch {
            val df = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
            fun now() = df.format(Date())

            addLog("BOOT", "PojavLauncher", "Starting PojavLauncher Native Bridge v3.3.1.1...", now())
            delay(250)
            _bootProgress.value = 0.15f
            _bootStatus.value = "Unpacking LWJGL 3.3.3 binaries"
            addLog("INFO", "LWJGL", "Unpacking native libs: libglfw.so, libopenal.so, libgl4es.so", now())
            delay(300)

            _bootProgress.value = 0.30f
            _bootStatus.value = "Setting up Java JRE"
            val javaVer = perf?.javaVersion ?: "Java 21"
            addLog("INFO", "JVM", "Selected JRE: $javaVer [ARM64-v8a HotSpot Virtual Machine]", now())
            val ram = perf?.allocatedRamMb ?: 2560
            addLog("INFO", "JVM", "Memory bounds: -Xms512M -Xmx${ram}M", now())
            val jvmArgs = perf?.customJvmArgs ?: "-XX:+UseG1GC"
            addLog("INFO", "JVM", "Applied flags: $jvmArgs", now())
            delay(350)

            _bootProgress.value = 0.50f
            _bootStatus.value = "Configuring Renderer: ${perf?.renderer ?: "Holy GL4ES"}"
            addLog("INFO", "Renderer", "Selected graphics pipeline: ${perf?.renderer ?: "Holy GL4ES"}", now())
            addLog("INFO", "Display", "Virtual surface buffer initialized (${perf?.resolutionScalePercent ?: 85}% scale)", now())
            delay(300)

            _bootProgress.value = 0.70f
            val loader = pack?.loader ?: "Fabric"
            val mcVer = pack?.mcVersion ?: "1.20.4"
            _bootStatus.value = "Injecting $loader for Minecraft $mcVer"
            addLog("MOD", loader, "Bootstrapping $loader loader for Minecraft $mcVer", now())

            val modList = currentMods.value.filter { it.isEnabled }
            addLog("MOD", "ModManager", "Loaded ${modList.size} active optimization and gameplay mods:", now())
            modList.take(6).forEach {
                addLog("MOD", "ModManager", " -> ${it.name} (${it.fileName})", now())
            }
            delay(350)

            _bootProgress.value = 0.85f
            _bootStatus.value = "Authenticating user: ${acc?.username ?: "Player"}"
            addLog("INFO", "Auth", "Logged in as ${acc?.username ?: "Player"} [${acc?.accountType ?: "OFFLINE"}]", now())
            addLog("INFO", "Skin", "Applied skin: ${acc?.skinPresetId ?: "steve"} (${acc?.skinModel ?: "STEVE"}) with cape: ${acc?.capePresetId ?: "migrator"}", now())
            delay(300)

            _bootProgress.value = 0.95f
            _bootStatus.value = "Starting net.minecraft.client.main.Main"
            addLog("BOOT", "Minecraft", "Invoking net.minecraft.client.main.Main.main()", now())
            addLog("INFO", "Minecraft", "OpenAL sound engine initialized. Sound device: OpenAL Soft on Android AudioTrack", now())
            addLog("INFO", "Minecraft", "Entering main loop! pojavexec finished successfully.", now())
            delay(400)

            _bootProgress.value = 1.0f
            _bootStatus.value = "Game Running!"

            // Configure in-game FPS based on performance preset
            val targetFps = when (perf?.preset) {
                "POTATO" -> 118
                "ULTRA" -> 75
                else -> 92
            }
            _gameStats.value = _gameStats.value.copy(
                fps = targetFps,
                memoryMaxMb = ram,
                memoryUsedMb = (ram * 0.45).toInt()
            )

            delay(300)
            _gameState.value = GameState.IN_GAME
        }
    }

    private fun addLog(level: String, tag: String, msg: String, timestamp: String) {
        val list = _bootLogs.value.toMutableList()
        list.add(ConsoleLogLine(level, tag, msg, timestamp))
        _bootLogs.value = list
    }

    fun exitGame() {
        launchJob?.cancel()
        _gameState.value = GameState.LAUNCHER
    }

    // In-Game Virtual Actions
    fun toggleF3() {
        _gameStats.value = _gameStats.value.copy(f3DebugOpen = !_gameStats.value.f3DebugOpen)
    }

    fun toggleF5() {
        val next = (_gameStats.value.f5Perspective + 1) % 3
        _gameStats.value = _gameStats.value.copy(f5Perspective = next)
    }

    fun toggleInventory() {
        _gameStats.value = _gameStats.value.copy(inventoryOpen = !_gameStats.value.inventoryOpen)
    }

    fun selectHotbarSlot(index: Int) {
        _gameStats.value = _gameStats.value.copy(activeSlotIndex = index)
    }

    fun triggerPlayerAction(action: String) {
        val activeItem = _gameStats.value.hotbarItems.getOrNull(_gameStats.value.activeSlotIndex) ?: "Hand"
        val message = when (action) {
            "ATTACK" -> "Swung $activeItem (Attack/Break block)"
            "USE" -> "Used $activeItem (Place/Interact)"
            "FORWARD" -> "Moved forward"
            "BACK" -> "Moved backward"
            "LEFT" -> "Moved left"
            "RIGHT" -> "Moved right"
            "JUMP" -> "Jumped"
            "SNEAK" -> "Sneaked / Crouched"
            else -> "Action: $action"
        }
        _gameStats.value = _gameStats.value.copy(recentActionMessage = message)
    }
}
