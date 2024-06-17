package com.example.agrotes_mobile.data.remote.retrofit

import com.example.agrotes_mobile.BuildConfig
import com.example.agrotes_mobile.data.remote.responses.auth.CreatedUser
import com.example.agrotes_mobile.data.remote.responses.auth.LoginRequest
import com.example.agrotes_mobile.data.remote.responses.auth.LoginResponses
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterRequest
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UpdateResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserProfileResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserUpdate
import com.example.agrotes_mobile.data.remote.responses.weather.WeatherResponse
import com.example.agrotes_mobile.data.remote.responses.test.DetailStoryResponse
import com.example.agrotes_mobile.data.remote.responses.test.LoginResponse
import com.example.agrotes_mobile.data.remote.responses.test.RegisterResponse
import com.example.agrotes_mobile.data.remote.responses.test.StoryResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponses

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponses

    @PUT("auth/update")
    suspend fun update(@Body userUpdate: UserUpdate): UpdateResponses

    @GET("auth/{id}")
    suspend fun getUserById(
        @Path("id") id: String?,
    ): UserProfileResponses

    @GET("stories")
    suspend fun getStories(): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoriesById(
        @Path("id") id: String?,
    ): DetailStoryResponse

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): WeatherResponse
}