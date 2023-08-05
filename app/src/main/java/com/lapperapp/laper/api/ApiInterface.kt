package com.laperapp.laper.api

import com.laperapp.laper.Data.LoginModel
import com.laperapp.laper.Data.LoginResponse
import com.laperapp.laper.Data.SignUpModel
import com.laperapp.laper.Data.UserBase
import com.lapperapp.laper.Data.UserUpdateModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import kotlin.collections.HashMap as HashMap1

interface ApiInterface {

    @POST("user-fetch")
    fun getUserData(
        @Header("x-access-token") token:String,
    ): Call<UserBase>

    @POST("signup")
    fun signUp(@Body signUpRequest: SignUpModel): Call<SignUpModel>

    @POST("login")
    fun logIn(@Body loginRequest: LoginModel): Call<LoginResponse>

    @PUT("user-update")
    fun updateUser(@Header("x-access-token") token:String,
                   @Body userUpdateModel: UserUpdateModel
    ):Call<UserBase>

}