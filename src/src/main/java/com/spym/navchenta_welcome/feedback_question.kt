package com.spym.navchenta_welcome

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.PrintWriter
import java.io.StringWriter

class feedback_question(private val context: Context){
    private var questions: MutableList<String> = mutableListOf()
    private var options: MutableList<List<String>> = mutableListOf()
    var correctAnswers: MutableList<String> = mutableListOf()
    var mode: MutableList<String> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var feedbackQuestion_api: feedback_question_api
    //    val question=arrayOf("What is the capital of India?",
//    "Which one of the languages is made by google?",
//    "What is 2 times 3?")
//    val choices: Array<Array<String>> = arrayOf(
//        arrayOf("", "","",""),
//        arrayOf("java","python","kotlin","cpp"),
//        arrayOf("5","3","6","21")
//    )
//    val valideAnswers=arrayOf("Delhi","kotlin","6")
    fun fetchQuestionsFromApi(languageSelected: language_selected,callback: () -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://165.22.212.47/api/final_evaluation/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        feedbackQuestion_api = retrofit.create(feedback_question_api::class.java)
        sharedPreferences = context.getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val language = sharedPreferences.getString("language_selected"," ")
            ?.let { language_selected(it) }
        if (language != null) {
            feedbackQuestion_api.get_feedback_question(language).enqueue(object : Callback<feedback_questions_response> {
                override fun onResponse(call: Call<feedback_questions_response>, response: Response<feedback_questions_response>) {
                    if (response.isSuccessful) {
                        val feedback_questionResponse = response.body()
                        feedback_questionResponse?.let { response ->
                            val questionsSplit = response.questions.split(":::(")
                            val modeSplit = response.mode.split(":::(")
                            val options1Split = response.option1.split(":::(")
                            val options2Split = response.option2.split(":::(")
                            val options3Split = response.option3.split(":::(")
                            val options4Split = response.option4.split(":::(")
//                            val correctSplit = response.correct.split(":::(")

                            for (i in questionsSplit.indices) {
                                questions.add(questionsSplit[i].trim())
                                val optionsList = listOf(
                                    options1Split[i].trim(),
                                    options2Split[i].trim(),
                                    options3Split[i].trim(),
                                    options4Split[i].trim()
                                )
                                options.add(optionsList)
                                mode.add(modeSplit[i].trim())
//                                correctAnswers.add(correctSplit[i].trim())
                            }

                            // Invoke the callback to notify data is fetched
                            callback()
                        }
                    } else {
                        println("API request failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<feedback_questions_response>, t: Throwable) {
                    val sw = StringWriter()
                    t.printStackTrace(PrintWriter(sw))
                    val exceptionAsString = sw.toString()
                    println(exceptionAsString)
                }
            })
        }
    }
    fun getQuestions(): MutableList<String> {
        return questions
    }
    fun getOptions(): List<List<String>> {
        return options
    }
//    fun getMode(): MutableList<String> {
//        return mode
//    }
}
