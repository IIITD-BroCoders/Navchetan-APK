package com.example.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface myapi{
    @POST("http://165.22.212.47/api/login/")
    fun checkCredentials(@Body credential: credentials): Call<ResponseBody>
}
data class emailresponse(val email:String)