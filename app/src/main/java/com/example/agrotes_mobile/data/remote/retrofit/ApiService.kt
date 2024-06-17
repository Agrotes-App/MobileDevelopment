package com.example.agrotes_mobile.data.remote.retrofit

import com.example.agrotes_mobile.BuildConfig
import com.example.agrotes_mobile.data.remote.responses.weather.WeatherResponse
import com.example.agrotes_mobile.data.remote.test.DetailStoryResponse
import com.example.agrotes_mobile.data.remote.test.LoginResponse
import com.example.agrotes_mobile.data.remote.test.RegisterResponse
import com.example.agrotes_mobile.data.remote.test.StoryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

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