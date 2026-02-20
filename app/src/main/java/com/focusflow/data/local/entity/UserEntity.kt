package com.focusflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val displayName: String,
    val totalCoins: Int = 0,
    val coinMultiplier: Int = 1,
    val totalFocusMinutes: Long = 0L,
    val createdAt: Long = System.currentTimeMillis()
)
