package com.example.navchenta_welcome.navigation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.navchenta_welcome.R

class resources : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)
        val click_website = findViewById<TextView>(R.id.click1)
        val click_youtube = findViewById<TextView>(R.id.click2)
        val click_instagram = findViewById<TextView>(R.id.click3)

        click_website.setOnClickListener {
            openUrlInBrowser("https://www.spym.org/")
        }
        click_youtube.setOnClickListener {
            openUrlInBrowser("https://www.youtube.com")
        }
        click_instagram.setOnClickListener {
            openUrlInBrowser("https://www.instagram.com")
        }
    }
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}