package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AccountEntity
import com.example.data.local.ModpackEntity
import com.example.data.local.PerformanceConfigEntity
import com.example.data.model.CapePresets
import com.example.data.model.SkinPresets
import com.example.ui.GameState
import com.example.ui.InGameStats
import com.example.ui.components.ConsoleLogLine
import com.example.ui.components.MinecraftCharacterViewer
import com.example.ui.components.PojavConsoleLogs
import com.example.ui.components.PojavVirtualControls
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McRedstone

@Composable
fun GameViewScreen(
    gameState: GameState,
    bootLogs: List<ConsoleLogLine>,
    bootProgress: Float,
    bootStatus: String,
    stats: InGameStats,
    account: AccountEntity?,
    modpack: ModpackEntity?,
    config: PerformanceConfigEntity?,
    onExitGame: () -> Unit,
    onToggleF3: () -> Unit,
    onToggleF5: () -> Unit,
    onToggleInventory: () -> Unit,
    onSelectHotbarSlot: (Int) -> Unit,
    onTriggerAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPaused by remember { mutableStateOf(false) }
    var showChatDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(Color.Black)) {

        if (gameState == GameState.BOOTING) {
            // BOOT SEQUENCE CONSOLE LOGS
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                PojavConsoleLogs(
                    logs = bootLogs,
                    progress = bootProgress,
                    statusText = bootStatus,
                    modifier = Modifier.fillMaxSize()
                )
            }

        } else if (gameState == GameState.IN_GAME) {
            // RUNNING MINECRAFT JAVA SIMULATION

            // 1. 3D BLOCKY WORLD VIEWPORT CANVAS
            MinecraftWorldCanvas(
                perspective = stats.f5Perspective,
                account = account,
                modifier = Modifier.fillMaxSize()
            )

            // 2. F3 DEBUG OVERLAY (AUTHENTIC JAVA F3 SCREEN)
            if (stats.f3DebugOpen) {
                F3DebugScreen(
                    stats = stats,
                    account = account,
                    modpack = modpack,
                    config = config,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 12.dp, end = 12.dp)
                )
            }

            // 3. RECENT ACTION TOAST
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 54.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xBB000000),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x44FFFFFF))
            ) {
                Text(
                    text = stats.recentActionMessage,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = McEmerald,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            // 4. HEALTH BAR, HUNGER BAR, AND HOTBAR (Minecraft Java HUD)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Hearts & Food Row
                Row(
                    modifier = Modifier.width(320.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 10 Hearts
                    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        repeat(10) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Heart",
                                tint = McRedstone,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    // 10 Hunger drumsticks
                    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        repeat(10) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = "Food",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }

                // 9 Hotbar slots
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xCC2C3530))
                        .border(2.dp, Color(0xFF55685C), RoundedCornerShape(6.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    stats.hotbarItems.forEachIndexed { index, item ->
                        val isSelected = index == stats.activeSlotIndex
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) Color(0xFF526E5B) else Color(0x881E2621))
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) Color.White else Color(0x44FFFFFF),
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { onSelectHotbarSlot(index) }
                                .testTag("hotbar_slot_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getItemEmoji(item),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // 5. POJAV VIRTUAL TOUCH CONTROLS (D-Pad, Action Keys, Function Keys)
            PojavVirtualControls(
                opacity = config?.touchControlOpacity ?: 0.85f,
                isF3Active = stats.f3DebugOpen,
                onToggleF3 = onToggleF3,
                onToggleF5 = onToggleF5,
                onOpenInventory = onToggleInventory,
                onOpenChat = { showChatDialog = true },
                onPauseGame = { isPaused = true },
                onPrimaryAction = { onTriggerAction("ATTACK") },
                onSecondaryAction = { onTriggerAction("USE") },
                onMove = { onTriggerAction(it) },
                modifier = Modifier.fillMaxSize()
            )

            // 6. INVENTORY DIALOG (INV)
            if (stats.inventoryOpen) {
                MinecraftInventoryDialog(
                    stats = stats,
                    onClose = onToggleInventory,
                    onSelectItem = { onSelectHotbarSlot(it) }
                )
            }

            // 7. PAUSE / ESC MENU
            if (isPaused) {
                MinecraftPauseMenu(
                    onResume = { isPaused = false },
                    onQuitToLauncher = {
                        isPaused = false
                        onExitGame()
                    }
                )
            }

            // 8. CHAT / COMMAND DIALOG (CHAT / KBD)
            if (showChatDialog) {
                MinecraftChatDialog(
                    onDismiss = { showChatDialog = false },
                    onSend = { msg ->
                        onTriggerAction("Chat: $msg")
                        showChatDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun MinecraftWorldCanvas(
    perspective: Int,
    account: AccountEntity?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. SKY GRADIENT (Daytime twilight over Cherry Grove)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF386FA4),
                        Color(0xFF7CA982),
                        Color(0xFFC7EFCF)
                    ),
                    startY = 0f,
                    endY = h * 0.65f
                ),
                size = Size(w, h * 0.65f)
            )

            // 2. SUN (Pixel blocky sun)
            drawRect(
                color = Color(0xFFFFF7C2),
                topLeft = Offset(w * 0.72f, h * 0.12f),
                size = Size(40.dp.toPx(), 40.dp.toPx())
            )

            // 3. DISTANT MOUNTAINS & CHERRY TREES
            val mountainPath = Path().apply {
                moveTo(0f, h * 0.55f)
                lineTo(w * 0.2f, h * 0.42f)
                lineTo(w * 0.35f, h * 0.48f)
                lineTo(w * 0.55f, h * 0.38f)
                lineTo(w * 0.75f, h * 0.49f)
                lineTo(w, h * 0.40f)
                lineTo(w, h * 0.65f)
                lineTo(0f, h * 0.65f)
                close()
            }
            drawPath(mountainPath, Color(0xFF4A6B53))

            // Snow peaks
            drawCircle(Color(0xFFE2E8F0), radius = 24.dp.toPx(), center = Offset(w * 0.55f, h * 0.39f))

            // 4. TERRAIN (Grass Block Hill)
            drawRect(
                color = Color(0xFF4A8505), // Vibrant Minecraft grass
                topLeft = Offset(0f, h * 0.62f),
                size = Size(w, 20.dp.toPx())
            )
            drawRect(
                color = Color(0xFF6B4226), // Dirt below
                topLeft = Offset(0f, h * 0.62f + 20.dp.toPx()),
                size = Size(w, h)
            )

            // Crosshair in middle (First person)
            val cx = w / 2f
            val cy = h / 2f
            val chSize = 10.dp.toPx()
            drawLine(Color.White.copy(alpha = 0.8f), Offset(cx - chSize, cy), Offset(cx + chSize, cy), strokeWidth = 2f)
            drawLine(Color.White.copy(alpha = 0.8f), Offset(cx, cy - chSize), Offset(cx, cy + chSize), strokeWidth = 2f)
        }

        // Perspective 3rd person character rendering
        if (perspective > 0 && account != null) {
            val skin = SkinPresets.getById(account.skinPresetId)
            val cape = CapePresets.getById(account.capePresetId)
            Box(
                modifier = Modifier
                    .size(160.dp, 240.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp)
            ) {
                MinecraftCharacterViewer(
                    skin = skin,
                    cape = cape,
                    height = 240.dp,
                    showControls = false
                )
            }
        }
    }
}

