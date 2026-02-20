package com.focusflow.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.focusflow.R
import com.focusflow.data.local.entity.TreeEntity
import com.focusflow.data.repository.FocusRepository
import com.focusflow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : LifecycleService() {

    @Inject
    lateinit var repository: FocusRepository

    private var countDownTimer: CountDownTimer? = null
    private var currentDurationMs: Long = 0L


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START -> {
                val durationMs = intent.getLongExtra(EXTRA_DURATION_MS, DEFAULT_POMODORO_MS)
                startTimer(durationMs)
                startForeground(NOTIFICATION_ID, createNotification(0, durationMs))
            }
            ACTION_CANCEL -> {
                cancelTimer()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            else -> {
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        cancelTimer()
        super.onDestroy()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        repository.setTimerRunning(false)
        repository.setTimerRemaining(0L)
    }

    private fun startTimer(durationMs: Long) {
        cancelTimer()
        currentDurationMs = durationMs
        repository.setTimerRemaining(durationMs)
        repository.setTimerRunning(true)
        countDownTimer = object : CountDownTimer(durationMs, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                repository.setTimerRemaining(millisUntilFinished)
                updateNotification(millisUntilFinished, durationMs)
            }

            override fun onFinish() {
                repository.setTimerRemaining(0L)
                repository.setTimerRunning(false)
                updateNotification(0L, durationMs)
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                recordSuccessfulSession()
            }
        }.start()
    }

    private fun recordSuccessfulSession() {
        lifecycleScope.launch {
            val user = repository.getCurrentUserOnce() ?: return@launch
            val focusMinutes = (currentDurationMs / 60_000).toInt()
            val coins = focusMinutes * user.coinMultiplier
            repository.addCoins(user.id, coins)
            repository.addFocusMinutes(user.id, focusMinutes.toLong())
            repository.insertTree(
                TreeEntity(
                    userId = user.id,
                    taskId = null,
                    treeType = "Healthy",
                    focusMinutes = focusMinutes,
                    plantedAt = System.currentTimeMillis()
                )
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(remainingMs: Long, totalMs: Long): android.app.Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val progress = if (totalMs > 0) (totalMs - remainingMs).toInt() / 1000 else 0
        val max = (totalMs / 1000).toInt()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(formatTime(remainingMs))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setProgress(max, progress, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun updateNotification(remainingMs: Long, totalMs: Long) {
        val notification = createNotification(remainingMs, totalMs)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = (ms / 1000).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }

    companion object {
        const val CHANNEL_ID = "focus_timer_channel"
        const val NOTIFICATION_ID = 1001
        const val EXTRA_DURATION_MS = "extra_duration_ms"
        const val DEFAULT_POMODORO_MS = 25 * 60 * 1000L
        const val ACTION_START = "com.focusflow.action.START_TIMER"
        const val ACTION_CANCEL = "com.focusflow.action.CANCEL_TIMER"
    }
}
