package com.example.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface myapi_quizzing {
    @POST("http://165.22.212.47/api/questions/")
    fun getQuestions(@Body languageSelected: language_selected): Call<QuestionResponse>
}
