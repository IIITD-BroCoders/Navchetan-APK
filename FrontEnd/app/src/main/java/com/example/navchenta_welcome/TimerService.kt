package com.example.navchenta_welcome

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.IBinder

class TimerService : Service() {
    private lateinit var unlockHandler: Handler
    private lateinit var sharedPreferences: SharedPreferences

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        unlockHandler = Handler()
        sharedPreferences = getSharedPreferences("QuizPreferences", MODE_PRIVATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the timer to enable the posttest button after 30 seconds
        unlockHandler.postDelayed({
            val editor = sharedPreferences.edit()
            editor.putBoolean("postTestUnlocked", true)
            editor.apply()

            val timerIntent = Intent("TIMER_COMPLETE")
            sendBroadcast(timerIntent)
        }, 30000)

        // Return START_STICKY to ensure that the service restarts if it gets killed
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unlockHandler.removeCallbacksAndMessages(null)
    }
}











