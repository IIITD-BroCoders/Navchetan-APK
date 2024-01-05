package com.example.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface myapi_score {
    @POST("http://165.22.212.47/api/score_pretest/")
    fun send_score(@Body credential_score: credential_scores): Call<ResponseBody>
}
