package com.spym.navchenta_welcome


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Feedback_refrence_page : AppCompatActivity() {
//    private lateinit var click_website: TextView
//    private lateinit var click_youtube: TextView
//    private lateinit var click_instagram: TextView
//    val click_website = findViewById<TextView>(R.id.click_website)
//    val click_youtube = findViewById<TextView>(R.id.feedback)
//    val click_instagram = findViewById<TextView>(R.id.send_your_message)
    private lateinit var next_button : Button
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
        setContentView(R.layout.activity_feedback_refrence_page)
        val click_website = findViewById<TextView>(R.id.click_website)
        val click_youtube = findViewById<TextView>(R.id.feedback)
        val click_instagram = findViewById<TextView>(R.id.send_your_message)
        val click_issup = findViewById<TextView>(R.id.send_your_message2)
        next_button = findViewById(R.id.next_to_feedback)
        click_website.setOnClickListener {
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
                                //showToast("Status updated successfully now going to Website")
                                val websiteUrl = "https://www.spym.org/"
                                openUrlInBrowser(websiteUrl)
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
        click_youtube.setOnClickListener {
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
                                //showToast("Status updated successfully now going to Youtube")
                                val youtubeUrl = "https://www.youtube.com/@SPYMINDIA/about"
                                openUrlInBrowser(youtubeUrl)
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
                                                    showToast("Status updated successfully")
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
        click_instagram.setOnClickListener {
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
//                                showToast("Status updated successfully now going to feedback form")
                            val websiteUrl = "https://instagram.com/spymindia?igshid=NjIwNzIyMDk2Mg=="
                            openUrlInBrowser(websiteUrl)
//                                val intent = Intent(this@Feedback_refrence_page, feedback_form::class.java)
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
        click_issup.setOnClickListener{
            val websiteUrl = "https://www.issup.net/membership/apply"
            openUrlInBrowser(websiteUrl)
        }

        next_button.setOnClickListener {
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
                            if(status.toInt()==4){
//                                    showToast("Status updated successfully now going to feedback form")
//                            val websiteUrl = "https://www.google.com/"
//                            openUrlInBrowser(websiteUrl)
                                val intent = Intent(this@Feedback_refrence_page, Feed::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                showToast("Complete the reference page first before going to Feedback Page")
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showToast("Status not updated")
                }
            })
//            val intent = Intent(this, Feed::class.java)
//            startActivity(intent)
//            finish()
        }
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}