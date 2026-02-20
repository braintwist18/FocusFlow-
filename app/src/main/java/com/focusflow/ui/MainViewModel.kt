package com.focusflow.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusflow.data.local.entity.TreeEntity
import com.focusflow.data.repository.FocusRepository
import com.focusflow.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: FocusRepository
) : ViewModel() {

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
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                if (repository.isTimerRunning().value) {
                    context.startService(
                        Intent(context, TimerService::class.java).apply {
                            action = TimerService.ACTION_CANCEL
                        }
                    )
                    viewModelScope.launch {
                        recordWitheredAndResetMultiplier()
                    }
                }
            }
        })
    }

    private suspend fun recordWitheredAndResetMultiplier() {
        val user = repository.getCurrentUserOnce() ?: return
        repository.insertTree(
            TreeEntity(
                userId = user.id,
                taskId = null,
                treeType = "Withered",
                focusMinutes = 0,
                plantedAt = System.currentTimeMillis()
            )
        )
        repository.setCoinMultiplier(user.id, 1)
    }
}
