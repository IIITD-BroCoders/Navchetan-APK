package com.spym.navchenta_welcome.navigation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.spym.navchenta_welcome.R
import com.spym.navchenta_welcome.about_us_iiitd

class about_us : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        val about_devloper =findViewById<TextView>(R.id.aboutus_iiitd_description)
        about_devloper.setOnClickListener {
            val intent = Intent(this, about_us_iiitd::class.java)
            startActivity(intent)
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
}