
package com.spym.navchenta_welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.spym.navchenta_welcome.navigation.Drawer
//import com.example.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.PrintWriter
import java.io.StringWriter

class login : AppCompatActivity() {
    private lateinit var login_button: Button
    public lateinit var login_email: EditText
    private lateinit var login_password: EditText
    private lateinit var emailerrortext : TextView
    private lateinit var sign_up: Button
    public lateinit var login_import: String
    private val serverURL = "http://165.22.212.47/api/login/"
    private lateinit var myApiService: myapi
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPreferences: SharedPreferences
    private  var isLoggedIn: Boolean = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            navigateToDashboard()
            return
        }

        login_email = findViewById(R.id.login_email)
        login_password = findViewById(R.id.login_passward)
        login_button = findViewById(R.id.login_button)
        emailerrortext = findViewById(R.id.email_error_text)
        val go_to_signup=findViewById<TextView>(R.id.go_to_singup)

        val retrofit = Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApiService = retrofit.create(myapi::class.java)


        login_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (isEmailFormatValid(email)) {
                    emailerrortext.visibility = View.GONE
                } else {
                    emailerrortext.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isEmpty()) {
                    emailerrortext.visibility = View.GONE
                }
            }
        })

        login_button.setOnClickListener {
            val email = login_email.text.toString()
            login_import = email
            val password = login_password.text.toString()
//            showToast("Email: $email\nPassword: $password")
            val credentials = credentials(email, password)
            myApiService.checkCredentials(credentials).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string()
                        if(response.code()==200 && responseString!=null){
                            val jsonObject = JSONObject(responseString)
                            val sessionToken = jsonObject.getString("session_token")
                            val flag = jsonObject.getString("flag")
                            val status = jsonObject.getString("status")
                            val pretest_score = jsonObject.getString("pretest_score")
                            val posttest_score = jsonObject.getString("posttest_score")
                            val session_id = jsonObject.getString("session_id")
                            sharedPreferences.edit().putString("status", status).apply()
                            sharedPreferences.edit().putString("flag_login", flag).apply()
                            sharedPreferences.edit().putString("pretest_score",pretest_score).apply()
                            sharedPreferences.edit().putString("posttest_score",posttest_score).apply()
                            sharedPreferences.edit().putString("session_id",session_id).apply()


                            saveLoginStatus(true)
                            val intent = Intent(this@login, Drawer::class.java)
                            intent.putExtra("sessionToken",sessionToken.toString())
                            startActivity(intent)
                            finish()
                        }
                        else if (response.code() == 201) {
                            showToast("Email doesn't exist. Kindly signup first")
                            showToast(response.code().toString())
                        }
                    }
                    else showToast("Request Failed")
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val sw = StringWriter()
                    val pw = PrintWriter(sw)
                    t.printStackTrace(pw)
                    //showToast(sw.toString())
                    showToast("Failed to connect to the server")
                }
            })
        }
        go_to_signup.setOnClickListener {
            val intent = Intent(this, Sing_up::class.java)
            startActivity(intent)
        }
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val isAppInstalled = sharedPreferences.getBoolean("isAppInstalled", false)

        if (isLoggedIn && isAppInstalled) {
            val intent = Intent(this, Drawer::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.putString("email", login_email.text.toString())
            editor.apply()
        }
    }
    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("email", login_email.text.toString())
        editor.apply()
    }
    private fun navigateToDashboard() {
        val intent = Intent(this, Drawer::class.java)
        startActivity(intent)
        finish()
    }
    private fun isEmailFormatValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
