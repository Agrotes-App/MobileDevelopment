package com.example.agrotes_mobile.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.agrotes_mobile.data.local.room.DiseaseDao
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.data.pref.UserPreference
import com.example.agrotes_mobile.data.remote.retrofit.ApiService
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.data.pref.UserModel
import com.example.agrotes_mobile.data.remote.responses.auth.LoginErrorResponse
import com.example.agrotes_mobile.data.remote.responses.auth.LoginRequest
import com.example.agrotes_mobile.data.remote.responses.auth.LoginResponses
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterRequest
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UpdateResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserUpdate
import com.example.agrotes_mobile.data.remote.responses.weather.WeatherResponse
import com.example.agrotes_mobile.data.remote.responses.test.DetailStoryResponse
import com.example.agrotes_mobile.data.remote.responses.test.StoryResponse
import com.example.agrotes_mobile.data.remote.retrofit.ApiConfig
import com.example.agrotes_mobile.data.remote.retrofit.WeatherConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepository(
    private var apiService: ApiService,
    private var userPreference: UserPreference,
    private val diseaseDao: DiseaseDao,
) {

    fun signup(username: String, email: String, password: String): LiveData<Result<RegisterResponses>> = liveData {
        emit(Result.Loading)
        val request = RegisterRequest(username, email, password)
        try {
            val result = apiService.register(request)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponses>> = liveData {
        emit(Result.Loading)
        val request = LoginRequest(email, password)
        try {
            val result = apiService.login(request)
            emit(Result.Success(result))
        }  catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateProfile(username: String, email: String, password: String): LiveData<Result<UpdateResponses>> = liveData {
        emit(Result.Loading)
        val request = UserUpdate(username, email, password)
        try {
            val result = apiService.update(request)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllDisease(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.getStories()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDiseaseById(id: String?): LiveData<Result<DetailStoryResponse>> = liveData {
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.getStoriesById(id)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)
    suspend fun logout() = userPreference.logOut()
    suspend fun insert(entity: DiseaseEntity) = diseaseDao.insert(entity)

    fun getSession(): Flow<UserModel> = userPreference.getSession()
    fun getAllHistory(): LiveData<List<DiseaseEntity>> = diseaseDao.getAllHistory()

    fun getWeather(lat: Double?, lon: Double?): LiveData<Result<WeatherResponse>> = liveData {
        emit(Result.Loading)
        try {
            apiService = WeatherConfig.getApiService()
            val result = apiService.getWeather(lat, lon)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            diseaseDao: DiseaseDao,
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userPreference, diseaseDao)
        }.also { instance = it }
    }
}