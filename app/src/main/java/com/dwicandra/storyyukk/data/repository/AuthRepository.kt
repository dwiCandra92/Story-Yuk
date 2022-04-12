package com.dwicandra.storyyukk.data.repository

import android.util.Log
import com.dwicandra.storyyukk.data.remote.response.auth.ResponseLogin
import com.dwicandra.storyyukk.data.remote.response.auth.ResponseRegister
import com.dwicandra.storyyukk.data.remote.retrofit.ApiService
import com.dwicandra.storyyukk.data.result.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository private constructor(
    private val apiService: ApiService,
) {
    fun requestRegister(name: String, email: String, password: String) {
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false) {
                        Log.i("debug", "onResponse: BERHASIL")
                        response.body()?.message
                    } else {
                        response.body()?.message
                    }
                } else {
                    Log.i("debug", "onResponse: GA BERHASIL")
                    Result.Loading
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e("debug", "onFailure: ERROR > " + t.message)
                Result.Error("ERROR")
            }
        })
    }

    fun requestLogin(email: String, password: String) {
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false) {
                        Log.i("debug", "onResponse: BERHASIL")
                        response.body()?.message
                    } else {
                        Log.i("debug", "onResponse: GAGAL")
                        response.body()?.message
                    }
                } else {
                    Log.i("debug", "onResponse: GA BERHASIL")
                }
            }
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.i("debug", "onFailure: GA BERHASIL")
            }
        })
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}