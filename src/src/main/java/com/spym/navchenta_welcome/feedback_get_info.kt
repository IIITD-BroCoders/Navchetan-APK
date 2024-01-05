package com.spym.navchenta_welcome

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface feedback_get_info {
    @POST("http://165.22.212.47/api/feedback_get_info/")
    fun get_feedback_question(@Body feedback_get: feedback_get): Call<ResponseBody>
}