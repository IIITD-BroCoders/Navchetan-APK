package com.spym.navchenta_welcome
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface myapi_quizzing {
    @POST("http://165.22.212.47/api/questions/")
    fun getQuestions(@Body languageSelected: language_selected): Call<QuestionResponse>
}
