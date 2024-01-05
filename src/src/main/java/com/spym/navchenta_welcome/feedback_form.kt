package com.spym.navchenta_welcome
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.PrintWriter
import java.io.StringWriter

class feedback_form : AppCompatActivity(), View.OnClickListener {
    var score =0
    var totalQuestion=feedback_question(this).getQuestions().size
    var currentQuestionInd=0
    var selectedAnswer=""
    val sese = ""
    lateinit var questionTextView: TextView
    lateinit var ansA: Button
    lateinit var ansB: Button
    lateinit var ansC: Button
    lateinit var ansD: Button
    lateinit var submit_btn: Button
    lateinit var final_btn: Button
    lateinit var totalQuestionTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var feedback_object = feedback_question(this)
    lateinit var sharedPreferences_flag: SharedPreferences
    private lateinit var send_status_api: feedback_send_status_api
    private lateinit var flag_receiver: flag_receive
    private var answer: String = ""
    private lateinit var theory_answer:EditText
    private lateinit var feedback_eval: feedback_evaluation_api
    var theory = ""
    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/flag_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/status_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit3 = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/feedback_answer/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_quiz)
        sharedPreferences_flag = getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)
        totalQuestionTextView = findViewById<TextView>(R.id.total_question)
        questionTextView = findViewById<TextView>(R.id.question)
        ansA = findViewById<Button>(R.id.ans_A)
        ansB = findViewById<Button>(R.id.ans_B)
        ansC = findViewById<Button>(R.id.ans_C)
        ansD = findViewById<Button>(R.id.ans_D)
        submit_btn = findViewById<Button>(R.id.submit_Btn)
        final_btn = findViewById<Button>(R.id.final_Btn)
        ansA.setOnClickListener(this)
        ansB.setOnClickListener(this)
        ansC.setOnClickListener(this)
        ansD.setOnClickListener(this)
        submit_btn.setOnClickListener(this)
        final_btn.setOnClickListener(this)
        theory_answer = findViewById(R.id.element1)

        val sharedP = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val language = sharedP.getString("language_selected"," ")
        val language_object = language?.let { language_selected(language = it) }
        if (language_object != null) {
            feedback_object.fetchQuestionsFromApi(language_object) {
                totalQuestion = feedback_object.getQuestions().size

                totalQuestionTextView.text = "Total questions: $totalQuestion"

                // Populate the UI with the fetched data
                loadNewQuestion()
            }
        }

    }

    override fun onClick(view: View) {
        ansA.setBackgroundColor(Color.argb(0,94,109,246))
        ansB.setBackgroundColor(Color.argb(0,94,109,246))
        ansC.setBackgroundColor(Color.argb(0,94,109,246))
//        ansD.setBackgroundColor(Color.red(100))
        ansD.setBackgroundColor(Color.argb(0,94,109,246))
//        var theory =""
        val clickedButton = view as Button
        if (clickedButton.id == R.id.submit_Btn) {

//            if (selectedAnswer == feedback_question(this).valideAnswers[currentQuestionInd] ){
//                score++
//            }

//            val theory = theory_answer.text.toString()
            theory = theory_answer.text.toString()
            if(theory!=""){
                answer = answer+":::("+theory
                theory_answer.setText("")
            }
            else{
                answer = answer+":::("+selectedAnswer
            }
//            answer = answer+":::("+selectedAnswer
            currentQuestionInd++
            loadNewQuestion()
        } else {
            // Choices button clicked
            selectedAnswer = clickedButton.text.toString()
            clickedButton.setBackgroundColor(Color.GREEN)
        }
    }


