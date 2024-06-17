package com.example.agrotes_mobile.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.agrotes_mobile.data.local.room.DiseaseDao
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.data.pref.UserPreference
import com.example.agrotes_mobile.data.remote.retrofit.ApiService
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.data.pref.UserModel
import com.example.agrotes_mobile.data.remote.responses.weather.WeatherResponse
import com.example.agrotes_mobile.data.remote.test.DetailStoryResponse
import com.example.agrotes_mobile.data.remote.test.LoginResponse
import com.example.agrotes_mobile.data.remote.test.StoryResponse
import com.example.agrotes_mobile.data.remote.retrofit.ApiConfig
import com.example.agrotes_mobile.data.remote.retrofit.WeatherConfig
import com.example.agrotes_mobile.data.remote.test.ErrorResponse
import com.example.agrotes_mobile.data.remote.test.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepository(private var apiService: ApiService, private var userPreference: UserPreference, private val diseaseDao: DiseaseDao) {

    fun signup(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
            emit(Result.Loading)
            try {
                val result = apiService.register(name, email, password)
                emit(Result.Success(result))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                e.printStackTrace()
                emit(Result.Error(errorBody.message.toString()))
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