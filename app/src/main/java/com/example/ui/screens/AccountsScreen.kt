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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
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
import com.example.data.local.AccountEntity
import com.example.data.model.CapePresets
import com.example.data.model.SkinPresets
import com.example.ui.components.MinecraftCharacterViewer
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
fun AccountsScreen(
    accounts: List<AccountEntity>,
    activeAccount: AccountEntity?,
    onCreateAccount: (username: String, type: String, model: String, skinId: String, capeId: String) -> Unit,
    onSwitchAccount: (Long) -> Unit,
    onDeleteAccount: (Long) -> Unit,
    onUpdateSkinAndCape: (AccountEntity, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Skin & Cape Customizer, 1: Daftar Akun

    // Active skin and cape from active account
    val currentSkinId = activeAccount?.skinPresetId ?: "steve"
    val currentCapeId = activeAccount?.capePresetId ?: "migrator"
    val currentModel = activeAccount?.skinModel ?: "STEVE"

    val activeSkin = SkinPresets.getById(currentSkinId)
    val activeCape = CapePresets.getById(currentCapeId)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Akun Offline / Local",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Kustomisasi skin 3D, jubah (cape), dan kelola akun crack",
                        fontSize = 12.sp,
                        color = McTextSecondary
                    )
                }

                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = McEmerald,
                        contentColor = McObsidian
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("create_account_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Account", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Akun Baru", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // TABS: Customizer vs Accounts List
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
                    text = { Text("Kustomisasi Skin & Cape", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Daftar Akun (${accounts.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        if (selectedTab == 0) {
            // TAB 0: LIVE 3D / 2D SKIN & CAPE VIEWER
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Active user summary badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = McGold, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Akun Aktif: ${activeAccount?.username ?: "Player"}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Model Toggle: Steve vs Alex
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1B241E))
                                .border(1.dp, Color(0xFF2E3E32), RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "STEVE (4px)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentModel == "STEVE") McObsidian else Color.White,
                                modifier = Modifier
                                    .clickable {
                                        activeAccount?.let { onUpdateSkinAndCape(it, currentSkinId, currentCapeId, "STEVE") }
                                    }
                                    .background(if (currentModel == "STEVE") McEmerald else Color.Transparent)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                            Text(
                                text = "ALEX (3px)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentModel == "ALEX") McObsidian else Color.White,
                                modifier = Modifier
                                    .clickable {
                                        activeAccount?.let { onUpdateSkinAndCape(it, currentSkinId, currentCapeId, "ALEX") }
                                    }
                                    .background(if (currentModel == "ALEX") McEmerald else Color.Transparent)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // The Interactive Canvas Viewer
                    MinecraftCharacterViewer(
                        skin = activeSkin,
                        cape = activeCape,
                        height = 280.dp,
                        showControls = true,
                        modifier = Modifier.testTag("character_viewer_canvas")
                    )
                }
            }

            // SKIN PRESET SELECTOR
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Pilih Preset Skin Minecraft",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = McEmerald
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(SkinPresets.allPresets) { preset ->
                            val isSelected = preset.id == currentSkinId
                            Card(
                                modifier = Modifier
                                    .width(130.dp)
                                    .clickable {
                                        activeAccount?.let {
                                            onUpdateSkinAndCape(it, preset.id, currentCapeId, preset.model)
                                        }
                                    }
                                    .testTag("skin_preset_${preset.id}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF1E2E23) else McSurface
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) McEmerald else Color(0xFF2C3A30)
                                )
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    // Mini Head representation
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(preset.headSkinColor)
                                            .border(1.dp, preset.hairColor, RoundedCornerShape(6.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp, 8.dp)
                                                .background(preset.torsoColor)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = preset.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) McGold else Color.White,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = preset.model,
                                        fontSize = 10.sp,
                                        color = McTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // CAPE PRESET SELECTOR
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Pilih Jubah (Cape)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = McGold
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(CapePresets.allPresets) { cape ->
                            val isSelected = cape.id == currentCapeId
                            Card(
                                modifier = Modifier
                                    .width(140.dp)
                                    .clickable {
                                        activeAccount?.let {
                                            onUpdateSkinAndCape(it, currentSkinId, cape.id, currentModel)
                                        }
                                    }
                                    .testTag("cape_preset_${cape.id}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF2A281E) else McSurface
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) McGold else Color(0xFF2C3A30)
                                )
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    // Cape miniature badge
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp, 38.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(cape.baseColor)
                                            .border(1.dp, cape.borderCol, RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(cape.emblemColor)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = cape.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) McGold else Color.White,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = cape.description,
                                        fontSize = 9.sp,
                                        color = McTextSecondary,
                                        maxLines = 2,
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

        } else {
            // TAB 1: LIST OF ACCOUNTS
            items(accounts, key = { it.id }) { acc ->
                val isActive = acc.id == activeAccount?.id
                val skin = SkinPresets.getById(acc.skinPresetId)
                val cape = CapePresets.getById(acc.capePresetId)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSwitchAccount(acc.id) }
                        .testTag("account_item_${acc.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) Color(0xFF1E2E23) else McSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isActive) 1.5.dp else 1.dp,
                        if (isActive) McEmerald else Color(0xFF2D3D32)
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
                            // Avatar block
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(skin.headSkinColor)
                                    .border(1.5.dp, skin.hairColor, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp, 8.dp)
                                        .background(skin.eyeColor)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = acc.username,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    if (isActive) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = McEmerald
                                        ) {
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

                                Text(
                                    text = "Tipe: ${acc.accountType} • Skin: ${skin.name} (${acc.skinModel})",
                                    fontSize = 11.sp,
                                    color = McTextSecondary
                                )
                                Text(
                                    text = "Jubah: ${cape.name} • UUID: ${acc.uuid}",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF6B7E72)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!isActive) {
                                Button(
                                    onClick = { onSwitchAccount(acc.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF263C2E),
                                        contentColor = McEmerald
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Pilih", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (accounts.size > 1) {
                                IconButton(onClick = { onDeleteAccount(acc.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = Color(0xFF884444),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }

    // CREATE ACCOUNT DIALOG (Support Local / Offline / Cracked)
    if (showCreateDialog) {
        CreateAccountDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { username, type, model, skinId, capeId ->
                onCreateAccount(username, type, model, skinId, capeId)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun CreateAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: (username: String, type: String, model: String, skinId: String, capeId: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("OFFLINE") } // OFFLINE or CRACKED
    var model by remember { mutableStateOf("STEVE") } // STEVE or ALEX
    var selectedSkinId by remember { mutableStateOf("steve") }
    var selectedCapeId by remember { mutableStateOf("migrator") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Buat Akun Offline / Crack Baru",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Bisa login tanpa akun Microsoft atau koneksi internet.",
                    fontSize = 11.sp,
                    color = McEmerald
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username Minecraft (Nickname)") },
                    placeholder = { Text("e.g. PojavCrafter99") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = McEmerald,
                        focusedLabelColor = McEmerald
                    )
                )

                // Account Type Selector
                Column {
                    Text("Tipe Akun", fontSize = 11.sp, color = McTextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { accountType = "OFFLINE" },
                            shape = RoundedCornerShape(6.dp),
                            color = if (accountType == "OFFLINE") McEmerald else Color(0xFF1E2821)
                        ) {
                            Text(
                                text = "OFFLINE / LOCAL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (accountType == "OFFLINE") McObsidian else Color.White,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { accountType = "CRACKED" },
                            shape = RoundedCornerShape(6.dp),
                            color = if (accountType == "CRACKED") McGold else Color(0xFF1E2821)
                        ) {
                            Text(
                                text = "CRACKED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (accountType == "CRACKED") McObsidian else Color.White,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                // Arm Model: Steve (Classic) or Alex (Slim)
                Column {
                    Text("Model Lengan", fontSize = 11.sp, color = McTextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { model = "STEVE" },
                            shape = RoundedCornerShape(6.dp),
                            color = if (model == "STEVE") McDiamond else Color(0xFF1E2821)
                        ) {
                            Text(
                                text = "Steve (4px Classic)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (model == "STEVE") McObsidian else Color.White,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { model = "ALEX" },
                            shape = RoundedCornerShape(6.dp),
                            color = if (model == "ALEX") McDiamond else Color(0xFF1E2821)
                        ) {
                            Text(
                                text = "Alex (3px Slim)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (model == "ALEX") McObsidian else Color.White,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotBlank()) {
                        onConfirm(username, accountType, model, selectedSkinId, selectedCapeId)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = McEmerald, contentColor = McObsidian)
            ) {
                Text("Simpan Akun", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = Color(0xFFAAAAAA)) }
        },
        containerColor = Color(0xFF151D17)
    )
}
