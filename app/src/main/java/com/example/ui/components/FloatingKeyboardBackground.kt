package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonOrangePrimary
import com.example.ui.theme.PureGold
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class FloatingKeyItem(
    val label: String,
    val initialXRatio: Float,
    val initialYRatio: Float,
    val speedX: Float,
    val speedY: Float,
    val sizeDp: Float,
    val colorType: Int, // 0: Cyan, 1: Orange, 2: Gold
    val isKeycap: Boolean = true
)

@Composable
fun FloatingKeyboardBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.45f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "KeyboardDrift")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "TimeProgress"
    )

    val textMeasurer = rememberTextMeasurer()

    val keyItems = remember {
        listOf(
            FloatingKeyItem("ESC", 0.12f, 0.15f, 0.4f, 0.6f, 44f, 1, isKeycap = true),
            FloatingKeyItem("ENTER", 0.82f, 0.22f, -0.3f, 0.5f, 54f, 0, isKeycap = true),
            FloatingKeyItem("SPACE", 0.48f, 0.78f, 0.2f, -0.4f, 70f, 2, isKeycap = true),
            FloatingKeyItem("FADLLY", 0.25f, 0.65f, -0.5f, 0.3f, 58f, 2, isKeycap = true),
            FloatingKeyItem("CTRL", 0.75f, 0.88f, 0.35f, -0.5f, 46f, 0, isKeycap = true),
            FloatingKeyItem("TAB", 0.08f, 0.45f, 0.25f, 0.4f, 42f, 1, isKeycap = true),
            FloatingKeyItem("Q", 0.88f, 0.55f, -0.4f, 0.2f, 36f, 0, isKeycap = true),
            FloatingKeyItem("{ }", 0.35f, 0.28f, 0.3f, -0.3f, 34f, 1, isKeycap = false),
            FloatingKeyItem("</>", 0.65f, 0.38f, -0.2f, 0.45f, 38f, 0, isKeycap = false),
            FloatingKeyItem("ALT", 0.42f, 0.92f, 0.4f, -0.35f, 40f, 1, isKeycap = true),
            FloatingKeyItem("?", 0.85f, 0.72f, -0.3f, -0.4f, 32f, 2, isKeycap = false),
            FloatingKeyItem("SHIFT", 0.20f, 0.85f, 0.45f, 0.3f, 50f, 0, isKeycap = true),
            FloatingKeyItem("GEMINI", 0.60f, 0.12f, -0.35f, 0.25f, 52f, 2, isKeycap = true),
            FloatingKeyItem("W", 0.45f, 0.50f, 0.2f, 0.4f, 36f, 1, isKeycap = true),
            FloatingKeyItem("&&", 0.15f, 0.32f, -0.25f, -0.3f, 32f, 0, isKeycap = false)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            // Background Deep Radial Gradient
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF102A45),
                        CyberNavyDark
                    ),
                    center = Offset(canvasW * 0.5f, canvasH * 0.35f),
                    radius = canvasW * 0.9f
                ),
                size = size
            )

            // Draw glowing tech circuit grid lines
            drawGridLines(canvasW, canvasH, time)

            // Draw floating keycaps and tech symbols
            keyItems.forEach { item ->
                val primaryColor = when (item.colorType) {
                    0 -> ElectricCyan
                    1 -> NeonOrangePrimary
                    else -> PureGold
                }.copy(alpha = alpha)

                // Calculate animated drifting position
                val driftX = (item.initialXRatio * canvasW + (item.speedX * time * 20f)) % (canvasW + 120f) - 60f
                val driftY = (item.initialYRatio * canvasH + (item.speedY * time * 20f)) % (canvasH + 120f) - 60f
                val bobbing = sin((time * 0.05f + item.initialXRatio * 10f).toDouble()).toFloat() * 12f

                val posX = driftX
                val posY = driftY + bobbing

                if (item.isKeycap) {
                    drawFloatingKeycap(
                        item = item,
                        posX = posX,
                        posY = posY,
                        color = primaryColor,
                        textMeasurer = textMeasurer
                    )
                } else {
                    drawFloatingSymbol(
                        item = item,
                        posX = posX,
                        posY = posY,
                        color = primaryColor,
                        textMeasurer = textMeasurer
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawGridLines(width: Float, height: Float, time: Float) {
    val gridColor = ElectricCyan.copy(alpha = 0.06f)
    val step = 70f
    var x = 0f
    while (x < width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1f
        )
        x += step
    }
    var y = 0f
    while (y < height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1f
        )
        y += step
    }

    // Glowing drifting orbs
    val orbCount = 6
    for (i in 0 until orbCount) {
        val angle = (time * 0.02f + i * (2 * PI / orbCount)).toFloat()
        val orbX = width * 0.5f + cos(angle) * (width * 0.4f)
        val orbY = height * 0.5f + sin(angle * 1.3f) * (height * 0.35f)
        val orbColor = if (i % 2 == 0) NeonOrangePrimary.copy(alpha = 0.09f) else ElectricCyan.copy(alpha = 0.09f)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(orbColor, Color.Transparent),
                center = Offset(orbX, orbY),
                radius = 110f
            ),
            radius = 110f,
            center = Offset(orbX, orbY)
        )
    }
}

private fun DrawScope.drawFloatingKeycap(
    item: FloatingKeyItem,
    posX: Float,
    posY: Float,
    color: Color,
    textMeasurer: TextMeasurer
) {
    val keyW = item.sizeDp * 1.5f
    val keyH = item.sizeDp * 1.15f
    val corner = CornerRadius(10f, 10f)

    // Keycap bottom 3D bevel / base
    drawRoundRect(
        color = color.copy(alpha = 0.25f),
        topLeft = Offset(posX, posY + 6f),
        size = Size(keyW, keyH),
        cornerRadius = corner
    )

    // Keycap top cap
    drawRoundRect(
        color = Color(0xFF0F223D).copy(alpha = 0.85f),
        topLeft = Offset(posX, posY),
        size = Size(keyW, keyH),
        cornerRadius = corner
    )

    // Keycap glowing outline
    drawRoundRect(
        color = color,
        topLeft = Offset(posX, posY),
        size = Size(keyW, keyH),
        cornerRadius = corner,
        style = Stroke(width = 2.5f)
    )

    // Inner mechanical switch cross stem indicator on top corner
    drawCircle(
        color = color.copy(alpha = 0.4f),
        radius = 3.5f,
        center = Offset(posX + keyW - 10f, posY + 10f)
    )

    // Label text
    val textLayout = textMeasurer.measure(
        text = item.label,
        style = TextStyle(
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    )

    drawText(
        textLayoutResult = textLayout,
        topLeft = Offset(
            posX + (keyW - textLayout.size.width) / 2f,
            posY + (keyH - textLayout.size.height) / 2f
        )
    )
}

private fun DrawScope.drawFloatingSymbol(
    item: FloatingKeyItem,
    posX: Float,
    posY: Float,
    color: Color,
    textMeasurer: TextMeasurer
) {
    val textLayout = textMeasurer.measure(
        text = item.label,
        style = TextStyle(
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace
        )
    )

    drawCircle(
        color = color.copy(alpha = 0.15f),
        radius = 24f,
        center = Offset(posX, posY)
    )
    drawCircle(
        color = color,
        radius = 24f,
        center = Offset(posX, posY),
        style = Stroke(width = 1.5f)
    )

    drawText(
        textLayoutResult = textLayout,
        topLeft = Offset(
            posX - textLayout.size.width / 2f,
            posY - textLayout.size.height / 2f
        )
    )
}
