package com.example.navchenta_welcome

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.PrintWriter
import java.io.StringWriter


class feedback_screen : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var send_status_api: feedback_send_status_api
    private lateinit var flag_receiver: flag_receive
    val retrofit = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/status_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/flag_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        setContentView(R.layout.feedback_screen)
    }

    fun onFinalEvaluationClick(view: View) {
        send_status_api = retrofit.create(feedback_send_status_api::class.java)
//        flag_receiver = retrofit2.create(flag_receive::class.java)

        val status = sharedPreferences.getString("status", "0")
//        showToast(status.toString())
        if(status!=null && status.toInt()==0){
            val email = sharedPreferences.getString("email", "")
//            showToast(email.toString())
            val status_send_object = feedback_send_status(email.toString(), "1")
            send_status_api.send_status(status_send_object).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string()
                        if(response.code()==200 && responseString!=null){
                            val jsonObject = JSONObject(responseString)
                            val status_1 = jsonObject.getString("status")
                            if(status_1=="1"){
                                val intent = Intent(this@feedback_screen, feedback_form::class.java)
                                startActivity(intent)
                                finish()
                                //showToast("Status updated successfully now going to final evaluation")
                            }
                        }
                        else if (response.code() == 201) {
                            showToast("Error")
                            showToast(response.code().toString())
                        }
                    }
                    else showToast(response.code().toString())
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
        // Add your code here for Final Evaluation button click
    }
// status receive from server
    // check status condition
    // send the updated status to the server
    fun onYouTubeClick(view: View) {
        flag_receiver = retrofit2.create(flag_receive::class.java)
        val email = sharedPreferences.getString("email", "")
        val flag_email = flag_credentials(email.toString())
        flag_receiver.send_email(flag_email).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    if(response.code()==200 && responseString!=null){
                        val jsonObject = JSONObject(responseString)
                        val status = jsonObject.getString("status")
                        if(status.toInt()==1){
                            //showToast("Status updated successfully now going to Youtube")
                            val youtubeUrl = "https://www.youtube.com/"
                            openUrlInBrowser(youtubeUrl)
                            send_status_api = retrofit.create(feedback_send_status_api::class.java)
                            val status_send_object = feedback_send_status(email.toString(), "2")
                            send_status_api.send_status(status_send_object).enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        val responseString = response.body()?.string()
                                        if(response.code()==200 && responseString!=null){
                                            val jsonObject = JSONObject(responseString)
                                            val status_1 = jsonObject.getString("status")
                                            if(status_1.toInt()==2){
                                                //showToast("Status updated successfully")
                                            }
                                            else{
                                                showToast("Status not updated")
                                            }
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    showToast("Status not updated")
                                }
                            })
                        }
                        else{
                            showToast("Complete the previous task first")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Status not updated")
            }
        })


    }

    fun onWebsiteClick(view: View) {
        flag_receiver = retrofit2.create(flag_receive::class.java)
        val email = sharedPreferences.getString("email", "")
        val flag_email = flag_credentials(email.toString())
        flag_receiver.send_email(flag_email).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    if(response.code()==200 && responseString!=null){
                        val jsonObject = JSONObject(responseString)
                        val status = jsonObject.getString("status")
                        if(status.toInt()==2){
                            //showToast("Status updated successfully now going to Website")
                            val websiteUrl = "https://www.google.com/"
                            openUrlInBrowser(websiteUrl)
                            send_status_api = retrofit.create(feedback_send_status_api::class.java)
                            val status_send_object = feedback_send_status(email.toString(), "3")
                            send_status_api.send_status(status_send_object).enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        val responseString = response.body()?.string()
                                        if(response.code()==200 && responseString!=null){
                                            val jsonObject = JSONObject(responseString)
                                            val status_1 = jsonObject.getString("status")
                                            if(status_1.toInt()==3){
                                                //showToast("Status updated successfully")
                                            }
                                            else{
                                                showToast("Status not updated")
                                            }
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    showToast("Status not updated")
                                }
                            })
                        }
                        else{
                            showToast("Complete the previous task first")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Status not updated")
            }
        })

    }

    fun onFeedbackFormClick(view: View) {
        // Add your code here for Feedback Form button click
        flag_receiver = retrofit2.create(flag_receive::class.java)
        val email = sharedPreferences.getString("email", "")
        val flag_email = flag_credentials(email.toString())
        flag_receiver.send_email(flag_email).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    if(response.code()==200 && responseString!=null){
                        val jsonObject = JSONObject(responseString)
                        val status = jsonObject.getString("status")
                        if(status.toInt()==3){
                            //showToast("Status updated successfully now going to feedback form")
//                            val websiteUrl = "https://www.google.com/"
//                            openUrlInBrowser(websiteUrl)
                            send_status_api = retrofit.create(feedback_send_status_api::class.java)
                            val status_send_object = feedback_send_status(email.toString(), "4")
                            send_status_api.send_status(status_send_object).enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        val responseString = response.body()?.string()
                                        if(response.code()==200 && responseString!=null){
                                            val jsonObject = JSONObject(responseString)
                                            val status_1 = jsonObject.getString("status")
                                            if(status_1.toInt()==4){
                                                //showToast("Status updated successfully")
                                            }
                                            else{
                                                showToast("Status not updated")
                                            }
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    showToast("Status not updated")
                                }
                            })
                        }
                        else{
                            showToast("Complete the previous task first")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Status not updated")
            }
        })
    }

    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}