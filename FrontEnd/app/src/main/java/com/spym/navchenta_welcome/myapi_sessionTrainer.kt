package com.spym.navchenta_welcome
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface myapi_sessionTrainer{
    @POST("http://165.22.212.47/api/session_trainers/")
    fun get_TrainerSession(@Body flag_credentials: flag_credentials): Call<sessionResponse>
}
