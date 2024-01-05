package com.spym.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface flag_receive {
    @POST("http://165.22.212.47/api/flag_receive/")
    fun send_email(@Body flag_credentials: flag_credentials): Call<ResponseBody>
}