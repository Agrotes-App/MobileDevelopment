package com.example.agrotes_mobile.di.app

import android.content.Context
import com.example.agrotes_mobile.data.local.room.DiseaseRoomDatabase
import com.example.agrotes_mobile.data.pref.UserPreference
import com.example.agrotes_mobile.data.pref.dataStore
import com.example.agrotes_mobile.data.remote.retrofit.app.ApiConfig
import com.example.agrotes_mobile.repository.app.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// app injection
object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = DiseaseRoomDatabase.getDatabase(context)
        val dao = database.diseaseDao()
        return UserRepository.getInstance(apiService, pref, dao)
    }
}