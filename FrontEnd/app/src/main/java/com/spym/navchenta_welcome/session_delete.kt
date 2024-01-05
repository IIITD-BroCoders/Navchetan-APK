package com.spym.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface session_delete{
    @POST("http://165.22.212.47/api/session_delete/")
    fun ses_delete(@Body sessionDelete: credential_delete): Call<ResponseBody>
}

