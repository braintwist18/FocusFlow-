package com.focusflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusflow.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("UPDATE users SET totalCoins = totalCoins + :coins WHERE id = :userId")
    suspend fun addCoins(userId: Long, coins: Int)

    @Query("UPDATE users SET coinMultiplier = :multiplier WHERE id = :userId")
    suspend fun setCoinMultiplier(userId: Long, multiplier: Int)

    @Query("UPDATE users SET totalFocusMinutes = totalFocusMinutes + :minutes WHERE id = :userId")
    suspend fun addFocusMinutes(userId: Long, minutes: Long)
}
