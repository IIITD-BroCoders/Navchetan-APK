package com.spym.navchenta_welcome
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface feedback_evaluation_api {
    @POST("http://165.22.212.47/api/feedback_answer/")
    fun send_eval(@Body evalData: eval_data): Call<ResponseBody>
}