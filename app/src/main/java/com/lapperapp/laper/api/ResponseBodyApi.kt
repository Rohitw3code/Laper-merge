package com.laperapp.laper

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.laperapp.laper.Data.LoginModel
import com.laperapp.laper.Data.LoginResponse
import com.laperapp.laper.Data.SignUpModel
import com.laperapp.laper.Data.UserBase
import com.laperapp.laper.api.RetrofitClient
import com.lapperapp.laper.Data.UserUpdateModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ResponseBodyApi {
//    fun getUser(context:Context): Call<UserFetch> {
//        val token = RetrofitClient.getCredential("token",context)
//        val jsonapi = RetrofitClient.getClient()
//        return jsonapi.getUserData(token)
//    }


    fun logInResponseBody(model: LoginModel, onResponse: (String?) -> Unit, onFailure: (Throwable) -> Unit) {
        val jsonapi = RetrofitClient.getClient()
        jsonapi.logIn(model).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    onResponse(response.body()?.token)
                } else {
                    onFailure(Throwable("Response unsuccessful"))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun signUpResponseBody(model: SignUpModel, onResponse: (String?) -> Unit, onFailure: (Throwable) -> Unit) {
        val jsonapi = RetrofitClient.getClient()
        jsonapi.signUp(model).enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {
                if (response.isSuccessful) {
                    onResponse("Sign Up Successful")
                } else {
                    onFailure(Throwable("Response unsuccessful"))
                }
            }
            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun getUserResponseBody(context: Context, onResponse: (UserBase?) -> Unit, onFailure: (Throwable) -> Unit) {
        val token = RetrofitClient.getCredential("token",context)
        val jsonapi = RetrofitClient.getClient()
        jsonapi.getUserData(token).enqueue(object : Callback<UserBase> {
            override fun onResponse(call: Call<UserBase>, response: Response<UserBase>) {
                if (response.isSuccessful) {
                    val userFetch: UserBase? = response.body()
                    onResponse(userFetch)
                } else {
                    onFailure(Throwable("Response unsuccessful"))
                }
            }

            override fun onFailure(call: Call<UserBase>, t: Throwable) {
                onFailure(t)
            }
        })
    }


    fun updateUser(context: Context,updateModel: UserUpdateModel, onResponse: (UserBase?) -> Unit, onFailure: (Throwable) -> Unit){
        val token = RetrofitClient.getCredential("token",context)
        val jsonapi = RetrofitClient.getClient()
        jsonapi.updateUser(token,updateModel).enqueue(object : Callback<UserBase> {
            override fun onResponse(call: Call<UserBase>, response: Response<UserBase>) {
                if (response.isSuccessful) {
                    val userFetch: UserBase? = response.body()
                    onResponse(userFetch)
                } else {
                    val errorMessage = "Response unsuccessful: ${response.code()}"
                    onFailure(Throwable(errorMessage))                }
            }
            override fun onFailure(call: Call<UserBase>, t: Throwable) {
                onFailure(t)
            }
        })
    }


}