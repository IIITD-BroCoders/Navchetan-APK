package com.spym.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface myapi_session_delete{
    @POST("http://165.22.212.47/api/session_delete_api/")
    fun delete(@Body del_cred: credential_delete): Call<ResponseBody>
}