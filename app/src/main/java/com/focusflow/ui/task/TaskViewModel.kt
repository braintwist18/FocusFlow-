package com.focusflow.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusflow.data.local.entity.TaskEntity
import com.focusflow.data.repository.FocusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: FocusRepository
) : ViewModel() {

    val currentUser = repository.getCurrentUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    val tasks = repository.getCurrentUser().flatMapLatest { user ->
        if (user != null) repository.getTasks(user.id) else flowOf(emptyList())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    suspend fun addTask(title: String, description: String?, estimatedPomodoros: Int): Long? {
        val user = repository.getCurrentUserOnce() ?: return null
        return repository.insertTask(
            TaskEntity(
                userId = user.id,
                title = title,
                description = description,
                estimatedPomodoros = estimatedPomodoros
            )
        )
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
}
