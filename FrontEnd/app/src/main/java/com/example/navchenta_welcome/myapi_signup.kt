package com.example.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface myapi_signup {
    @POST("http://165.22.212.47/api/sign_up/")
    fun add_cred(@Body credential_signup: credentials_sign_up): Call<ResponseBody>
}