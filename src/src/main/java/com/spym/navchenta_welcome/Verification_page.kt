package com.spym.navchenta_welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Verificationpage : AppCompatActivity() {

    private lateinit var ver_field: EditText
    private lateinit var ver_button: Button
    private val serverURL = "http://165.22.212.47/api/verify/"
    private val serverurl_2 ="http://165.22.212.47/api/sign_up"
    private lateinit var ver_api : myapi_verify
    private lateinit var sign_up_api : myapi_signup


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_page)
        ver_field = findViewById(R.id.ver_field)
        ver_button = findViewById(R.id.ver_button)



        val retrofit = Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val emailid = intent.getStringExtra("emailid").toString()
        val name = intent.getStringExtra("name").toString()
        val age = intent.getStringExtra("age").toString()
        val phonenumber = intent.getStringExtra("phonenumber").toString()
        val gender = intent.getStringExtra("gender").toString()
        val schoolphone = intent.getStringExtra("schoolphone").toString()
        val schoolemail = intent.getStringExtra("schoolemail").toString()
        val schooladdress = intent.getStringExtra("schooladdress").toString()
        val state = intent.getStringExtra("state").toString()
        val district = intent.getStringExtra("district").toString()
        val password = intent.getStringExtra("password").toString()


        val otp = intent.getStringExtra("otp").toString()

        val credentials_sign_up = credentials_sign_up(
            name, age, phonenumber, gender, schoolphone, schoolemail,
            schooladdress, emailid, state, district, password
        )

        ver_api = retrofit.create(myapi_verify::class.java)

        ver_field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredOTP = s.toString()
                if (enteredOTP == otp && enteredOTP.length==4) {
                    //showToast("You have been verified")
                    ver_api.ver_cred(credentials_sign_up).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                            if(response.isSuccessful){
                                if(response.code()==200)showToast("Signed up successfully")
                            }
                            else{
                                showToast("Request failed")
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            showToast("Failed to connect to the server")
                        }
                    })
                    val intent = Intent(this@Verificationpage, login::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    showToast("Invalid OTP")
                }
            }
        })

//        resend.setOnClickListener{
//            sign_up_api.add_cred(credentials_sign_up).enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
//                    if (response.isSuccessful) {
//                        val responseString = response.body()?.string()
//                        if (response.code() == 200 && responseString!=null){
//                            val jsonObject = JSONObject(responseString)
//                            val OTP = jsonObject.getString("otp")
//                            val intent = Intent(this@Verificationpage, Verificationpage::class.java)
//                            intent.putExtra("otp",OTP.toString())
//                            startActivity(intent)
//                            finish()
//                        }
//                        else if (response.code() == 201) {
//                            showToast("Email Already existing")
//                            showToast(response.code().toString())
//                        }
//                    }
//                    else {
//                        showToast("Request Failed")
//                        showToast(response.code().toString())
//                    }
//                }
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    showToast("Failed to connect to the server")
//                }
//            })
//        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}