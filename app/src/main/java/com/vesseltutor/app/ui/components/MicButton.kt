package com.vesseltutor.app.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vesseltutor.app.ui.practice.Phase
import com.vesseltutor.app.ui.theme.AlertRed
import com.vesseltutor.app.ui.theme.SuccessGreen
import com.vesseltutor.app.ui.theme.WarningAmber

@Composable
fun MicButton(
    phase: Phase,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mic-pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(animation = tween(700), repeatMode = RepeatMode.Reverse),
        label = "pulse-scale"
    )

    val backgroundColor = when (phase) {
        Phase.IDLE -> MaterialTheme.colorScheme.primary
        Phase.LISTENING -> AlertRed
        Phase.PROCESSING -> WarningAmber
        Phase.FEEDBACK_READY -> SuccessGreen
    }

    Box(
        modifier = modifier
            .size(128.dp)
            .scale(if (phase == Phase.LISTENING) pulseScale else 1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (phase) {
            Phase.PROCESSING -> CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
            Phase.FEEDBACK_READY -> Icon(
                imageVector = Icons.Filled.Replay,
                contentDescription = "Try again",
                tint = Color.White,
                modifier = Modifier.size(52.dp)
            )
            else -> Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = "Speak",
                tint = Color.White,
                modifier = Modifier.size(52.dp)
            )
        }
    }
}
