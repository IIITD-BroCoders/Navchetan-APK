package com.spym.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface myapi_session_join{
    @POST("http://165.22.212.47/api/session_update/")
    fun send_join(@Body joinCreds: join_creds): Call<ResponseBody>
}
