package com.spym.navchenta_welcome

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface feedback_question_api {
    @POST("http://165.22.212.47/api/final_evaluation/")
    fun get_feedback_question(@Body languageSelected: language_selected): Call<feedback_questions_response>

}