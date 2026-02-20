package com.focusflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trees",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("plantedAt")]
)
data class TreeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val taskId: Long? = null,
    val treeType: String, // "Healthy", "Withered"
    val focusMinutes: Int = 0,
    val plantedAt: Long = System.currentTimeMillis()
)
