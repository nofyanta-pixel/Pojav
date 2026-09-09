package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McObsidian
import com.example.ui.theme.McRedstone

data class ConsoleLogLine(
    val level: String, // "INFO", "WARN", "MOD", "BOOT"
    val tag: String,
    val message: String,
    val timestamp: String
)

@Composable
fun PojavConsoleLogs(
    logs: List<ConsoleLogLine>,
    progress: Float,
    statusText: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0A0E0B))
            .border(1.dp, Color(0xFF1E2821), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Console",
                    tint = McEmerald,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "PojavLauncher Java Console",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = McEmerald
                )
            }

            if (progress < 1.0f) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = McEmerald
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = McEmerald,
            trackColor = Color(0xFF1E2821)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = statusText,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = McGold,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Log list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF060907), RoundedCornerShape(6.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(logs) { line ->
                val color = when (line.level) {
                    "WARN" -> McGold
                    "ERROR" -> McRedstone
                    "MOD" -> McDiamond
                    else -> Color(0xFFB5C7B8)
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "[${line.timestamp}] ",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF5A6E5F)
                    )
                    Text(
                        text = "[${line.tag}/${line.level}] ",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = when (line.level) {
                            "WARN" -> McGold
                            "ERROR" -> McRedstone
                            "MOD" -> McEmerald
                            else -> Color(0xFF7BA684)
                        }
                    )
                    Text(
                        text = line.message,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = color
                    )
                }
            }
        }
    }
}
