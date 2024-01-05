package com.example.navchenta_welcome

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface myapi_verify {
    @POST("http://165.22.212.47/api/verify/")
    fun ver_cred(@Body credential_verify: credentials_sign_up): Call<ResponseBody>
}