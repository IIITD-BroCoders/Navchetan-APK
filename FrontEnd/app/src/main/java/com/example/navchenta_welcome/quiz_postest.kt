// quiz_page.kt

package com.example.navchenta_welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.navchenta_welcome.navigation.Drawer
//import com.example.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class quiz_postest : AppCompatActivity(), View.OnClickListener {

    private var score = 0
    private var totalQuestion = 0
    private var currentQuestionInd = 0
    private var selectedAnswer = ""
    private lateinit var questionTextView: TextView
    private lateinit var ansA: Button
    private lateinit var ansB: Button
    private lateinit var ansC: Button
    private lateinit var ansD: Button
    private lateinit var ansE : Button
    private lateinit var ansF : Button
    private lateinit var submit_btn: Button
    private lateinit var final_btn: Button
    private lateinit var questionAnswer: QuestionAnswer
    private lateinit var login: String
    private lateinit var sharedPreferences: SharedPreferences
    private var answer: String = " "
    override fun onBackPressed() {
        val intent = Intent(this, Drawer::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        login = intent.getStringExtra("emailid").toString()
        val totalQuestionTextView = findViewById<TextView>(R.id.total_question)
        questionTextView = findViewById(R.id.question)
        ansA = findViewById(R.id.ans_A)
        ansB = findViewById(R.id.ans_B)
        ansC = findViewById(R.id.ans_C)
        ansD = findViewById(R.id.ans_D)
        ansE = findViewById(R.id.ans_E)
        ansF = findViewById(R.id.ans_F)
        submit_btn = findViewById(R.id.submit_Btn)
        final_btn = findViewById(R.id.final_Btn)
        ansA.setOnClickListener(this)
        ansB.setOnClickListener(this)
        ansC.setOnClickListener(this)
        ansD.setOnClickListener(this)
        ansE.setOnClickListener(this)
        ansF.setOnClickListener(this)
        submit_btn.setOnClickListener(this)
        final_btn.setOnClickListener(this)



        questionAnswer = QuestionAnswer(this)

        // Fetch questions from the API and receive the data in the callback

        val sharedP = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val language = sharedP.getString("language_selected"," ")
        val language_object = language?.let { language_selected(language = it) }
        if (language_object != null) {
            questionAnswer.fetchQuestionsFromApi(language_object) {
                totalQuestion = questionAnswer.quiz_getQuestions().size

                totalQuestionTextView.text = "Total questions: $totalQuestion"

                // Populate the UI with the fetched data
                loadNewQuestion()

                if(ansD.text.equals("---")){
                    ansD.visibility = View.INVISIBLE
                }
                else {
                    ansD.visibility = View.VISIBLE
                }

                if(ansE.text.equals("---")){
                    ansE.visibility = View.INVISIBLE
                }
                else{
                    ansE.visibility = View.VISIBLE
                }
                if(ansF.text.equals("---")){
                    ansF.visibility = View.INVISIBLE
                }
                else{
                    ansF.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onClick(view: View) {
        ansA.setBackgroundColor(Color.argb(0, 94, 109, 246))
        ansB.setBackgroundColor(Color.argb(0, 94, 109, 246))
        ansC.setBackgroundColor(Color.argb(0, 94, 109, 246))
        ansD.setBackgroundColor(Color.argb(0, 94, 109, 246))
        ansE.setBackgroundColor(Color.argb(0, 94, 109, 246))
        ansF.setBackgroundColor(Color.argb(0, 94, 109, 246))

        val clickedButton = view as Button
        if(clickedButton.id == R.id.final_Btn){
            if (selectedAnswer == questionAnswer.quiz_getCorrectAnswers()[currentQuestionInd]) {
                score++
            }
            answer = answer+":::("+selectedAnswer
            sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("posttest_score",score.toString()).apply()
            finishQuiz()
            return
        }
        if (clickedButton.id == R.id.submit_Btn) {
            if (selectedAnswer == questionAnswer.quiz_getCorrectAnswers()[currentQuestionInd]) {
                score++
            }
            answer = answer+":::("+selectedAnswer
            currentQuestionInd++
            loadNewQuestion()
        } else {
            // Choices button clicked
            selectedAnswer = clickedButton.text.toString()
            clickedButton.setBackgroundColor(Color.MAGENTA)
        }
    }

    private fun loadNewQuestion() {

        if(totalQuestion - currentQuestionInd == 1){
            final_btn.visibility = View.VISIBLE
            submit_btn.visibility = View.INVISIBLE
        }
        else{
            final_btn.visibility = View.INVISIBLE
            submit_btn.visibility = View.VISIBLE
        }

        questionTextView.text = questionAnswer.quiz_getQuestions()[currentQuestionInd]
        ansA.text = questionAnswer.quiz_getOptions()[currentQuestionInd][0]
        ansB.text = questionAnswer.quiz_getOptions()[currentQuestionInd][1]
        ansC.text = questionAnswer.quiz_getOptions()[currentQuestionInd][2]
        ansD.text = questionAnswer.quiz_getOptions()[currentQuestionInd][3]
        ansE.text = questionAnswer.quiz_getOptions()[currentQuestionInd][4]
        ansF.text = questionAnswer.quiz_getOptions()[currentQuestionInd][5]
        questionTextView.text = questionAnswer.quiz_getQuestions()[currentQuestionInd]

        if(ansD.text.equals("---")){
            ansD.visibility = View.INVISIBLE
        }
        else {
            ansD.visibility = View.VISIBLE
        }

        if(ansE.text.equals("---")){
            ansE.visibility = View.INVISIBLE
        }
        else{
            ansE.visibility = View.VISIBLE
        }
        if(ansF.text.equals("---")){
            ansF.visibility = View.INVISIBLE
        }
        else{
            ansF.visibility = View.VISIBLE
        }
    }

    private fun finishQuiz() {
        val passStatus: String = if (score > totalQuestion * 0.60) {
            "Passed"
        } else {
            "Failed"
        }
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(passStatus)
            .setMessage("Score is $score out of $totalQuestion and answers are $answer")
            .setCancelable(false)
            .show()

        // Delay the intent using Handler
        Handler().postDelayed({
            alertDialog.dismiss()

            val intent = Intent(this@quiz_postest, Drawer::class.java)
            intent.putExtra("score", score.toString())
            startActivity(intent)
            finish()
        }, 5000) // 5000 milliseconds = 5 seconds
        val retrofit = Retrofit.Builder()
            .baseUrl("http://165.22.212.47/api/score_posttest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var scoreApi = retrofit.create(myapi_posttest::class.java)
        //call shared prefernces
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val login = sharedPref.getString("email", "")
        val credentials = credential_scores(login.toString(),score.toString(),answer)
        scoreApi.send_score(credentials).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if(response.code() == 200){
                        //showToast("Score sent")
                        val shared = getSharedPreferences("QuizPreferences", Context.MODE_PRIVATE)
                        shared.edit().putBoolean(Drawer.KEY_LOCK_POST, true).apply()
                        sharedPref.edit().putString("status","0").apply()
                    }
                    else{
                        showToast("Score not sent")
                    }
                }
                else{
                    showToast("Request Failed")
                    //showToast(login.toString())
                    //showToast(response.code().toString())
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Failed To connect to server")
            }
        })
    }

    private fun restartQuiz() {
        score = 0
        currentQuestionInd = 0
        loadNewQuestion()
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}