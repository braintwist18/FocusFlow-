package com.focusflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.focusflow.data.local.dao.TaskDao
import com.focusflow.data.local.dao.TreeDao
import com.focusflow.data.local.dao.UserDao
import com.focusflow.data.local.entity.TaskEntity
import com.focusflow.data.local.entity.TreeEntity
import com.focusflow.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        TreeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun treeDao(): TreeDao
}
