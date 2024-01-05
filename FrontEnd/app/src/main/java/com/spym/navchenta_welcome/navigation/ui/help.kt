package com.spym.navchenta_welcome.navigation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spym.navchenta_welcome.R
import com.spym.navchenta_welcome.navigation.Drawer

class help : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Drawer::class.java)
        startActivity(intent)
        finish()
    }
}