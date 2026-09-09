package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CapePreset
import com.example.data.model.SkinPreset
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McSurface

@Composable
fun MinecraftCharacterViewer(
    skin: SkinPreset,
    cape: CapePreset,
    modifier: Modifier = Modifier,
    height: Dp = 260.dp,
    showControls: Boolean = true
) {
    var isBackView by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "CapeFlap")
    val capeWave by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "capeWave"
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF141A16),
                        Color(0xFF0C100D)
                    )
                )
            )
            .border(1.dp, Color(0xFF2E3D32), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Grid texture subtle background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 20.dp.toPx()
            for (x in 0..(size.width / step).toInt()) {
                drawLine(
                    color = Color(0x0CFFFFFF),
                    start = Offset(x * step, 0f),
                    end = Offset(x * step, size.height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..(size.height / step).toInt()) {
                drawLine(
                    color = Color(0x0CFFFFFF),
                    start = Offset(0f, y * step),
                    end = Offset(size.width, y * step),
                    strokeWidth = 1f
                )
            }
        }

        // Minecraft Player Body Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clickable { isBackView = !isBackView }
        ) {
            val centerX = size.width / 2f
            val startY = size.height * 0.12f

            // Scale unit (1 Minecraft pixel block in px)
            val u = size.height / 36f

            val isAlex = skin.model.equals("ALEX", ignoreCase = true)
            val armWidthU = if (isAlex) 3f else 4f

            // Shadow on floor
            drawOval(
                color = Color(0x44000000),
                topLeft = Offset(centerX - 8 * u, startY + 28.5f * u),
                size = Size(16 * u, 3.5f * u)
            )

            if (!isBackView) {
                // FRONT VIEW
                // 1. Cape visible on sides if equipped
                if (cape.id != "none") {
                    drawCapeSides(cape, centerX, startY, u, capeWave)
                }

                // 2. Legs (Left Leg and Right Leg: each 4 wide x 12 tall)
                val leftLegX = centerX - 4 * u
                val rightLegX = centerX
                val legY = startY + 16 * u
                drawPixelBox(skin.legColor, leftLegX, legY, 3.9f * u, 12 * u)
                drawPixelBox(skin.legColor, rightLegX, legY, 3.9f * u, 12 * u)
                // Shoes accent
                drawPixelBox(skin.legColor.copy(alpha = 0.6f), leftLegX, legY + 9 * u, 3.9f * u, 3 * u)
                drawPixelBox(skin.legColor.copy(alpha = 0.6f), rightLegX, legY + 9 * u, 3.9f * u, 3 * u)

                // 3. Torso (8 wide x 12 tall)
                val torsoX = centerX - 4 * u
                val torsoY = startY + 8 * u
                drawPixelBox(skin.torsoColor, torsoX, torsoY, 8 * u, 12 * u)
                // Torso accent / belt detail
                drawPixelBox(skin.accentColor, torsoX + 1.5f * u, torsoY + 9 * u, 5 * u, 1.5f * u)

                // 4. Arms (Left Arm and Right Arm)
                val leftArmX = torsoX - armWidthU * u
                val rightArmX = torsoX + 8 * u
                val armY = torsoY
                drawPixelBox(skin.armColor, leftArmX, armY, armWidthU * u - 0.5f, 12 * u)
                drawPixelBox(skin.armColor, rightArmX + 0.5f, armY, armWidthU * u - 0.5f, 12 * u)
                // Sleeves
                drawPixelBox(skin.torsoColor, leftArmX, armY, armWidthU * u - 0.5f, 4 * u)
                drawPixelBox(skin.torsoColor, rightArmX + 0.5f, armY, armWidthU * u - 0.5f, 4 * u)

                // 5. Head (8 wide x 8 tall)
                val headX = centerX - 4 * u
                val headY = startY
                drawPixelBox(skin.headSkinColor, headX, headY, 8 * u, 8 * u)

                // Hair (Top and sides)
                drawPixelBox(skin.hairColor, headX, headY, 8 * u, 2.5f * u)
                drawPixelBox(skin.hairColor, headX, headY + 2.5f * u, 1.5f * u, 2f * u)
                drawPixelBox(skin.hairColor, headX + 6.5f * u, headY + 2.5f * u, 1.5f * u, 2f * u)

                // Eyes
                val eyeY = headY + 3.5f * u
                // Left eye (white + iris)
                drawPixelBox(Color.White, headX + 1.5f * u, eyeY, 2 * u, 1.2f * u)
                drawPixelBox(skin.eyeColor, headX + 2.3f * u, eyeY, 1.2f * u, 1.2f * u)
                // Right eye
                drawPixelBox(Color.White, headX + 4.5f * u, eyeY, 2 * u, 1.2f * u)
                drawPixelBox(skin.eyeColor, headX + 4.5f * u, eyeY, 1.2f * u, 1.2f * u)

                // Mouth
                drawPixelBox(skin.hairColor.copy(alpha = 0.5f), headX + 3 * u, headY + 6 * u, 2 * u, 0.8f * u)

            } else {
                // BACK VIEW (SHOWCASE CAPE!)
                val leftLegX = centerX - 4 * u
                val rightLegX = centerX
                val legY = startY + 16 * u
                drawPixelBox(skin.legColor, leftLegX, legY, 3.9f * u, 12 * u)
                drawPixelBox(skin.legColor, rightLegX, legY, 3.9f * u, 12 * u)

                val torsoX = centerX - 4 * u
                val torsoY = startY + 8 * u
                val armWidth = if (isAlex) 3f else 4f
                val leftArmX = torsoX - armWidth * u
                val rightArmX = torsoX + 8 * u
                val armY = torsoY

                // Arms back
                drawPixelBox(skin.armColor, leftArmX, armY, armWidth * u - 0.5f, 12 * u)
                drawPixelBox(skin.armColor, rightArmX + 0.5f, armY, armWidth * u - 0.5f, 12 * u)
                drawPixelBox(skin.torsoColor, leftArmX, armY, armWidth * u - 0.5f, 4 * u)
                drawPixelBox(skin.torsoColor, rightArmX + 0.5f, armY, armWidth * u - 0.5f, 4 * u)

                // Head Back
                val headX = centerX - 4 * u
                val headY = startY
                drawPixelBox(skin.hairColor, headX, headY, 8 * u, 8 * u)

                // Cape (Over torso and back)
                if (cape.id != "none") {
                    drawFullBackCape(cape, centerX, torsoY - 0.5f * u, u, capeWave)
                } else {
                    // Plain back torso
                    drawPixelBox(skin.torsoColor, torsoX, torsoY, 8 * u, 12 * u)
                }
            }
        }

        // Overlay control pills
        if (showControls) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .clickable { isBackView = !isBackView },
                shape = RoundedCornerShape(20.dp),
                color = McSurface.copy(alpha = 0.9f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334537))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = "Rotate",
                        tint = McEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isBackView) "Tampak Belakang (Jubah)" else "Tampak Depan (Skin)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            // Top badge: Model Steve / Alex
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD1B241E)
            ) {
                Text(
                    text = "${skin.model} • ${if (skin.model == "ALEX") "3px" else "4px"}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = McGold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private fun DrawScope.drawPixelBox(
    color: Color,
    x: Float,
    y: Float,
    w: Float,
    h: Float
) {
    drawRect(
        color = color,
        topLeft = Offset(x, y),
        size = Size(w, h)
    )
}

private fun DrawScope.drawCapeSides(
    cape: CapePreset,
    centerX: Float,
    startY: Float,
    u: Float,
    wave: Float
) {
    val capeY = startY + 8 * u
    // Show edges of cape billowing in the breeze
    val capeWidth = 9.5f * u + wave * 0.2f
    val capeHeight = 15f * u
    drawRect(
        color = cape.borderCol,
        topLeft = Offset(centerX - capeWidth / 2f, capeY),
        size = Size(capeWidth, capeHeight)
    )
    drawRect(
        color = cape.baseColor,
        topLeft = Offset(centerX - (capeWidth - 1 * u) / 2f, capeY + 0.5f * u),
        size = Size(capeWidth - 1 * u, capeHeight - 1 * u)
    )
}

private fun DrawScope.drawFullBackCape(
    cape: CapePreset,
    centerX: Float,
    capeY: Float,
    u: Float,
    wave: Float
) {
    val capeW = 9.2f * u
    val capeH = 15f * u
    val capeX = centerX - capeW / 2f + wave * 0.4f

    // Outer border
    drawRoundRect(
        color = cape.borderCol,
        topLeft = Offset(capeX, capeY),
        size = Size(capeW, capeH),
        cornerRadius = CornerRadius(2f, 2f)
    )

    // Base color fill
    drawRect(
        color = cape.baseColor,
        topLeft = Offset(capeX + 0.8f * u, capeY + 0.8f * u),
        size = Size(capeW - 1.6f * u, capeH - 1.6f * u)
    )

    // Cape Insignia / Emblem
    val emblemX = capeX + capeW / 2f
    val emblemY = capeY + 5f * u

    when (cape.emblemType) {
        "STAR" -> {
            // Golden Mojang / Migrator Compass Star
            drawCircle(cape.emblemColor, radius = 2.2f * u, center = Offset(emblemX, emblemY))
            drawRect(
                color = cape.baseColor,
                topLeft = Offset(emblemX - 0.7f * u, emblemY - 0.7f * u),
                size = Size(1.4f * u, 1.4f * u)
            )
            // 4 points
            drawLine(cape.emblemColor, Offset(emblemX - 3.2f * u, emblemY), Offset(emblemX + 3.2f * u, emblemY), strokeWidth = 2f)
            drawLine(cape.emblemColor, Offset(emblemX, emblemY - 3.2f * u), Offset(emblemX, emblemY + 3.2f * u), strokeWidth = 2f)
        }
        "CREEPER" -> {
            // Minecon Creeper face
            val crX = emblemX - 1.8f * u
            val crY = emblemY - 1.8f * u
            // Eyes
            drawRect(cape.emblemColor, Offset(crX, crY), Size(1.1f * u, 1.1f * u))
            drawRect(cape.emblemColor, Offset(crX + 2.5f * u, crY), Size(1.1f * u, 1.1f * u))
            // Mouth
            drawRect(cape.emblemColor, Offset(crX + 1.1f * u, crY + 1.2f * u), Size(1.4f * u, 1.8f * u))
            drawRect(cape.emblemColor, Offset(crX + 0.4f * u, crY + 2.0f * u), Size(0.8f * u, 1.4f * u))
            drawRect(cape.emblemColor, Offset(crX + 2.4f * u, crY + 2.0f * u), Size(0.8f * u, 1.4f * u))
        }
        "PICKAXE" -> {
            // Diagonal pickaxe head
            drawLine(cape.emblemColor, Offset(emblemX - 2.5f * u, emblemY - 2.5f * u), Offset(emblemX + 2.5f * u, emblemY - 2.5f * u), strokeWidth = 4f)
            drawLine(cape.emblemColor.copy(alpha = 0.7f), Offset(emblemX, emblemY - 2.5f * u), Offset(emblemX, emblemY + 3.5f * u), strokeWidth = 3f)
        }
        "OPTIFINE" -> {
            // "OF" banner
            drawRect(cape.emblemColor, Offset(emblemX - 2.5f * u, emblemY - 2f * u), Size(2.2f * u, 3.5f * u))
            drawRect(cape.baseColor, Offset(emblemX - 1.8f * u, emblemY - 1.2f * u), Size(0.8f * u, 1.9f * u))
            drawRect(cape.emblemColor, Offset(emblemX + 0.5f * u, emblemY - 2f * u), Size(2.2f * u, 3.5f * u))
            drawRect(cape.baseColor, Offset(emblemX + 1.2f * u, emblemY - 1.2f * u), Size(1.5f * u, 1.0f * u))
            drawRect(cape.baseColor, Offset(emblemX + 1.2f * u, emblemY + 0.5f * u), Size(1.5f * u, 1.0f * u))
        }
        "CHERRY" -> {
            // Petals
            drawCircle(Color.White, radius = 1.2f * u, center = Offset(emblemX - 1f * u, emblemY - 1f * u))
            drawCircle(Color.White, radius = 1.2f * u, center = Offset(emblemX + 1f * u, emblemY - 0.5f * u))
            drawCircle(Color.White, radius = 1.0f * u, center = Offset(emblemX, emblemY + 1.5f * u))
            drawCircle(Color(0xFFFFD1DC), radius = 0.6f * u, center = Offset(emblemX, emblemY))
        }
        else -> {
            // Classic shield / gem symbol
            drawCircle(cape.emblemColor, radius = 1.8f * u, center = Offset(emblemX, emblemY))
        }
    }
}
