package com.spym.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface generate_certificate {
    @POST("http://165.22.212.47/api/generate_certificate/")
    fun send_email(@Body flag_credentials: flag_credentials): Call<ResponseBody>
}