@Composable
private fun F3DebugScreen(
    stats: InGameStats,
    account: AccountEntity?,
    modpack: ModpackEntity?,
    config: PerformanceConfigEntity?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column Stats
        Column(
            modifier = Modifier
                .background(Color(0x88000000), RoundedCornerShape(4.dp))
                .padding(6.dp)
        ) {
            DebugText("Minecraft ${modpack?.mcVersion ?: "1.20.4"} (${modpack?.loader ?: "Fabric"}/Client)")
            DebugText("${stats.fps} fps T: inf, B: 0 (${config?.renderer ?: "Holy GL4ES 1.1.5"})")
            DebugText("XYZ: ${String.format("%.3f", stats.x)} / ${String.format("%.3f", stats.y)} / ${String.format("%.3f", stats.z)}")
            DebugText("Block: ${stats.x.toInt()} ${stats.y.toInt()} ${stats.z.toInt()}")
            DebugText("Chunk: 8 4 12 in 7 4 -23")
            DebugText("Facing: north (Towards negative Z) (179.2 / -4.1)")
            DebugText("Biome: ${stats.biome}")
            DebugText("Light: 15 (15 sky, 0 block)")
            DebugText("Local Difficulty: 2.25 // 0.00 (Day 42)")
        }

        // Right Column Stats (Memory & GPU Driver)
        Column(
            modifier = Modifier
                .background(Color(0x88000000), RoundedCornerShape(4.dp))
                .padding(6.dp),
            horizontalAlignment = Alignment.End
        ) {
            DebugText("Java: ${config?.javaVersion?.split(" ")?.first() ?: "21.0.3"} 64bit")
            DebugText("Mem: ${(stats.memoryUsedMb * 100 / stats.memoryMaxMb)}% ${stats.memoryUsedMb}/${stats.memoryMaxMb}MB")
            DebugText("Allocated: 62% ${(stats.memoryMaxMb * 0.62).toInt()}MB")
            DebugText("Display: Pojav Virtual (${config?.resolutionScalePercent ?: 85}%)")
            DebugText("Renderer: ${config?.renderer ?: "Holy GL4ES"}")
            DebugText("GPU: Adreno 730 / Mali-G710")
            DebugText("Mod: Sodium 0.5.8 (Active)")
            DebugText("User: ${account?.username ?: "Player"}")
        }
    }
}

