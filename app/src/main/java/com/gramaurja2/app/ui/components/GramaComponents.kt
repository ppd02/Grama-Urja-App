package com.gramaurja2.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.PowerStatus
import com.gramaurja2.app.domain.model.Zone
import com.gramaurja2.app.ui.theme.Alert
import com.gramaurja2.app.ui.theme.Forest
import com.gramaurja2.app.ui.theme.ForestDark
import com.gramaurja2.app.ui.theme.Mint
import com.gramaurja2.app.ui.theme.Sky
import com.gramaurja2.app.ui.theme.Solar

@Composable
fun FuturisticBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    )
                )
            )
    ) { content() }
}

@Composable
fun GramaLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(Color(0xFFBFFFE6), Sky, ForestDark)))
            .border(1.dp, Color.White.copy(alpha = 0.65f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(Color.White.copy(alpha = 0.16f), radius = size.minDimension * 0.42f)
            drawArc(Color.White, 205f, 310f, false, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
            drawLine(Solar, Offset(size.width * 0.57f, size.height * 0.16f), Offset(size.width * 0.36f, size.height * 0.55f), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
            drawLine(Solar, Offset(size.width * 0.36f, size.height * 0.55f), Offset(size.width * 0.61f, size.height * 0.49f), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
            drawLine(Solar, Offset(size.width * 0.61f, size.height * 0.49f), Offset(size.width * 0.43f, size.height * 0.83f), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
        }
    }
}

@Composable
fun HeroHeader(title: String, subtitle: String, live: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))
            .background(Brush.horizontalGradient(listOf(ForestDark, Forest, Sky)))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GramaLogo(Modifier.size(62.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = Mint, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
        StatusPill(live, Color(0xFF72F0B0))
    }
}

@Composable
fun GramaCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Composable
fun LargePowerButton(text: String, status: PowerStatus, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val color = if (status == PowerStatus.ON) Forest else Alert
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        onClick = onClick
    ) {
        Text(text, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun PrimaryAction(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
fun SecondaryAction(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(modifier = modifier.height(54.dp), shape = RoundedCornerShape(16.dp), onClick = onClick) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatusPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, color = color, style = MaterialTheme.typography.labelMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun ZoneChips(zones: List<Zone>, selected: Zone, language: Language, onSelect: (Zone) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        zones.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { zone ->
                    FilterChip(
                        selected = selected.id == zone.id,
                        onClick = { onSelect(zone) },
                        label = { Text(zone.displayName(language), maxLines = 2, overflow = TextOverflow.Ellipsis) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatusIcon(status: PowerStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        PowerStatus.ON -> Forest
        PowerStatus.OFF -> Alert
        PowerStatus.UNKNOWN -> Solar
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (status) {
                PowerStatus.ON -> "ON"
                PowerStatus.OFF -> "OFF"
                PowerStatus.UNKNOWN -> "?"
            },
            color = color,
            fontWeight = FontWeight.Black
        )
    }
}
