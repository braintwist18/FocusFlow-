package com.focusflow.ui.forest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusflow.data.local.entity.TreeEntity
import com.focusflow.data.repository.FocusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ForestViewModel @Inject constructor(
    private val repository: FocusRepository
) : ViewModel() {

    val currentUser = repository.getCurrentUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    val trees = repository.getCurrentUser().flatMapLatest { user ->
        if (user != null) repository.getTrees(user.id) else flowOf(emptyList())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}
