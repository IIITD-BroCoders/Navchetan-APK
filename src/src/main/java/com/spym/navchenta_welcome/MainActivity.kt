
package com.spym.navchenta_welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.spym.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var emailapi : flag_receive
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
//      val password = sharedPreferences.getString("password", "")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://165.22.212.47/api/flag_receive/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var flagApi = retrofit.create(flag_receive::class.java)
        val flag_email = flag_credentials(email.toString())
        flagApi.send_email(flag_email).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    if(response.code()==200 && responseString!=null){
                        val jsonObject = JSONObject(responseString)
                        val flag = jsonObject.getString("flag")
                        val posttest_score = jsonObject.getString("posttest_score")
                        val pretest_score = jsonObject.getString("pretest_score")
                        val status = jsonObject.getString("status")
                        val session_id = jsonObject.getString("session_id")
                        val editor = sharedPreferences.edit()
                        //showToast(posttest_score)
                        //showToast(pretest_score)
                        editor.putString("flag_login", flag)
                        editor.putString("posttest_score",posttest_score)
                        editor.putString("pretest_score",pretest_score)
                        editor.putString("status",status)
                        editor.putString("session_id",session_id)
                        editor.apply()
//                        showToast("session id is $session_id")
                        //showToast("Welcome boku no hero")
                    }
                }
                else{
                    showToast("Server Error")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val intent = Intent(this@MainActivity, login::class.java)
                startActivity(intent)
                finish()
            }
        })
        val isLoggedIn = checkIfLoggedIn()

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn) {
                val intent = Intent(this, Drawer::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, login::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000)

    }
    private fun checkIfLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        return isLoggedIn
    }

    override fun onStart() {
        super.onStart()

    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

