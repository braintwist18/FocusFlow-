package com.focusflow.data.repository

import com.focusflow.data.local.entity.TaskEntity
import com.focusflow.data.local.entity.TreeEntity
import com.focusflow.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FocusRepository {
    fun getCurrentUser(): Flow<UserEntity?>
    suspend fun getCurrentUserOnce(): UserEntity?
    suspend fun insertOrUpdateUser(user: UserEntity): Long
    suspend fun addCoins(userId: Long, coins: Int)
    suspend fun setCoinMultiplier(userId: Long, multiplier: Int)
    suspend fun addFocusMinutes(userId: Long, minutes: Long)

    fun getTasks(userId: Long): Flow<List<TaskEntity>>
    suspend fun getTaskById(taskId: Long): TaskEntity?
    suspend fun insertTask(task: TaskEntity): Long
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(taskId: Long)

    fun getTrees(userId: Long): Flow<List<TreeEntity>>
    suspend fun insertTree(tree: TreeEntity): Long

    fun timerRemainingMs(): StateFlow<Long>
    fun isTimerRunning(): StateFlow<Boolean>
    fun setTimerRemaining(ms: Long)
    fun setTimerRunning(running: Boolean)
}
