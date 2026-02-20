package com.focusflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.focusflow.data.local.entity.TreeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TreeDao {
    @Query("SELECT * FROM trees WHERE userId = :userId ORDER BY plantedAt DESC")
    fun getTreesByUser(userId: Long): Flow<List<TreeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tree: TreeEntity): Long

    @Query("SELECT COUNT(*) FROM trees WHERE userId = :userId AND treeType = 'Withered'")
    fun getWitheredCount(userId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM trees WHERE userId = :userId AND treeType = 'Healthy'")
    fun getHealthyCount(userId: Long): Flow<Int>
}
