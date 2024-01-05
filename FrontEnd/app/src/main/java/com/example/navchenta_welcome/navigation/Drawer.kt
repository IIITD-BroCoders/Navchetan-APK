package com.example.navchenta_welcome.navigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.navchenta_welcome.LanguageSelectionActivity
import com.example.navchenta_welcome.LanguageSelectionActivity2
import com.example.navchenta_welcome.LanguageSelectionActivity3
import com.example.navchenta_welcome.R
import com.example.navchenta_welcome.TimerService
import com.example.navchenta_welcome.databinding.ActivityMain2Binding
import com.example.navchenta_welcome.feedback_form
import com.example.navchenta_welcome.feedback_screen
import com.example.navchenta_welcome.feedback_send_status
import com.example.navchenta_welcome.feedback_send_status_api
import com.example.navchenta_welcome.flag_credentials
import com.example.navchenta_welcome.flag_receive
import com.example.navchenta_welcome.login
import com.example.navchenta_welcome.navigation.ui.about_us
import com.example.navchenta_welcome.navigation.ui.certificate.Certificate
import com.example.navchenta_welcome.navigation.ui.help
import com.example.navchenta_welcome.quiz_page
import com.example.navchenta_welcome.quiz_postest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.PrintWriter
import java.io.StringWriter

class Drawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    private lateinit var pretest: ImageButton
    private lateinit var posttest: ImageButton
    private lateinit var feedback: ImageButton
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferences_flag: SharedPreferences
    private lateinit var flagReceive: flag_receive
    private lateinit var flag_receiver: flag_receive
    private lateinit var send_status_api: feedback_send_status_api
    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/flag_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/status_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

            companion object {
        private const val QUIZ_PREFERENCES = "QuizPreferences"
        const val KEY_LOCK_PRE = "lock_pre"
        const val KEY_LOCK_POST = "lock_post"
        const val KEY_PRETIME = "pretime"
        private const val KEY_POSTTIME = "posttime"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(QUIZ_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences_flag = getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)
        setSupportActionBar(binding.appBarMain.toolbar)
        pretest = findViewById(R.id.pretest_button)
        posttest = findViewById(R.id.posttest_button)
        feedback = findViewById(R.id.feedback)
        val flag_t = sharedPreferences_flag.getString("flag_login", "")
//        showToast(flag_t.toString())
        if(flag_t=="trainer"){
            pretest.isEnabled = false
            posttest.isEnabled = false
            feedback.isEnabled = false
        }
        pretest.setOnClickListener {
//            val lockPretest = sharedPreferences.getBoolean(KEY_LOCK_PRE, false)
//            if (sharedPreferences.get) {
//                showToast("You have already taken the pretest")
//            }
//             {
//                val pre_score = sharedPreferences_flag.getString("pretest_score", "o")
                if(sharedPreferences_flag.getString("pretest_score","p") != "-1"){
//                    val s = sharedPreferences_flag.getString("pretest_score", "p")
                    showToast("You have already taken the pretest")
                    return@setOnClickListener
                }
                else{
                    startActivity(Intent(this@Drawer, LanguageSelectionActivity::class.java))
                }
//            }
        }

//        posttest.setOnClickListener {
//            if(sharedPreferences.getBoolean(KEY_LOCK_POST, false)){
//                showToast("You have already taken the posttest")
//                return@setOnClickListener
//            }
//            if(!sharedPreferences.getBoolean(KEY_LOCK_PRE, false)){
//                showToast("You have to take the pretest first")
//                return@setOnClickListener
//            }
//            val time = System.currentTimeMillis()
//            val preTime = sharedPreferences.getLong(KEY_PRETIME, 0)
//            val diff = time - preTime
//
//            val diffInSec = diff / 1000
//            val diffInMin = diffInSec / 60
//            val diffInHr = diffInMin / 60
//            val diffInDay = diffInHr / 24
//            showToast(diffInSec.toString())
//            if (diffInSec < 30) {
//                showToast("LOCKED: You have to wait for 2 days before taking the posttest")
//            } else {
//                if(sharedPreferences_flag.getString("posttest_score", "") != "-1"){
//                    showToast("You have already taken the posttest")
//                    return@setOnClickListener
//                }
//                else{
//                    sharedPreferences.edit().putLong(KEY_POSTTIME, time).apply()
//                    startActivity(Intent(this@Drawer, LanguageSelectionActivity2::class.java))
//                }
//            }
//        }

        posttest.setOnClickListener {
            val post_score = sharedPreferences_flag.getString("posttest_score", "-1")
            val pre_score = sharedPreferences_flag.getString("pretest_score", "-1")
            if(pre_score == "-1"){
                showToast("You have to take the pretest first")
                return@setOnClickListener
            }
            if(post_score !="-1"){
                showToast("You have already taken the posttest")
                return@setOnClickListener
            }

            val time = System.currentTimeMillis()
            val preTime = sharedPreferences.getLong(KEY_PRETIME, 0)
            val diff = time - preTime
            showToast("Pretime ="+preTime.toString())

            val diffInSec = diff / 1000
            val diffInMin = diffInSec / 60
            val diffInHr = diffInMin / 60
            val diffInDay = diffInHr / 24
            showToast(diffInSec.toString())
            if (diffInSec < 30) {
                showToast("LOCKED: You have to wait for 2 days before taking the posttest")
                return@setOnClickListener
            }
            else {
                startActivity(Intent(this@Drawer, LanguageSelectionActivity2::class.java))
            }
        }


        feedback.setOnClickListener {
//            showToast(sharedPreferences_flag.getString("pretest", "").toString())
//            showToast(sharedPreferences_flag.getString("posttest", "").toString())
            val status = sharedPreferences_flag.getString("status", "").toString()
            if(sharedPreferences_flag.getString("pretest_score", "") == "-1" || sharedPreferences_flag.getString("posttest_score", "") == "-1"){
                showToast("You first have to take the pretest and posttest before giving feedback")
                return@setOnClickListener
            }
            if(status.toInt()>0){
                showToast("You have already given feedback")
                return@setOnClickListener
            }
            else{
                flag_receiver = retrofit2.create(flag_receive::class.java)
                val email = sharedPreferences_flag.getString("email", "")
                val flag_email = flag_credentials(email.toString())
                flag_receiver.send_email(flag_email).enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseString = response.body()?.string()
                            if(response.code()==200 && responseString!=null){
                                val jsonObject = JSONObject(responseString)
                                val status = jsonObject.getString("status")
//                                showToast("Showing current status")
//                                showToast(status.toString())
                                if(status.toInt()==0){
//                                    showToast("Status updated successfully now going to feedback form")
//                            val websiteUrl = "https://www.google.com/"
//                            openUrlInBrowser(websiteUrl)
                                    val intent = Intent(this@Drawer, LanguageSelectionActivity3::class.java)
                                    startActivity(intent)
                                    finish()
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
//                send_status_api = retrofit.create(feedback_send_status_api::class.java)
////        flag_receiver = retrofit2.create(flag_receive::class.java)
//
//                val status = sharedPreferences_flag.getString("status", "0")
////        showToast(status.toString())
//                if(status!=null && status.toInt()==0){
//                    val intent = Intent(this@Drawer, LanguageSelectionActivity3::class.java)
//                    startActivity(intent)
//                    finish()
////                    val email = sharedPreferences_flag.getString("email", "")
//////            showToast(email.toString())
////                    val status_send_object = feedback_send_status(email.toString(), "1")
////                    send_status_api.send_status(status_send_object).enqueue(object : Callback<ResponseBody> {
////                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
////                            if (response.isSuccessful) {
////                                val responseString = response.body()?.string()
////                                if(response.code()==200 && responseString!=null){
////                                    val jsonObject = JSONObject(responseString)
////                                    val status_1 = jsonObject.getString("status")
////                                    if(status_1=="1"){
////                                        val intent = Intent(this@Drawer, LanguageSelectionActivity3::class.java)
////                                        startActivity(intent)
////                                        finish()
////                                        showToast("Status updated successfully now going to final evaluation")
////                                    }
////                                }
////                                else if (response.code() == 201) {
////                                    showToast("Error")
////                                    showToast(response.code().toString())
////                                }
////                            }
////                            else {
////                                showToast("Response unsuccessful")
////                                showToast(response.code().toString())
////                            }
////                        }
////                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                            val sw = StringWriter()
////                            val pw = PrintWriter(sw)
////                            t.printStackTrace(pw)
////                            showToast(sw.toString())
////                            showToast("Failed to connect to the server")
////                        }
////                    })
//                }
//                startActivity(Intent(this@Drawer, LanguageSelectionActivity3::class.java))
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navView2: BottomNavigationView = drawerLayout.findViewById(R.id.nav_view1)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home,
//                R.id.nav_gallery,
//                R.id.nav_slideshow,
//                R.id.dashboardFragment,
//                R.id.notificationsFragment,
//                R.id.homeFragment,
//                R.id.nav_certificate,
//                R.id.nav_resources
//            ), drawerLayout
//        )




        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_resources,R.id.nav_about_us,R.id.nav_help,R.id.nav_certificate,R.id.dashboardFragment,R.id.notificationsFragment,R.id.homeFragment

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_certificate -> {
                    // Open the new activity here
                    showToast("Certificate")
                    val intent = Intent(this, Certificate::class.java)
                    startActivity(intent)
//                    sharedPreferences_flag = getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)
//                    val email = sharedPreferences_flag.getString("email", "")
//                    val flag = flag_credentials(email.toString())
//                    flagReceive.send_email(flag).enqueue(object : retrofit2.Callback<ResponseBody> {
//                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                            if (response.isSuccessful) {
//                                val responseString = response.body()?.string()
//                                if(response.code()==200 && responseString!=null){
//                                    val jsonObject = JSONObject(responseString)
//                                    val status = jsonObject.getString("status")
//                                    if(status.toInt()==5){
//                                        showToast("Certificate generated successfully")
//
//                                    }
//                                    else{
//                                        showToast("Complete the previous task first")
//                                    }
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                            showToast(response.code().toString())
//                            showToast("Certificate generation failed")
//                        }
//                    })
                }
                R.id.nav_resources -> {
//                    showToast("Certificate")
//                    showToast("Yo have clicked on resources")
                    // Handle Resources selection
                    val intent = Intent(this, about_us::class.java)
                    startActivity(intent)
                }
                R.id.nav_about_us -> {
//                    showToast("U have clicked on about us")
                    // Handle About Us selection
                    val intent = Intent(this, about_us::class.java)
                    startActivity(intent)
                }
                R.id.nav_help -> {
//                    showToast("U have clicked on help")
                    // Handle Help selection
                    val intent = Intent(this, help::class.java)
                    startActivity(intent)
                }
            }
            // Close the navigation drawer
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView2.setupWithNavController(navController)

    }


//        navView.setOnClickListener { view ->
//            when (view.id) {
//                R.id.nav_certificate -> {
////                    startActivity(Intent(this@Drawer, login::class.java))
//                    showToast("Certificate")
//                }
//            }
//        }
//        navView2.setOnClickListener { view ->
//        navView2.setOnClickListener{view ->
//            when(view.id){
//                R.id.nav_certificate -> {
//                    showToast("Certificate")
//                }
//            }
//        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
