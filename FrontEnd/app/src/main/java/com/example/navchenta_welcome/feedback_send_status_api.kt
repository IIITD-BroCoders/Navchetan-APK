package com.example.navchenta_welcome

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface feedback_send_status_api {
    @POST("http://165.22.212.47/api/status_receive/")
    fun send_status(@Body feedback_send_status: feedback_send_status): Call<ResponseBody>
}