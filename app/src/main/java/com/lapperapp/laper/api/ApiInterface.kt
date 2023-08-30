package com.laperapp.laper.api

import com.laperapp.laper.Data.LoginModel
import com.laperapp.laper.Data.LoginResponse
import com.laperapp.laper.Data.SignUpModel
import com.laperapp.laper.Data.UserBase
import com.lapperapp.laper.Data.ExpertFilterModel
import com.lapperapp.laper.Data.ExpertBase
import com.lapperapp.laper.Data.UserUpdateModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("api/user-fetch")
    fun getUserData(
        @Header("x-access-token") token:String,
    ): Call<UserBase>

    @POST("api/signup")
    fun signUp(@Body signUpRequest: SignUpModel): Call<SignUpModel>

    @POST("api/login")
    fun logIn(@Body loginRequest: LoginModel): Call<LoginResponse>


    @PUT("api/user-update")
    fun updateUser(@Header("x-access-token") token:String,
                   @Body userUpdateModel: UserUpdateModel
    ):Call<UserBase>

    @POST("api/user-fetch-experts")
    fun getExperts(@Header("x-access-token") token:String,
    ):Call<ExpertBase>

    @POST("api/user-fetch-experts")
    fun getExpertData(@Header("x-access-token") token:String,
                      @Body model:ExpertFilterModel
    ):Call<ExpertBase>


    @GET("auth/google")
    fun authGoogle(): Call<LoginResponse>


}