@Composable
private fun DebugText(text: String) {
    Text(
        text = text,
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFFE2E8F0),
        lineHeight = 12.sp
    )
}

@Composable
private fun MinecraftPauseMenu(
    onResume: () -> Unit,
    onQuitToLauncher: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC0B100D))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.width(280.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2822)),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF334538))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Game Menu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Button(
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C4032), contentColor = Color.White),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Lanjutkan Game (Resume)", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onQuitToLauncher,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = McRedstone, contentColor = Color.White),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Kembali ke Launcher", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MinecraftInventoryDialog(
    stats: InGameStats,
    onClose: () -> Unit,
    onSelectItem: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Crafting & Inventory", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Pilih slot hotbar untuk digunakan:", fontSize = 12.sp, color = McEmerald)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(stats.hotbarItems) { index, item ->
                        val isSelected = index == stats.activeSlotIndex
                        Surface(
                            modifier = Modifier
                                .clickable {
                                    onSelectItem(index)
                                    onClose()
                                },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Color(0xFF2E4D37) else Color(0xFF1C241F),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) McEmerald else Color(0xFF2C3930)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = getItemEmoji(item), fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = McEmerald, contentColor = McObsidian)
            ) {
                Text("Tutup", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFF151D18)
    )
}

@Composable
private fun MinecraftChatDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Chat / Perintah Pojav", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Ketik pesan atau /gamemode...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        modifier = Modifier.clickable { text = "/gamemode creative" },
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF1F2E24)
                    ) {
                        Text("/gamemode creative", fontSize = 10.sp, color = McEmerald, modifier = Modifier.padding(4.dp))
                    }
                    Surface(
                        modifier = Modifier.clickable { text = "/time set day" },
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF1F2E24)
                    ) {
                        Text("/time set day", fontSize = 10.sp, color = McGold, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotBlank()) onSend(text) },
                colors = ButtonDefaults.buttonColors(containerColor = McEmerald, contentColor = McObsidian)
            ) {
                Text("Kirim", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26332A), contentColor = Color.White)
            ) {
                Text("Batal")
            }
        },
        containerColor = Color(0xFF151D18)
    )
}

private fun getItemEmoji(name: String): String {
    return when {
        name.contains("Sword") -> "⚔️"
        name.contains("Pickaxe") -> "⛏️"
        name.contains("Apple") -> "🍎"
        name.contains("Table") -> "🪑"
        name.contains("Planks") -> "🪵"
        name.contains("Torch") -> "🕯️"
        name.contains("Bow") -> "🏹"
        name.contains("Beef") -> "🥩"
        name.contains("Water") -> "🪣"
        else -> "📦"
    }
}
