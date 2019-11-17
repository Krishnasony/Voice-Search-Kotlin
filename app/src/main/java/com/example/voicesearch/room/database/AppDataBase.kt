package com.example.voicesearch.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.voicesearch.room.SearchDao
import com.example.voicesearch.room.database.AppDataBase.Companion.VERSION
import com.example.voicesearch.room.entity.RecentSearch

@Database(
    entities = [
    RecentSearch::class
    ],
    version = VERSION,
    exportSchema = false

)
abstract class AppDataBase:RoomDatabase() {
    abstract val searchDao:SearchDao

    companion object{
        const val CURRENT: String = "AppDataBase"
        const val DATABASE_NAME="voice_search.db"
        const val VERSION = 1

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(
            context: Context
        ): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .setJournalMode(JournalMode.TRUNCATE)

                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}