package com.focusflow.data.repository

import com.focusflow.data.local.dao.TaskDao
import com.focusflow.data.local.dao.TreeDao
import com.focusflow.data.local.dao.UserDao
import com.focusflow.data.local.entity.TaskEntity
import com.focusflow.data.local.entity.TreeEntity
import com.focusflow.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val taskDao: TaskDao,
    private val treeDao: TreeDao
) : FocusRepository {

    private val _timerRemainingMs = MutableStateFlow(0L)
    override fun timerRemainingMs(): StateFlow<Long> = _timerRemainingMs.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    override fun isTimerRunning(): StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    override fun setTimerRemaining(ms: Long) {
        _timerRemainingMs.value = ms
    }

    override fun setTimerRunning(running: Boolean) {
        _isTimerRunning.value = running
    }

    override fun getCurrentUser(): Flow<UserEntity?> = userDao.getCurrentUser()

    override suspend fun getCurrentUserOnce(): UserEntity? {
        return userDao.getCurrentUser().first()
    }

    override suspend fun insertOrUpdateUser(user: UserEntity): Long {
        return if (user.id == 0L) {
            userDao.insert(user)
        } else {
            userDao.update(user)
            user.id
        }
    }

    override suspend fun addCoins(userId: Long, coins: Int) {
        userDao.addCoins(userId, coins)
    }

    override suspend fun setCoinMultiplier(userId: Long, multiplier: Int) {
        userDao.setCoinMultiplier(userId, multiplier)
    }

    override suspend fun addFocusMinutes(userId: Long, minutes: Long) {
        userDao.addFocusMinutes(userId, minutes)
    }

    override fun getTasks(userId: Long): Flow<List<TaskEntity>> =
        taskDao.getTasksByUser(userId)

    override suspend fun getTaskById(taskId: Long): TaskEntity? =
        taskDao.getTaskById(taskId)

    override suspend fun insertTask(task: TaskEntity): Long =
        taskDao.insert(task)

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
    }

    override suspend fun deleteTask(taskId: Long) {
        taskDao.deleteById(taskId)
    }

    override fun getTrees(userId: Long): Flow<List<TreeEntity>> =
        treeDao.getTreesByUser(userId)

    override suspend fun insertTree(tree: TreeEntity): Long =
        treeDao.insert(tree)
}
