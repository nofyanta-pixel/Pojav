package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McRedstone
import kotlin.math.roundToInt

@Composable
fun PojavVirtualControls(
    opacity: Float,
    isF3Active: Boolean,
    onToggleF3: () -> Unit,
    onToggleF5: () -> Unit,
    onOpenInventory: () -> Unit,
    onOpenChat: () -> Unit,
    onPauseGame: () -> Unit,
    onPrimaryAction: (String) -> Unit, // Attack/Break
    onSecondaryAction: (String) -> Unit, // Place/Use
    onMove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMouseActive by remember { mutableStateOf(false) }
    var mousePos by remember { mutableStateOf(Offset(300f, 300f)) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(isMouseActive) {
                if (isMouseActive) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        mousePos = Offset(
                            (mousePos.x + dragAmount.x).coerceIn(20f, size.width.toFloat() - 20f),
                            (mousePos.y + dragAmount.y).coerceIn(20f, size.height.toFloat() - 20f)
                        )
                    }
                }
            }
    ) {
        val density = LocalDensity.current

        // 1. TOP BAR FUNCTION BUTTONS (ESC, INV, F3, F5, CHAT, KBD, MOUSE)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 12.dp, end = 12.dp)
                .alpha(opacity),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left quick buttons
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PojavControlKey(
                    label = "ESC",
                    color = McRedstone.copy(alpha = 0.85f),
                    onClick = onPauseGame
                )
                PojavControlKey(
                    label = "INV",
                    color = Color(0xFF2C3E50),
                    onClick = onOpenInventory
                )
                PojavControlKey(
                    label = "F3",
                    isActive = isF3Active,
                    onClick = onToggleF3
                )
                PojavControlKey(
                    label = "F5",
                    onClick = onToggleF5
                )
            }

            // Right quick buttons
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PojavControlKey(
                    label = "CHAT",
                    icon = Icons.Default.Chat,
                    onClick = onOpenChat
                )
                PojavControlKey(
                    label = "MOUSE",
                    isActive = isMouseActive,
                    icon = Icons.Default.Mouse,
                    onClick = { isMouseActive = !isMouseActive }
                )
                PojavControlKey(
                    label = "KBD",
                    icon = Icons.Default.Keyboard,
                    onClick = onOpenChat
                )
            }
        }

        // 2. LEFT SIDE: D-PAD (FORWARD, LEFT, BACKWARD, RIGHT, SNEAK)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 24.dp)
                .size(160.dp)
                .alpha(opacity)
        ) {
            // Forward (W)
            PojavDpadButton(
                icon = Icons.Default.KeyboardArrowUp,
                label = "W",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(52.dp),
                onClick = { onMove("FORWARD") }
            )
            // Left (A)
            PojavDpadButton(
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                label = "A",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(52.dp),
                onClick = { onMove("LEFT") }
            )
            // Backward (S)
            PojavDpadButton(
                icon = Icons.Default.KeyboardArrowDown,
                label = "S",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(52.dp),
                onClick = { onMove("BACK") }
            )
            // Right (D)
            PojavDpadButton(
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                label = "D",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(52.dp),
                onClick = { onMove("RIGHT") }
            )
            // Center (Sneak / Crouch)
            PojavDpadButton(
                icon = null,
                label = "SHIFT",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(46.dp),
                isCenter = true,
                onClick = { onMove("SNEAK") }
            )
        }

        // 3. RIGHT SIDE: ACTION BUTTONS (PRI, SEC, JUMP)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 24.dp)
                .size(170.dp)
                .alpha(opacity)
        ) {
            // PRI (Attack / Break / Left Click)
            PojavActionButton(
                label = "PRI",
                subLabel = "Attack",
                bgColor = Color(0xBB991B1B),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(64.dp),
                onClick = { onPrimaryAction("ATTACK") }
            )

            // SEC (Place / Use / Right Click)
            PojavActionButton(
                label = "SEC",
                subLabel = "Use",
                bgColor = Color(0xBB1E40AF),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(64.dp),
                onClick = { onSecondaryAction("USE") }
            )

            // JUMP (Spacebar)
            PojavActionButton(
                label = "JUMP",
                subLabel = "Space",
                bgColor = Color(0xBB166534),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(70.dp),
                onClick = { onMove("JUMP") }
            )
        }

        // 4. VIRTUAL MOUSE CURSOR (if active)
        if (isMouseActive) {
            Icon(
                imageVector = Icons.Default.NearMe,
                contentDescription = "Mouse Cursor",
                tint = McGold,
                modifier = Modifier
                    .offset { IntOffset(mousePos.x.roundToInt(), mousePos.y.roundToInt()) }
                    .size(24.dp)
            )
        }
    }
}

@Composable
private fun PojavControlKey(
    label: String,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    color: Color = Color(0xCC1E2922),
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit
) {
    val bg = if (isActive) McEmerald.copy(alpha = 0.85f) else color
    val contentColor = if (isActive) Color(0xFF0D140E) else Color.White

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(6.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x44FFFFFF))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(
                text = label,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Composable
private fun PojavDpadButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    label: String,
    modifier: Modifier = Modifier,
    isCenter: Boolean = false,
    onClick: () -> Unit
) {
    val bg = if (isCenter) Color(0xCC374151) else Color(0xAA1F2937)

    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0x55FFFFFF))
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun PojavActionButton(
    label: String,
    subLabel: String,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0x88FFFFFF))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = subLabel,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xCCFFFFFF)
            )
        }
    }
}