//    fun loadNewQuestion(){
//        val element1: View = findViewById(R.id.element1)
//        val element2: View = findViewById(R.id.element2)
//        if(currentQuestionInd==totalQuestion){
//            finishQuiz()
//            return
//        }
//        showToast(feedback_object.getQuestions()[currentQuestionInd])
//
////        totalQuestionTextView.setText("Q"+(currentQuestionInd+1))
//        questionTextView.setText(feedback_question(this).getQuestions().toString())
//        ansA.setText(feedback_question(this).getOptions()[currentQuestionInd][0])
//        if(ansA.text.toString()=="lol"){
//            Log.d("test","success")
//            element1.visibility = View.VISIBLE
//            element2.visibility = View.INVISIBLE
//        }
//        else{
//            element2.visibility = View.VISIBLE
//            element1.visibility = View.INVISIBLE
//        }
//        ansB.setText(feedback_question(this).getOptions()[currentQuestionInd][1])
//        if(ansB.text.toString()=="lol"){
//            Log.d("test","success")
//            element1.visibility = View.VISIBLE
//            element2.visibility = View.INVISIBLE
//        }
//        else{
//            element2.visibility = View.VISIBLE
//            element1.visibility = View.INVISIBLE
//        }
//        ansC.setText(feedback_question(this).getOptions()[currentQuestionInd][2])
//        ansD.setText(feedback_question(this).getOptions()[currentQuestionInd][3])
//        questionTextView.setText(feedback_question(this).getQuestions()[currentQuestionInd])
//
//    }

    private fun loadNewQuestion() {
        val element1: View = findViewById(R.id.element1)
        val element2: View = findViewById(R.id.element2)
        if (currentQuestionInd >= totalQuestion) {
            submit_btn.visibility = View.INVISIBLE
            final_btn.visibility = View.VISIBLE
            finishQuiz()
            return
        }
//        if(totalQuestion-currentQuestionInd==1){
//            submit_btn.visibility = View.INVISIBLE
//            final_btn.visibility = View.VISIBLE
//        }
        else{
            submit_btn.visibility = View.VISIBLE
            final_btn.visibility = View.INVISIBLE
        }


        val questionsList = feedback_object.getQuestions()
        val optionsList = feedback_object.getOptions()

        if (currentQuestionInd < questionsList.size) {
            val currentQuestion = questionsList[currentQuestionInd]
            val currentOptions = optionsList[currentQuestionInd]

//            showToast(currentQuestion)

            questionTextView.text = currentQuestion
            ansA.text = currentOptions[0]
            ansB.text = currentOptions[1]
            ansC.text = currentOptions[2]
            ansD.text = currentOptions[3]

            if (currentOptions[1] == "lol") {
                Log.d("test", "success")
                element1.visibility = View.VISIBLE
                element2.visibility = View.INVISIBLE
            } else {
                element2.visibility = View.VISIBLE
                element1.visibility = View.INVISIBLE
            }
        } else {
            // Handle the case where currentQuestionInd is out of bounds
            finishQuiz()
            return
        }
    }

    private fun finishQuiz() {
        val passStatus: String = if (score > totalQuestion * 0.60) {
            "Success"
        } else {
            "Success"
        }

        AlertDialog.Builder(this)
            .setTitle(passStatus)
            .setMessage("Successfully completed final evaluation")
            .setPositiveButton("Go to next Page") { dialogInterface, i -> restartQuiz() }
            .setCancelable(false)
            .show()

//        val intent = Intent(this@feedback_form, resources::class.java)
//        startActivity(intent)
//        finish()
    }

    private fun restartQuiz() {
        score = 0
        currentQuestionInd = 0
//        loadNewQuestion()
      //  showToast("Inside restartQuiz function")
        send_status_api = retrofit.create(feedback_send_status_api::class.java)
//        flag_receiver = retrofit2.create(flag_receive::class.java)
        val email = sharedPreferences_flag.getString("email", "")
        val flag_email = flag_credentials(email.toString())
        feedback_eval = retrofit3.create(feedback_evaluation_api::class.java)

        val status = sharedPreferences_flag.getString("status", "0")
        //showToast("This is before first if condition "+status.toString())
        if(status!=null && status.toInt()==0){
            //showToast("This is inside first if condition "+status.toString())
            //showToast("Currently status is"+status)
            val status_send_object = feedback_send_status(email.toString(), "1")
            val eval_object = eval_data(email.toString(), answer)
            send_status_api.send_status(status_send_object).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    //showToast("Inside Response")
                    if (response.isSuccessful) {
                        //showToast("Response is very very successful")
                        val responseString = response.body()?.string()
                        if(response.code()==200 && responseString!=null){
                            val jsonObject = JSONObject(responseString)
                            val status_1 = jsonObject.getString("status")
                            //showToast("This is status_1 "+status_1)
                            if(status_1=="1"){
//                                val intent = Intent(this@feedback_form, LanguageSelectionActivity3::class.java)
//                                startActivity(intent)
//                                finish()
                                feedback_eval.send_eval(eval_object).enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                                        if (response.isSuccessful){
                                            val responseString = response.body()?.string()
                                            if(response.code()==200 && responseString!=null){
                                                val jsonObject = JSONObject(responseString)
                                                val msg = jsonObject.getString("message")
                                                //showToast(msg)
                                            }
                                            else if(response.code()==201){
                                                showToast("Error")
                                                showToast(response.code().toString())
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        showToast("Failed")
                                    }
                                })
                                val intent = Intent(this@feedback_form, Feedback_refrence_page::class.java)
                                startActivity(intent)
                                finish()
                                showToast("Final evaluation completed successfully going to Reference page")
                            }
                        }
                        else if (response.code() == 201) {
                            showToast("Error")
                            showToast(response.code().toString())
                        }
                    }
                    else {
                        //showToast("Response unsuccessful")
                        //showToast(response.code().toString())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val sw = StringWriter()
                    val pw = PrintWriter(sw)
                    t.printStackTrace(pw)
                    showToast(sw.toString())
                    showToast("Failed to connect to the server")
                }
            })
        }

    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}