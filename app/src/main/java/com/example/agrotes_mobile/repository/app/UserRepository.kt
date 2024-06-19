package com.example.agrotes_mobile.repository.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.agrotes_mobile.data.local.room.DiseaseDao
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.data.pref.UserPreference
import com.example.agrotes_mobile.data.remote.retrofit.app.ApiService
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.data.local.entity.UserEntity
import com.example.agrotes_mobile.data.remote.responses.auth.LoginRequest
import com.example.agrotes_mobile.data.remote.responses.auth.LoginResponses
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterRequest
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UpdateResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserProfileResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserUpdate
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.data.remote.responses.disease.ErrorResponse
import com.example.agrotes_mobile.data.remote.retrofit.app.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepository(private var apiService: ApiService, private var userPreference: UserPreference, private val diseaseDao: DiseaseDao) {

    // <-- Auth -->
    fun signup(username: String, email: String, password: String): LiveData<Result<RegisterResponses>> = liveData {
        emit(Result.Loading)
        val request = RegisterRequest(username, email, password)
        try {
            val result = apiService.register(request)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponses::class.java)
            e.printStackTrace()
            emit(Result.Error(errorBody.error.toString()))
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
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponses::class.java)
            e.printStackTrace()
            emit(Result.Error(errorBody.error.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserById(id: String?): LiveData<Result<UserProfileResponses>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.getUserById(id)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateProfile(username: String, email: String): LiveData<Result<UpdateResponses>> = liveData {
            emit(Result.Loading)
            val request = UserUpdate(username = username, email = email)
            try {
                val token = runBlocking { userPreference.getSession().first().token }
                apiService = ApiConfig.getApiService(token)
                val result = apiService.update(request)
                emit(Result.Success(result))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, UpdateResponses::class.java)
                e.printStackTrace()
                emit(Result.Error(errorBody.error.toString()))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun updatePassword(password: String): LiveData<Result<UpdateResponses>> = liveData {
        emit(Result.Loading)
        val request = UserUpdate(password = password)
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.update(request)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UpdateResponses::class.java)
            e.printStackTrace()
            emit(Result.Error(errorBody.error.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    // <-- Disease -->
    fun getAllDiseases(): LiveData<Result<List<DiseaseResponses>>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.getAllDiseases()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDiseaseById(id: String?): LiveData<Result<DiseaseResponses>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.getDiseaseById(id)
            emit(Result.Success(result))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            e.printStackTrace()
            emit(Result.Error(errorBody.error.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDiseaseByName(name: String?): LiveData<Result<DiseaseResponses>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { userPreference.getSession().first().token }
            apiService = ApiConfig.getApiService(token)
            val result = apiService.getDiseaseByName(name)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            e.printStackTrace()
            emit(Result.Error(errorBody.error.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(user: UserEntity) = userPreference.saveSession(user)
    suspend fun insert(entity: DiseaseEntity) = diseaseDao.insert(entity)
    suspend fun delete(entity: DiseaseEntity) = diseaseDao.delete(entity)
    suspend fun logout() = userPreference.logOut()

    fun getSession(): Flow<UserEntity> = userPreference.getSession()
    fun getAllHistory(): LiveData<List<DiseaseEntity>> = diseaseDao.getAllHistory()


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, diseaseDao: DiseaseDao)
        : UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userPreference, diseaseDao)
        }.also { instance = it }
    }
}