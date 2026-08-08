package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.FviRecord
import com.example.data.model.LearningModule
import com.example.data.model.ThreatAlert
import com.example.data.model.UserProfile

@Database(
    entities = [UserProfile::class, FviRecord::class, LearningModule::class, ThreatAlert::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SafeGuardDatabase : RoomDatabase() {
    abstract fun dao(): SafeGuardDao

    companion object {
        @Volatile
        private var INSTANCE: SafeGuardDatabase? = null

        fun getDatabase(context: Context): SafeGuardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SafeGuardDatabase::class.java,
                    "safeguard_ai_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
