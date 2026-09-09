package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.GameState
import com.example.ui.LauncherViewModel
import com.example.ui.screens.AccountsScreen
import com.example.ui.screens.GameViewScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ModpacksScreen
import com.example.ui.screens.PerformanceScreen
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McSurface
import com.example.ui.theme.MyApplicationTheme

enum class LauncherTab {
    HOME,
    MODPACKS,
    ACCOUNTS,
    PERFORMANCE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: LauncherViewModel = viewModel()
                LauncherApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherApp(viewModel: LauncherViewModel) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val activeAccount by viewModel.activeAccount.collectAsStateWithLifecycle()
    val modpacks by viewModel.modpacks.collectAsStateWithLifecycle()
    val selectedModpack by viewModel.selectedModpack.collectAsStateWithLifecycle()
    val performanceConfig by viewModel.performanceConfig.collectAsStateWithLifecycle()
    val currentMods by viewModel.currentMods.collectAsStateWithLifecycle()

    val bootLogs by viewModel.bootLogs.collectAsStateWithLifecycle()
    val bootProgress by viewModel.bootProgress.collectAsStateWithLifecycle()
    val bootStatus by viewModel.bootStatus.collectAsStateWithLifecycle()
    val gameStats by viewModel.gameStats.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(LauncherTab.HOME) }

    if (gameState != GameState.LAUNCHER) {
        // FULLSCREEN IN-GAME / BOOTING VIEWPORT
        GameViewScreen(
            gameState = gameState,
            bootLogs = bootLogs,
            bootProgress = bootProgress,
            bootStatus = bootStatus,
            stats = gameStats,
            account = activeAccount,
            modpack = selectedModpack,
            config = performanceConfig,
            onExitGame = { viewModel.exitGame() },
            onToggleF3 = { viewModel.toggleF3() },
            onToggleF5 = { viewModel.toggleF5() },
            onToggleInventory = { viewModel.toggleInventory() },
            onSelectHotbarSlot = { viewModel.selectHotbarSlot(it) },
            onTriggerAction = { viewModel.triggerPlayerAction(it) }
        )
    } else {
        // LAUNCHER DASHBOARD WITH TOP BAR & BOTTOM NAVIGATION
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = McObsidian,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Pojav Icon
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(McEmerald)
                                    .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "P",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp,
                                    color = McObsidian,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "PojavLauncher",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF1E2E23)
                                    ) {
                                        Text(
                                            text = "Java Edition",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = McEmerald,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${selectedModpack?.name ?: "Java"} • ${activeAccount?.username ?: "Offline"}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF8FA194)
                                )
                            }
                        }
                    },
                    actions = {
                        // Quick Launch action icon in top bar
                        IconButton(
                            onClick = { viewModel.launchGame() },
                            modifier = Modifier.testTag("top_launch_action")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(McEmerald),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Quick Launch",
                                    tint = McObsidian,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = McSurface,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF121814),
                    contentColor = McEmerald,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == LauncherTab.HOME,
                        onClick = { currentTab = LauncherTab.HOME },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == LauncherTab.HOME) Icons.Default.Home else Icons.Outlined.Home,
                                contentDescription = "Beranda"
                            )
                        },
                        label = { Text("Beranda", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = McObsidian,
                            selectedTextColor = McEmerald,
                            indicatorColor = McEmerald,
                            unselectedIconColor = Color(0xFF7E9284),
                            unselectedTextColor = Color(0xFF7E9284)
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == LauncherTab.MODPACKS,
                        onClick = { currentTab = LauncherTab.MODPACKS },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == LauncherTab.MODPACKS) Icons.Default.Folder else Icons.Outlined.Folder,
                                contentDescription = "Modpack"
                            )
                        },
                        label = { Text("Modpack", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = McObsidian,
                            selectedTextColor = McEmerald,
                            indicatorColor = McEmerald,
                            unselectedIconColor = Color(0xFF7E9284),
                            unselectedTextColor = Color(0xFF7E9284)
                        ),
                        modifier = Modifier.testTag("nav_modpacks")
                    )

                    NavigationBarItem(
                        selected = currentTab == LauncherTab.ACCOUNTS,
                        onClick = { currentTab = LauncherTab.ACCOUNTS },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == LauncherTab.ACCOUNTS) Icons.Default.Person else Icons.Outlined.Person,
                                contentDescription = "Akun"
                            )
                        },
                        label = { Text("Akun & Skin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = McObsidian,
                            selectedTextColor = McEmerald,
                            indicatorColor = McEmerald,
                            unselectedIconColor = Color(0xFF7E9284),
                            unselectedTextColor = Color(0xFF7E9284)
                        ),
                        modifier = Modifier.testTag("nav_accounts")
                    )

                    NavigationBarItem(
                        selected = currentTab == LauncherTab.PERFORMANCE,
                        onClick = { currentTab = LauncherTab.PERFORMANCE },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == LauncherTab.PERFORMANCE) Icons.Default.Speed else Icons.Outlined.Speed,
                                contentDescription = "Performa"
                            )
                        },
                        label = { Text("Performa", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = McObsidian,
                            selectedTextColor = McEmerald,
                            indicatorColor = McEmerald,
                            unselectedIconColor = Color(0xFF7E9284),
                            unselectedTextColor = Color(0xFF7E9284)
                        ),
                        modifier = Modifier.testTag("nav_performance")
                    )
                }
            }
        ) { innerPadding ->
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                label = "tab_animation"
            ) { tab ->
                when (tab) {
                    LauncherTab.HOME -> {
                        HomeScreen(
                            selectedModpack = selectedModpack,
                            activeAccount = activeAccount,
                            performanceConfig = performanceConfig,
                            activeModsCount = currentMods.filter { it.isEnabled }.size,
                            onLaunch = { viewModel.launchGame() },
                            onNavigateModpacks = { currentTab = LauncherTab.MODPACKS },
                            onNavigateAccounts = { currentTab = LauncherTab.ACCOUNTS },
                            onNavigatePerformance = { currentTab = LauncherTab.PERFORMANCE }
                        )
                    }

                    LauncherTab.MODPACKS -> {
                        ModpacksScreen(
                            modpacks = modpacks,
                            selectedModpack = selectedModpack,
                            mods = currentMods,
                            onSelectModpack = { viewModel.selectModpack(it) },
                            onCreateModpack = { name, ver, loader, desc, ram ->
                                viewModel.createModpack(name, ver, loader, desc, ram)
                            },
                            onDeleteModpack = { viewModel.deleteModpack(it) },
                            onToggleMod = { viewModel.toggleMod(it) },
                            onAddMod = { packId, name, fileName, cat, ver, desc ->
                                viewModel.addMod(packId, name, fileName, cat, ver, desc)
                            },
                            onDeleteMod = { viewModel.deleteMod(it) }
                        )
                    }

                    LauncherTab.ACCOUNTS -> {
                        AccountsScreen(
                            accounts = accounts,
                            activeAccount = activeAccount,
                            onCreateAccount = { username, type, model, skinId, capeId ->
                                viewModel.createAccount(username, type, model, skinId, capeId)
                            },
                            onSwitchAccount = { viewModel.switchAccount(it) },
                            onDeleteAccount = { viewModel.deleteAccount(it) },
                            onUpdateSkinAndCape = { acc, skinId, capeId, model ->
                                viewModel.updateSkinAndCape(acc, skinId, capeId, model)
                            }
                        )
                    }

                    LauncherTab.PERFORMANCE -> {
                        PerformanceScreen(
                            config = performanceConfig,
                            onApplyPreset = { viewModel.applyPreset(it) },
                            onUpdateConfig = { viewModel.updatePerformance(it) }
                        )
                    }
                }
            }
        }
    }
}
