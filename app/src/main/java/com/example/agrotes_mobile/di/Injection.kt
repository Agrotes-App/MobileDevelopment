package com.example.agrotes_mobile.di

import android.content.Context
import com.example.agrotes_mobile.data.local.room.DiseaseRoomDatabase
import com.example.agrotes_mobile.repository.DiseaseRepository

object Injection {
    fun provideDiseaseRepository(context: Context): DiseaseRepository {
        val database = DiseaseRoomDatabase.getDatabase(context)
        val dao = database.diseaseDao()
        return DiseaseRepository.getInstance(dao)
    }
}