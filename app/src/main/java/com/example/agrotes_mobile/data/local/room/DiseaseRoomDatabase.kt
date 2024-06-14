package com.example.agrotes_mobile.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity

@Database(entities = [DiseaseEntity::class], version = 1)
abstract class DiseaseRoomDatabase : RoomDatabase() {
    abstract fun diseaseDao(): DiseaseDao

    companion object {
        @Volatile
        private var INSTANCE: DiseaseRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): DiseaseRoomDatabase {
            if (INSTANCE == null) {
                synchronized(DiseaseRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DiseaseRoomDatabase::class.java, "history_database").build()
                }
            }
            return INSTANCE as DiseaseRoomDatabase
        }
    }
}