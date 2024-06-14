package com.example.agrotes_mobile.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.agrotes_mobile.data.local.room.DiseaseDao
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.data.pref.UserPreference
import com.example.agrotes_mobile.data.remote.responses.RegisterResponse
import com.example.agrotes_mobile.data.remote.retrofit.ApiService
import com.example.agrotes_mobile.data.Result
import com.example.agrotes_mobile.data.pref.UserModel
import com.example.agrotes_mobile.data.remote.responses.LoginResponse
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private var apiService: ApiService,
    private var userPreference: UserPreference,
    private val diseaseDao: DiseaseDao
) {
    fun signup(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)

    suspend fun logout() = userPreference.logOut()

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    fun getAllHistory(): LiveData<List<DiseaseEntity>> = diseaseDao.getAllHistory()

    suspend fun insert(entity: DiseaseEntity) = diseaseDao.insert(entity)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            diseaseDao: DiseaseDao
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userPreference, diseaseDao)
        }.also { instance = it }
    }
}