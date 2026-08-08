package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FviRecord
import com.example.data.model.LearningModule
import com.example.data.model.ThreatAlert
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface SafeGuardDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(profile: UserProfile)

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    @Query("SELECT * FROM fvi_history ORDER BY timestampMs DESC")
    fun getFviHistory(): Flow<List<FviRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFviRecord(record: FviRecord)

    @Query("SELECT * FROM learning_modules")
    fun getAllModules(): Flow<List<LearningModule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<LearningModule>)

    @Query("UPDATE learning_modules SET isCompleted = :completed WHERE id = :moduleId")
    suspend fun updateModuleCompletion(moduleId: String, completed: Boolean)

    @Query("SELECT * FROM threat_alerts ORDER BY id DESC")
    fun getThreatAlerts(): Flow<List<ThreatAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThreatAlerts(alerts: List<ThreatAlert>)

    @Query("UPDATE threat_alerts SET isRead = 1 WHERE id = :alertId")
    suspend fun markAlertAsRead(alertId: String)
}
