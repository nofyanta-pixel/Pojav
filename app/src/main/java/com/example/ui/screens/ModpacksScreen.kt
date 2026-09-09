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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.ModItemEntity
import com.example.data.local.ModpackEntity
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McRedstone
import com.example.ui.theme.McSurface
import com.example.ui.theme.McSurfaceVariant
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModpacksScreen(
    modpacks: List<ModpackEntity>,
    selectedModpack: ModpackEntity?,
    mods: List<ModItemEntity>,
    onSelectModpack: (Long) -> Unit,
    onCreateModpack: (name: String, version: String, loader: String, desc: String, ramMb: Int) -> Unit,
    onDeleteModpack: (Long) -> Unit,
    onToggleMod: (ModItemEntity) -> Unit,
    onAddMod: (modpackId: Long, name: String, fileName: String, category: String, version: String, desc: String) -> Unit,
    onDeleteMod: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreatePackDialog by remember { mutableStateOf(false) }
    var showAddModDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Title & Add Pack Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Manajer Modpack",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Kustomisasi mod Fabric, Forge, dan profil game",
                            fontSize = 12.sp,
                            color = McTextSecondary
                        )
                    }

                    Button(
                        onClick = { showCreatePackDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF243B2B),
                            contentColor = McEmerald
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("create_modpack_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Pack", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Buat Profil", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // MODPACK SELECTOR ROW / CARDS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    modpacks.forEach { pack ->
                        val isCurrent = pack.id == selectedModpack?.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectModpack(pack.id) }
                                .testTag("modpack_item_${pack.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) Color(0xFF1E2E23) else McSurface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isCurrent) 1.5.dp else 1.dp,
                                if (isCurrent) McEmerald else Color(0xFF2C3C30)
                            )
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
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isCurrent) McEmerald else Color(0xFF26362B)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Folder,
                                            contentDescription = "Folder",
                                            tint = if (isCurrent) McObsidian else Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = pack.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            if (isCurrent) {
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = McEmerald
                                                ) {
                                                    Text(
                                                        text = "AKTIF",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = McObsidian,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "${pack.loader} ${pack.mcVersion} • RAM: ${pack.memoryMb}MB",
                                            fontSize = 11.sp,
                                            color = if (isCurrent) McEmerald else McTextSecondary
                                        )
                                    }
                                }

                                if (isCurrent) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = McEmerald,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // TABS: MODS, SHADERS, RESOURCE PACKS
            item {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF141C16),
                    contentColor = McEmerald,
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Mod Terpasang (${mods.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Shaders (Iris)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Resource Packs", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            // TAB CONTENT
            when (selectedTab) {
                0 -> {
                    // TAB 0: MODS LIST
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Daftar Mod: ${selectedModpack?.name ?: ""}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = McEmerald
                            )

                            Button(
                                onClick = { showAddModDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = McEmerald,
                                    contentColor = McObsidian
                                ),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.testTag("add_mod_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Mod", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Pasang Mod (.jar)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (mods.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(McSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada mod di modpack ini. Ketuk 'Pasang Mod (.jar)' untuk menambahkan.",
                                    fontSize = 12.sp,
                                    color = McTextSecondary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(mods, key = { it.id }) { mod ->
                            ModItemCard(
                                mod = mod,
                                onToggle = { onToggleMod(mod) },
                                onDelete = { onDeleteMod(mod.id) }
                            )
                        }
                    }
                }

                1 -> {
                    // TAB 1: SHADERS
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Shaderpack Loader (Iris Shaders / GL4ES Support)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = McEmerald
                            )

                            ShaderPresetCard(
                                name = "Complementary Reimagined r5.1",
                                type = "PBR & Volumetric Lighting",
                                fpsImpact = "Medium (35-55 FPS on Adreno 7xx)",
                                isEnabled = true
                            )
                            ShaderPresetCard(
                                name = "BSL Shaders v8.2",
                                type = "Warm Cinematic Fantasy",
                                fpsImpact = "Medium-High",
                                isEnabled = false
                            )
                            ShaderPresetCard(
                                name = "MakeUp - Ultra Fast (Potato Friendly)",
                                type = "Optimized for Mobile GPU",
                                fpsImpact = "Light (60+ FPS)",
                                isEnabled = false
                            )
                        }
                    }
                }

                2 -> {
                    // TAB 2: RESOURCE PACKS
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Texture & Resource Packs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = McEmerald
                            )

                            ResourcePackCard(
                                name = "Faithful 32x HD",
                                resolution = "32x32",
                                desc = "Double resolution vanilla textures preserving the original style.",
                                isEnabled = true
                            )
                            ResourcePackCard(
                                name = "Bare Bones Texture Pack",
                                resolution = "16x16",
                                desc = "Minecraft official trailer aesthetic with flat vibrant blocks.",
                                isEnabled = false
                            )
                            ResourcePackCard(
                                name = "Fresh Animations v1.9",
                                resolution = "Custom Entity Models",
                                desc = "Dynamic breathing and blinking animations for all vanilla mobs.",
                                isEnabled = true
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    // DIALOG: CREATE NEW MODPACK
    if (showCreatePackDialog) {
        CreateModpackDialog(
            onDismiss = { showCreatePackDialog = false },
            onConfirm = { name, ver, loader, desc, ram ->
                onCreateModpack(name, ver, loader, desc, ram)
                showCreatePackDialog = false
            }
        )
    }

    // DIALOG: ADD NEW MOD (.jar)
    if (showAddModDialog && selectedModpack != null) {
        AddModDialog(
            modpackName = selectedModpack.name,
            onDismiss = { showAddModDialog = false },
            onConfirm = { name, fileName, cat, ver, desc ->
                onAddMod(selectedModpack.id, name, fileName, cat, ver, desc)
                showAddModDialog = false
            }
        )
    }
}

@Composable
private fun ModItemCard(
    mod: ModItemEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mod_item_${mod.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (mod.isEnabled) McSurface else Color(0xFF141915)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (mod.isEnabled) Color(0xFF2D3E32) else Color(0xFF1E2821)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = mod.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (mod.isEnabled) Color.White else Color(0xFF7D8C81)
                    )

                    // Category Tag
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (mod.category) {
                            "Performance" -> Color(0xFF163E24)
                            "Graphics" -> Color(0xFF1E3A5F)
                            "Memory" -> Color(0xFF4C1D24)
                            else -> Color(0xFF2C3E50)
                        }
                    ) {
                        Text(
                            text = mod.category,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (mod.category) {
                                "Performance" -> McEmerald
                                "Graphics" -> McDiamond
                                "Memory" -> McRedstone
                                else -> McGold
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = mod.fileName,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF7A8B7E)
                )

                Text(
                    text = mod.description,
                    fontSize = 11.sp,
                    color = McTextSecondary,
                    maxLines = 1
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Switch(
                    checked = mod.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = McEmerald,
                        checkedTrackColor = Color(0xFF1B4329),
                        uncheckedThumbColor = Color(0xFF55665B),
                        uncheckedTrackColor = Color(0xFF18221B)
                    )
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF884444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ShaderPresetCard(
    name: String,
    type: String,
    fpsImpact: String,
    isEnabled: Boolean
) {
    var active by remember { mutableStateOf(isEnabled) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = McSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2B3A2F))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "$type • $fpsImpact", fontSize = 11.sp, color = McDiamond)
            }

            Switch(
                checked = active,
                onCheckedChange = { active = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = McDiamond,
                    checkedTrackColor = Color(0xFF133E4D)
                )
            )
        }
    }
}

