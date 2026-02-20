package com.focusflow.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.focusflow.ui.theme.ForestGreen
import com.focusflow.ui.theme.SageGreen

@Composable
fun TimerRing(
    remainingMs: Long,
    totalMs: Long,
    modifier: Modifier = Modifier,
    size: Dp = 280.dp,
    strokeWidth: Dp = 16.dp
) {
    val progress = if (totalMs > 0) {
        1f - (remainingMs.toFloat() / totalMs.toFloat())
    } else {
        0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "timer_progress"
    )
    Canvas(modifier = modifier.size(size)) {
        val strokeWidthPx = strokeWidth.toPx()
        val radius = (size.toPx() - strokeWidthPx) / 2f
        val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
        drawCircle(
            color = SageGreen.copy(alpha = 0.3f),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
        )
        val arcSize = size.toPx() - strokeWidthPx
        drawArc(
            color = ForestGreen,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            rect = Rect(Offset(strokeWidthPx / 2f, strokeWidthPx / 2f), Size(arcSize, arcSize)),
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
        )
    }
}
