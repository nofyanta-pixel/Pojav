package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.example.data.local.PerformanceConfigEntity
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McRedstone
import com.example.ui.theme.McSurface
import com.example.ui.theme.McSurfaceVariant
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary

@Composable
fun PerformanceScreen(
    config: PerformanceConfigEntity?,
    onApplyPreset: (String) -> Unit,
    onUpdateConfig: (PerformanceConfigEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val current = config ?: return

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Header
        item {
            Column {
                Text(
                    text = "Optimasi & Konfigurasi Pojav",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tuning memori RAM, renderer grafis, resolusi, dan argumen JVM",
                    fontSize = 12.sp,
                    color = McTextSecondary
                )
            }
        }

        // 1. QUICK PERFORMANCE PRESETS (POTATO, BALANCED, ULTRA, CUSTOM)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = McSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = McGold, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Preset Optimasi Cepat (1-Tap)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PresetButton(
                            title = "Potato Mode",
                            sub = "Max FPS (HP Kentang)",
                            isSelected = current.preset == "POTATO",
                            accentColor = McRedstone,
                            modifier = Modifier.weight(1f),
                            onClick = { onApplyPreset("POTATO") }
                        )
                        PresetButton(
                            title = "Balanced",
                            sub = "Standar Pojav",
                            isSelected = current.preset == "BALANCED",
                            accentColor = McEmerald,
                            modifier = Modifier.weight(1f),
                            onClick = { onApplyPreset("BALANCED") }
                        )
                        PresetButton(
                            title = "Shader Ultra",
                            sub = "Vulkan & High-End",
                            isSelected = current.preset == "ULTRA",
                            accentColor = McDiamond,
                            modifier = Modifier.weight(1f),
                            onClick = { onApplyPreset("ULTRA") }
                        )
                    }
                }
            }
        }

        // 2. RAM ALLOCATOR SLIDER
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = McSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Memory, contentDescription = null, tint = McEmerald, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Alokasi Memori JVM (RAM)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1B2F21),
                            border = androidx.compose.foundation.BorderStroke(1.dp, McEmerald.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "${current.allocatedRamMb} MB",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = McEmerald,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Disarankan 2048 MB - 3072 MB untuk HP 6GB/8GB RAM agar sistem Android tidak force close.",
                        fontSize = 11.sp,
                        color = McTextSecondary
                    )

                    Slider(
                        value = current.allocatedRamMb.toFloat(),
                        onValueChange = {
                            val rounded = ((it / 256).toInt()) * 256
                            onUpdateConfig(current.copy(allocatedRamMb = rounded, preset = "CUSTOM"))
                        },
                        valueRange = 1024f..6144f,
                        steps = 19,
                        colors = SliderDefaults.colors(
                            thumbColor = McEmerald,
                            activeTrackColor = McEmerald,
                            inactiveTrackColor = Color(0xFF1E2B21)
                        ),
                        modifier = Modifier.testTag("ram_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "1024 MB", fontSize = 10.sp, color = McTextSecondary)
                        Text(text = "2560 MB (Rekomendasi)", fontSize = 10.sp, color = McGold)
                        Text(text = "6144 MB", fontSize = 10.sp, color = McTextSecondary)
                    }
                }
            }
        }

        // 3. GRAPHICS RENDERER ENGINE
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = McSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.DisplaySettings, contentDescription = null, tint = McDiamond, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Renderer Grafis (Driver OpenGL)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    val renderers = listOf(
                        Triple("Holy GL4ES 1.1.5", "Sangat Cepat & Stabil (Default Snapdragon/Mali)", McEmerald),
                        Triple("VirGL Mesa 3D", "Dukungan OpenGL 4.3 Penuh", McGold),
                        Triple("Zink (Vulkan)", "OpenGL via Vulkan (Perlu Vulkan 1.2+)", McDiamond),
                        Triple("ANGLE (DirectX/Vulkan)", "Eksperimental Mobile Renderer", Color(0xFF9E9E9E))
                    )

                    renderers.forEach { (name, desc, color) ->
                        val isSelected = current.renderer == name
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onUpdateConfig(current.copy(renderer = name, preset = "CUSTOM")) }
                                .testTag("renderer_option_$name"),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF1E2E23) else Color(0xFF151C17),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) color else Color(0xFF233026)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(text = desc, fontSize = 10.sp, color = McTextSecondary)
                                }
                                if (isSelected) {
                                    Surface(shape = RoundedCornerShape(4.dp), color = color) {
                                        Text(
                                            text = "AKTIF",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = McObsidian,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. JAVA JRE VERSION & RESOLUTION SCALER
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = McSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Java selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Java Runtime Environment (JRE)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        val javas = listOf("Java 21 (LTS Modern)", "Java 17 (LTS 1.17-1.20)", "Java 8 (Legacy 1.12.2)")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            javas.forEach { jvm ->
                                val selected = current.javaVersion == jvm
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onUpdateConfig(current.copy(javaVersion = jvm)) },
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (selected) McGold else Color(0xFF171E19),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) McGold else Color(0xFF253328))
                                ) {
                                    Text(
                                        text = jvm.split(" ").first() + " " + jvm.split(" ").getOrNull(1),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) McObsidian else Color.White,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Resolution Scaler
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Skala Resolusi Layar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "${current.resolutionScalePercent}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = McEmerald)
                        }
                        Text(
                            text = "Menurunkan ke 70%-85% meningkatkan FPS secara drastis pada layar FHD+/2K.",
                            fontSize = 10.sp,
                            color = McTextSecondary
                        )
                        Slider(
                            value = current.resolutionScalePercent.toFloat(),
                            onValueChange = { onUpdateConfig(current.copy(resolutionScalePercent = it.toInt())) },
                            valueRange = 50f..100f,
                            colors = SliderDefaults.colors(thumbColor = McEmerald, activeTrackColor = McEmerald)
                        )
                    }

                    // Max FPS
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "Batas Frame Rate (FPS)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        val fpsOptions = listOf(30, 60, 90, 120, 240)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            fpsOptions.forEach { fps ->
                                val selected = current.maxFps == fps
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onUpdateConfig(current.copy(maxFps = fps)) },
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (selected) McEmerald else Color(0xFF171E19),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) McEmerald else Color(0xFF253328))
                                ) {
                                    Text(
                                        text = if (fps == 240) "Max" else "$fps",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) McObsidian else Color.White,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. JVM ARGUMENTS & FLAGS
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = McSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Code, contentDescription = null, tint = McGold, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Argumen JVM (Flags)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    OutlinedTextField(
                        value = current.customJvmArgs,
                        onValueChange = { onUpdateConfig(current.copy(customJvmArgs = it)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = McTextPrimary
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = McEmerald,
                            unfocusedBorderColor = Color(0xFF2B3A2E)
                        )
                    )

                    // Quick Flags presets
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                onUpdateConfig(current.copy(customJvmArgs = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2B21), contentColor = McEmerald),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Aikar's G1GC", fontSize = 10.sp)
                        }

                        Button(
                            onClick = {
                                onUpdateConfig(current.copy(customJvmArgs = "-XX:+UseShenandoahGC -XX:+UnlockExperimentalVMOptions -XX:ShenandoahGCHeuristics=compact"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2B21), contentColor = McGold),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Shenandoah GC", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // 6. TOUCH CONTROLS CUSTOMIZATION (Pojav Virtual Keys)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = McSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3C30))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = McEmerald, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Kustomisasi Tombol Virtual Pojav",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Transparansi Tombol Layar", fontSize = 12.sp, color = McTextSecondary)
                        Text(text = "${(current.touchControlOpacity * 100).toInt()}%", fontSize = 12.sp, color = McEmerald)
                    }
                    Slider(
                        value = current.touchControlOpacity,
                        onValueChange = { onUpdateConfig(current.copy(touchControlOpacity = it)) },
                        valueRange = 0.3f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = McEmerald, activeTrackColor = McEmerald)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun PresetButton(
    title: String,
    sub: String,
    isSelected: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("preset_$title"),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFF223528) else Color(0xFF151C17),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) accentColor else Color(0xFF243026)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) accentColor else Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = sub,
                fontSize = 9.sp,
                color = McTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
