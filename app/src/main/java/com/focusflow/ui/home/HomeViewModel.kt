package com.focusflow.ui.home

import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusflow.data.local.entity.UserEntity
import com.focusflow.data.repository.FocusRepository
import com.focusflow.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: FocusRepository
) : ViewModel() {

    val currentUser = repository.getCurrentUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    val timerRemainingMs = repository.timerRemainingMs().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0L
    )
    val isTimerRunning = repository.isTimerRunning().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    init {
        viewModelScope.launch {
            if (repository.getCurrentUserOnce() == null) {
                repository.insertOrUpdateUser(
                    UserEntity(displayName = "Focuser", totalCoins = 0, coinMultiplier = 1)
                )
            }
        }
    }

    fun startTimer(durationMs: Long = TimerService.DEFAULT_POMODORO_MS) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.EXTRA_DURATION_MS, durationMs)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun cancelTimer() {
        context.startService(
            Intent(context, TimerService::class.java).apply {
                action = TimerService.ACTION_CANCEL
            }
        )
    }

    fun onTimerComplete(focusMinutes: Int, userId: Long, taskId: Long?, multiplier: Int) {
        viewModelScope.launch {
            repository.addCoins(userId, focusMinutes * multiplier)
            repository.addFocusMinutes(userId, focusMinutes.toLong())
            repository.insertTree(
                com.focusflow.data.local.entity.TreeEntity(
                    userId = userId,
                    taskId = taskId,
                    treeType = "Healthy",
                    focusMinutes = focusMinutes,
                    plantedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
