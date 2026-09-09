package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.AccountEntity
import com.example.data.local.ModpackEntity
import com.example.data.local.PerformanceConfigEntity
import com.example.data.model.CapePresets
import com.example.data.model.SkinPresets
import com.example.ui.components.MinecraftCharacterViewer
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McEmeraldDark
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McRedstone
import com.example.ui.theme.McSurface
import com.example.ui.theme.McSurfaceVariant
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary

@Composable
fun HomeScreen(
    selectedModpack: ModpackEntity?,
    activeAccount: AccountEntity?,
    performanceConfig: PerformanceConfigEntity?,
    activeModsCount: Int,
    onLaunch: () -> Unit,
    onNavigateModpacks: () -> Unit,
    onNavigateAccounts: () -> Unit,
    onNavigatePerformance: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showQuickTerminal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // 1. HERO BANNER WITH POJAV TITLE
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF2E3D32), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mc_hero_banner),
                    contentDescription = "Minecraft Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark vignette gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x33000000),
                                    Color(0xE60D120E)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xCC111813),
                            border = androidx.compose.foundation.BorderStroke(1.dp, McEmerald.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(McEmerald)
                                )
                                Text(
                                    text = "POJAV ENGINE v3.3",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = McEmerald
                                )
                            }
                        }

                        // JRE Version badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xAA1E2821)
                        ) {
                            Text(
                                text = performanceConfig?.javaVersion?.split(" ")?.take(2)?.joinToString(" ") ?: "Java 21",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = McGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Minecraft Java Edition",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "PojavLauncher Android Runtime • Modpack & Skin Ready",
                            fontSize = 12.sp,
                            color = McEmeraldLightColor
                        )
                    }
                }
            }
        }

        // 2. ACTIVE ACCOUNT & MODPACK SUMMARY CARDS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Modpack Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateModpacks() }
                        .testTag("home_modpack_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = McSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = "Modpack",
                                tint = McDiamond,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Modpack Aktif",
                                fontSize = 11.sp,
                                color = McTextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedModpack?.name ?: "Fabulously Optimized",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "${selectedModpack?.loader ?: "Fabric"} ${selectedModpack?.mcVersion ?: "1.20.4"} • $activeModsCount Mod",
                            fontSize = 11.sp,
                            color = McEmerald
                        )
                    }
                }

                // Account Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateAccounts() }
                        .testTag("home_account_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = McSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Account",
                                tint = McGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Akun Offline",
                                fontSize = 11.sp,
                                color = McTextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeAccount?.username ?: "PojavPlayer",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "${activeAccount?.skinModel ?: "STEVE"} • ${activeAccount?.capePresetId ?: "Migrator"}",
                            fontSize = 11.sp,
                            color = McGold
                        )
                    }
                }
            }
        }

        // 3. BIG PRIMARY LAUNCH BUTTON (Pojav Style Green Gamepad Action)
        item {
            Button(
                onClick = onLaunch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(12.dp, RoundedCornerShape(14.dp), spotColor = McEmerald)
                    .testTag("launch_game_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = McEmerald,
                    contentColor = McObsidian
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(32.dp),
                        tint = McObsidian
                    )
                    Column {
                        Text(
                            text = "LUNCURKAN MINECRAFT",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = McObsidian
                        )
                        Text(
                            text = "${selectedModpack?.name ?: "Java Edition"} (${performanceConfig?.allocatedRamMb ?: 2560} MB RAM)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F3118)
                        )
                    }
                }
            }
        }

        // 4. PERFORMANCE & OPTIMIZATION QUICK HUD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigatePerformance() }
                    .testTag("home_performance_bar"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141D17)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26392B))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1F3024)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Performance",
                                tint = McEmerald,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Optimasi Performa Pojav",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Mode: ${performanceConfig?.preset ?: "BALANCED"} • ${performanceConfig?.renderer ?: "Holy GL4ES"} • Skala ${performanceConfig?.resolutionScalePercent ?: 85}%",
                                fontSize = 11.sp,
                                color = McTextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Optimize",
                        tint = McGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 5. MINI SKIN PREVIEW CARD
        item {
            activeAccount?.let { acc ->
                val skin = SkinPresets.getById(acc.skinPresetId)
                val cape = CapePresets.getById(acc.capePresetId)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = McSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3E33))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kustomisasi Karakter & Jubah",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Ketuk untuk ganti",
                                fontSize = 11.sp,
                                color = McEmerald,
                                modifier = Modifier.clickable { onNavigateAccounts() }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Character Canvas miniature
                            Box(
                                modifier = Modifier
                                    .size(110.dp, 140.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                MinecraftCharacterViewer(
                                    skin = skin,
                                    cape = cape,
                                    height = 140.dp,
                                    showControls = false
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = skin.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = McGold
                                )
                                Text(
                                    text = skin.description,
                                    fontSize = 11.sp,
                                    color = McTextSecondary,
                                    lineHeight = 15.sp,
                                    maxLines = 2
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF131A15),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF28362D))
                                ) {
                                    Text(
                                        text = "Jubah: ${cape.name}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = McEmerald,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

private val McEmeraldLightColor = Color(0xFFA7F3D0)
