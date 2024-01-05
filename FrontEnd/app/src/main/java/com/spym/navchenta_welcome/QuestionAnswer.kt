// QuestionAnswer.kt

package com.spym.navchenta_welcome

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionAnswer(private val context: Context) {
    private lateinit var questionApiService: myapi_quizzing
    private var questions: MutableList<String> = mutableListOf()
    private var options: MutableList<List<String>> = mutableListOf()
    var correctAnswers: MutableList<String> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences

    fun fetchQuestionsFromApi(languageSelected: language_selected,callback: () -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://165.22.212.47/api/questions/") // Replace with the base URL of your Django API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        questionApiService = retrofit.create(myapi_quizzing::class.java)
        sharedPreferences = context.getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val language = sharedPreferences.getString("language_selected"," ")
            ?.let { language_selected(it) }
        if (language != null) {
            questionApiService.getQuestions(languageSelected).enqueue(object : Callback<QuestionResponse> {
                override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                    // Handle API request failure
                    println("API request failed: ${t.message}")
                }

                override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
                    if (response.isSuccessful) {
                        val questionResponse = response.body()
                        questionResponse?.let { response ->
                            val questionsSplit = response.questions.split(":::(")
                            val options1Split = response.option1.split(":::(")
                            val options2Split = response.option2.split(":::(")
                            val options3Split = response.option3.split(":::(")
                            val options4Split = response.option4.split(":::(")
                            val options5Split = response.option5.split(":::(")
                            val options6Split = response.option6.split(":::(")
                            val correctSplit = response.correct.split(":::(")

                            for (i in questionsSplit.indices) {
                                questions.add(questionsSplit[i].trim())
                                val optionsList = listOf(
                                    options1Split[i].trim(),
                                    options2Split[i].trim(),
                                    options3Split[i].trim(),
                                    options4Split[i].trim(),
                                    options5Split[i].trim(),
                                    options6Split[i].trim()
                                )
                                options.add(optionsList)
                                correctAnswers.add(correctSplit[i].trim())
                            }

                            // Invoke the callback to notify data is fetched
                            callback()
                        }
                    } else {
                        println("API request failed: ${response.code()}")
                    }
                }
            })
        }
    }

    fun quiz_getQuestions(): List<String> {
        return questions
    }

    fun quiz_getOptions(): List<List<String>> {
        return options
    }

    fun quiz_getCorrectAnswers(): List<String> {
        return correctAnswers
    }
}