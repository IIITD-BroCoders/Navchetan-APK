package com.example.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface myapi_session {
    @POST("http://165.22.212.47/api/session_get/")
    fun get_session(@Body flag_credentials: flag_credentials): Call<sessionResponse>
}
