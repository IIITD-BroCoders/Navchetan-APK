package com.example.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface session_create{
    @POST("http://165.22.212.47/api/session_create/")
    fun send_session_details(@Body sessionCreate: session_create_data): Call<ResponseBody>
}