@Composable
private fun ResourcePackCard(
    name: String,
    resolution: String,
    desc: String,
    isEnabled: Boolean
) {
    var active by remember { mutableStateOf(isEnabled) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = McSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2B3A2F))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF2C3930)) {
                        Text(text = resolution, fontSize = 9.sp, color = McGold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                }
                Text(text = desc, fontSize = 11.sp, color = McTextSecondary, maxLines = 1)
            }

            Switch(
                checked = active,
                onCheckedChange = { active = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = McEmerald,
                    checkedTrackColor = Color(0xFF1E3A28)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateModpackDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, version: String, loader: String, desc: String, ramMb: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedVersion by remember { mutableStateOf("1.20.4") }
    var selectedLoader by remember { mutableStateOf("Fabric") }
    var ramAllocationMb by remember { mutableIntStateOf(2560) }
    var description by remember { mutableStateOf("") }

    val versions = listOf("1.21", "1.20.4", "1.20.1", "1.19.2", "1.16.5", "1.12.2")
    val loaders = listOf("Fabric", "Forge", "NeoForge", "Quilt", "Vanilla")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Buat Profil Modpack Baru", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Modpack") },
                    placeholder = { Text("e.g. My Survival FPS Boost") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = McEmerald,
                        focusedLabelColor = McEmerald
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Version dropdown
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Versi MC", fontSize = 11.sp, color = McTextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            versions.take(3).forEach { v ->
                                Surface(
                                    modifier = Modifier.clickable { selectedVersion = v },
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (selectedVersion == v) McEmerald else Color(0xFF1E2821)
                                ) {
                                    Text(
                                        text = v,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedVersion == v) McObsidian else Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Loader buttons
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mod Loader", fontSize = 11.sp, color = McTextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            loaders.take(3).forEach { l ->
                                Surface(
                                    modifier = Modifier.clickable { selectedLoader = l },
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (selectedLoader == l) McDiamond else Color(0xFF1E2821)
                                ) {
                                    Text(
                                        text = l,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedLoader == l) McObsidian else Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Column {
                    Text(
                        text = "Alokasi RAM: $ramAllocationMb MB",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = McGold
                    )
                    Slider(
                        value = ramAllocationMb.toFloat(),
                        onValueChange = { ramAllocationMb = (it / 256).toInt() * 256 },
                        valueRange = 1024f..6144f,
                        colors = SliderDefaults.colors(
                            thumbColor = McGold,
                            activeTrackColor = McGold
                        )
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi Modpack (Opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        name.ifEmpty { "Modpack ${selectedLoader} ${selectedVersion}" },
                        selectedVersion,
                        selectedLoader,
                        description,
                        ramAllocationMb
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = McEmerald, contentColor = McObsidian)
            ) {
                Text("Buat Modpack", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = Color(0xFFAAAAAA)) }
        },
        containerColor = Color(0xFF151D17)
    )
}

@Composable
private fun AddModDialog(
    modpackName: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, fileName: String, category: String, version: String, desc: String) -> Unit
) {
    var modName by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Performance") }
    var version by remember { mutableStateOf("1.0.0") }
    var description by remember { mutableStateOf("") }

    val categories = listOf("Performance", "Graphics", "Memory", "Utility", "Gameplay")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Pasang Mod ke $modpackName", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = modName,
                    onValueChange = {
                        modName = it
                        if (fileName.isEmpty()) {
                            fileName = "${it.lowercase().replace(" ", "-")}.jar"
                        }
                    },
                    label = { Text("Nama Mod") },
                    placeholder = { Text("e.g. ImmediatelyFast") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Nama File (.jar)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text("Kategori Mod", fontSize = 11.sp, color = McTextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.take(3).forEach { cat ->
                            Surface(
                                modifier = Modifier.clickable { category = cat },
                                shape = RoundedCornerShape(4.dp),
                                color = if (category == cat) McEmerald else Color(0xFF1F2922)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    color = if (category == cat) McObsidian else Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Keterangan Singkat") },
                    placeholder = { Text("e.g. Mempercepat render HUD dan GUI") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (modName.isNotEmpty()) {
                        onConfirm(modName, fileName, category, version, description)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = McEmerald, contentColor = McObsidian)
            ) {
                Text("Tambah Mod", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = Color(0xFFAAAAAA)) }
        },
        containerColor = Color(0xFF151D17)
    )
}
