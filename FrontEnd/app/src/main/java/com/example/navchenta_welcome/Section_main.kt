package com.example.navchenta_welcome

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navchenta_welcome.databinding.ActivityMainBinding
import com.example.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//import com.example.todolist.databinding.ActivityMainBinding

class Section_main : AppCompatActivity() {
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private lateinit var bt : Button
    private lateinit var sharedPreferences: SharedPreferences
    private var URL_DELETE = "http://165.22.212.47/api/session_delete_api/"
    private var URL_JOIN = "http://165.22.212.47/api/session_update/"

    override fun onBackPressed() {
        val intent = Intent(this, Drawer::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerTask.adapter = adapter
        upadteList()
//        bt = findViewById(R.id.fab)
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val flag = sharedPreferences.getString("flag_login", "")
        insertListeners()
        if (flag=="user"){
            binding.fab.visibility = View.GONE
        }
        else if(flag=="trainer"){
            binding.fab.visibility = View.VISIBLE
        }
    }


    public fun insertListeners() {

        val retrofit = Retrofit.Builder()
            .baseUrl(URL_DELETE) // Replace with the base URL of your Django API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofit2 = Retrofit.Builder()
            .baseUrl(URL_JOIN) // Replace with the base URL of your Django API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var deleteApi = retrofit.create(myapi_session_delete::class.java)
        var joinApi = retrofit2.create(myapi_session_join::class.java)
        val flag = sharedPreferences.getString("flag_login", "")
//        bt.setOnClickListener {
//            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
//        }
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
        if(flag == "trainer") {
            adapter.listenerDelete = {
                val id = it.id
                TaskDataSource.deleteTask(it)
//                showToast(it.id.toString())
                val id_obj = credential_delete(id.toString())
                if (id_obj != null) {
                    deleteApi.delete(id_obj).enqueue(object :
                        Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                showToast("Deleted")
                            } else {
                                showToast("Not Deleted")
                                showToast(response.code().toString())
                                showToast(response.message().toString())
                            }
                        }
                    })
                }
                upadteList()
            }
        }
        else{
            adapter.listenerDelete
        }
        adapter.listenerJoin = {
            if(flag == "user"){
                val id = it.id
                val email = sharedPreferences.getString("email"," ")
                val join_cred = join_creds(email.toString(),id)
                if (join_cred != null) {
                    joinApi.send_join(join_cred).enqueue(object :
                        Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        }
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                showToast("Successfully Joined the session")
                                sharedPreferences.edit().putString("session_id", id.toString()).apply()
                                val intent = Intent(this@Section_main, Drawer::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                showToast("Not Joined")
                                showToast(response.code().toString())
                                showToast(response.message().toString())
                            }
                        }
                    })
                }
                upadteList()
            }
            else{
                showToast("You are not a user")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) upadteList()

    }
    override fun onStart() {
        super.onStart()
        sharedPreferences = getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)

        val email = sharedPreferences.getString("email"," ")
        val flag = sharedPreferences.getString("flag_login", "user")
        val email_obj = email?.let { flag_credentials(it) }

        if(flag=="user") {
            binding.fab.visibility = View.GONE
            val retrofit = Retrofit.Builder()
                .baseUrl("http://165.22.212.47/api/session_get/") // Replace with the base URL of your Django API
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val sessionApi = retrofit.create(myapi_session::class.java)
            if (email_obj != null) {
                sessionApi.get_session(email_obj).enqueue(object :
                    Callback<sessionResponse> {
                    override fun onFailure(call: Call<sessionResponse>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<sessionResponse>,
                        response: Response<sessionResponse>
                    ) {
                        if (response.isSuccessful) {
                            TaskDataSource.deleteAll()
                            val session_dets = response.body()
                            session_dets?.let { response ->
                                var id = session_dets.session_id.split(":::(")
                                var email_t1 = session_dets.emailid_T1.split(":::(")
                                var email_t2 = session_dets.emailid_T2.split(":::(")
                                var state = session_dets.state.split(":::(")
                                var district = session_dets.district.split(":::(")
                                var address = session_dets.address.split(":::(")
                                var capacity = session_dets.capacity.split(":::(")
                                var date = session_dets.date.split(":::(")
                                var time = session_dets.time.split(":::(")
                                if(id[0].trim()==""){
                                    showToast("No Sessions")
                                    return
                                }
                                val id_before = sharedPreferences.getString("session_id", "-1")?.toInt()
                                if(id_before != -1){
                                    for (i in email_t1.indices) {
                                        if(id_before==Integer.parseInt(id[i].trim())){
                                            var task = Task(
                                                id = Integer.parseInt(id[i].trim()),
                                                firstemail = email_t1[i].trim(),
                                                secondemail = email_t2[i].trim(),
                                                state = state[i].trim(),
                                                district = district[i].trim(),
                                                address = address[i].trim(),
                                                batch = Integer.parseInt(capacity[i].trim()),
                                                date = date[i].trim(),
                                                hour = time[i].trim()
                                            )
                                            TaskDataSource.insertTask(task)
                                        }
                                    }
                                }
                                else{
                                    for (i in email_t1.indices) {
                                        if(Integer.parseInt(capacity[i].trim()) >=1){
                                            var task = Task(
                                                id = Integer.parseInt(id[i].trim()),
                                                firstemail = email_t1[i].trim(),
                                                secondemail = email_t2[i].trim(),
                                                state = state[i].trim(),
                                                district = district[i].trim(),
                                                address = address[i].trim(),
                                                batch = Integer.parseInt(capacity[i].trim()),
                                                date = date[i].trim(),
                                                hour = time[i].trim()
                                            )
                                            TaskDataSource.insertTask(task)
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
        else if(flag=="trainer"){
            binding.fab.visibility = View.VISIBLE
            val retrofit = Retrofit.Builder()
                .baseUrl("http://165.22.212.47/api/session_trainers/") // Replace with the base URL of your Django API
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val sessionApi = retrofit.create(myapi_sessionTrainer::class.java)
            if (email_obj != null) {
                sessionApi.get_TrainerSession(email_obj).enqueue(object :
                    Callback<sessionResponse> {
                    override fun onFailure(call: Call<sessionResponse>, t: Throwable) {
                        showToast("Failed")
                        showToast(t.message.toString())
                    }
                    override fun onResponse(
                        call: Call<sessionResponse>,
                        response: Response<sessionResponse>
                    ) {
                        if (response.isSuccessful) {

                            TaskDataSource.deleteAll()
                            val session_dets = response.body()
                            session_dets?.let { response ->
                                var id = session_dets.session_id.split(":::(")
                                var email_t1 = session_dets.emailid_T1.split(":::(")
                                var email_t2 = session_dets.emailid_T2.split(":::(")
                                var state = session_dets.state.split(":::(")
                                var district = session_dets.district.split(":::(")
                                var address = session_dets.address.split(":::(")
                                var capacity = session_dets.capacity.split(":::(")
                                var date = session_dets.date.split(":::(")
                                var time = session_dets.time.split(":::(")
                                if(id[0].trim()==""){
                                    showToast("No sessions")
                                    return
                                }
                                for (i in email_t1.indices) {
                                    var task = Task(
                                        id = Integer.parseInt(id[i].trim()),
                                        firstemail = email_t1[i].trim(),
                                        secondemail = email_t2[i].trim(),
                                        state = state[i].trim(),
                                        district = district[i].trim(),
                                        address = address[i].trim(),
                                        batch = Integer.parseInt(capacity[i].trim()),
                                        date = date[i].trim(),
                                        hour = time[i].trim()
                                    )
                                    TaskDataSource.insertTask(task)
                                }
                            }
                        }
                        else{
                            showToast("Not Successful")

                        }
                    }
                })
            }
        }
    }



    fun upadteList() {
        val list = TaskDataSource.getList()
        if (list.isEmpty()) {
            binding.emptyInclude.stateEmptyCs.visibility = View.VISIBLE
        } else {
            binding.emptyInclude.stateEmptyCs.visibility = View.GONE
        }
        adapter.submitList(list)
    }


    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

}




