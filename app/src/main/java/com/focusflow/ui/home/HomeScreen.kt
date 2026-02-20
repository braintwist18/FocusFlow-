package com.focusflow.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focusflow.service.TimerService
import com.focusflow.ui.components.TimerRing

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.currentUser.collectAsState(initial = null)
    val remainingMs by viewModel.timerRemainingMs.collectAsState(initial = 0L)
    val isRunning by viewModel.isTimerRunning.collectAsState(initial = false)
    val totalMs = TimerService.DEFAULT_POMODORO_MS

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            user?.let { u ->
                Text(
                    text = "Coins: ${u.totalCoins}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Multiplier: x${u.coinMultiplier}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            TimerRing(
                remainingMs = remainingMs,
                totalMs = if (isRunning) totalMs else totalMs
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = formatTime(remainingMs),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (isRunning) {
                Button(onClick = { viewModel.cancelTimer() }) {
                    Text("Cancel")
                }
            } else {
                Button(onClick = { viewModel.startTimer(totalMs) }) {
                    Text("Start focus")
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
