package com.example.agrotes_mobile.data.remote.retrofit.app

import com.example.agrotes_mobile.data.remote.responses.auth.LoginRequest
import com.example.agrotes_mobile.data.remote.responses.auth.LoginResponses
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterRequest
import com.example.agrotes_mobile.data.remote.responses.auth.RegisterResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UpdateResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserProfileResponses
import com.example.agrotes_mobile.data.remote.responses.auth.UserUpdate
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // <-- auth Service -->
    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): RegisterResponses

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponses

    @PUT("auth/update")
    suspend fun update(
        @Body userUpdate: UserUpdate,
    ): UpdateResponses

    @GET("auth/{id}")
    suspend fun getUserById(
        @Path("id") id: String?,
    ): UserProfileResponses

    // <-- Disease Service -->
    @GET("diseases")
    suspend fun getAllDiseases(): List<DiseaseResponses>

    @GET("diseases/{id}")
    suspend fun getDiseaseById(
        @Path("id") id: String?,
    ): DiseaseResponses

    @GET("diseases/name/{diseaseName}")
    suspend fun getDiseaseByName(
        @Path("diseaseName") id: String?,
    ): DiseaseResponses
}