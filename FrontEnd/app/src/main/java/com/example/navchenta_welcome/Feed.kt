package com.example.navchenta_welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText

import android.widget.RatingBar
import android.widget.Toast
import com.example.navchenta_welcome.navigation.Drawer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Feed : AppCompatActivity() {
    private lateinit var tvFeedback: TextView
    private lateinit var feedback_txt: EditText
    private lateinit var rbStars: RatingBar
    private lateinit var submit_button : Button
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback)
        submit_button = findViewById(R.id.btn_send)
        tvFeedback = findViewById(R.id.tvFeedback)
        rbStars = findViewById(R.id.rbStars)
        feedback_txt = findViewById(R.id.etFeedback
        )
        var feedback_text_send= feedback_txt.text.toString()
        rbStars.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            when (rating.toInt()) {
                0 -> tvFeedback.text = "Very Dissatisfied"
                1 -> tvFeedback.text = "Dissatisfied"
                2, 3 -> tvFeedback.text = "OK"
                4 -> tvFeedback.text = "Satisfied"
                5 -> tvFeedback.text = "Very Satisfied"
                else -> {
                    // Handle other cases if needed
                }
            }
        }
        submit_button.setOnClickListener {
            showToast("Thank you for your feedback!")
            val retrofit = Retrofit.Builder()
                .baseUrl("http://165.22.212.47/api/feedback_get_info/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val feedback_get_info = retrofit.create(feedback_get_info::class.java)
            val email = sharedPreferences.getString("email"," ")
            val rating = rbStars.rating.toString()
            val answer = feedback_txt.text.toString()
            val feedback_get = feedback_get(email!!,rating,answer)

            feedback_get_info.get_feedback_question(feedback_get).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                    // Handle API request failure
                    println("API request failed: ${t.message}")
                }

                override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                    if (response.isSuccessful) {
                        val feedbackResponse = response.body()
                        feedbackResponse?.let { response ->
                            println("API request successful: $response")
                        }
                    } else {
                        println("API request failed: ${response.errorBody().toString()}")
                    }
                }
            })

            val intent = Intent(this, Drawer::class.java)
            startActivity(intent)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)) {
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}
