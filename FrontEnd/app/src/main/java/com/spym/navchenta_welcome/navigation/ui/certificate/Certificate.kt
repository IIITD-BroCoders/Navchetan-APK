package com.spym.navchenta_welcome.navigation.ui.certificate

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.spym.navchenta_welcome.R
import com.spym.navchenta_welcome.flag_credentials
import com.spym.navchenta_welcome.flag_receive
import com.spym.navchenta_welcome.generate_certificate
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Certificate : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var flagReceive: flag_receive
    private lateinit var generate_certificate_api: generate_certificate
//    private lateinit var generate_btn : Button

    val retrofit = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/flag_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/generate_certificate/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)
        val button: Button = findViewById(R.id.button)
        showToast("Certification")
        button.setOnClickListener {
            // Code to execute when the button is clicked
            flagReceive = retrofit.create(flag_receive::class.java) // Replace FlagReceive with the actual class/interface
            generate_certificate_api = retrofit2.create(generate_certificate::class.java)

            sharedPreferences = getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)
            val email = sharedPreferences.getString("email", "")
            val flag = flag_credentials(email.toString())

            flagReceive.send_email(flag).enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    // Rest of your code...
//                    showToast(response.code().toString())
                    val responseString = response.body()?.string()
                    if(response.code()==200 && responseString!=null){
//                        showToast("Certificate generated successfully")
                        val jsonObject = JSONObject(responseString)
                        val status = jsonObject.getString("status")
                        if(status.toInt()==4){
                            generate_certificate_api.send_email(flag).enqueue(object : retrofit2.Callback<ResponseBody>{
                                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
//                                    showToast(response.code().toString())
                                    val responseString = response.body()?.string()
                                    if(response.code()==200 && responseString!=null){
                                        showToast("Certificate generated successfully")
                                    }
                                    else{
                                        showToast("Not received response code 200")
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    // Rest of your code...
                                    showToast("Certificate generation failed")
                                }
                            })
//                            showToast("Certificate generated successfully")
                        }
                        else{
                            showToast("Complete the previous task first")
                        }
                    }
                    else{
                        showToast("Not received response code 200 guluguglu")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Rest of your code...
                    showToast("Certificate generation failed")
//                    showToast(response.code().toString())
                }
            })
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, com.spym.navchenta_welcome.navigation.Drawer::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    // Rest of your code...
}