package com.example.navchenta_welcome.bottom.ui.dashboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.se.omapi.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.navchenta_welcome.Section_main
import com.example.navchenta_welcome.Task
import com.example.navchenta_welcome.TaskDataSource
import com.example.navchenta_welcome.databinding.FragmentDashboardBinding
import com.example.navchenta_welcome.flag_credentials
import com.example.navchenta_welcome.myapi_session
import com.example.navchenta_welcome.myapi_sessionTrainer
import com.example.navchenta_welcome.sessionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val goToTrainerButton: Button = binding.create
        val gotojoinButton: Button = binding.button2

        sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)
        val flag = sharedPreferences.getString("flag_login", "user")

        if (flag=="user"){
            gotojoinButton.visibility = View.VISIBLE
            goToTrainerButton.visibility = View.GONE
            gotojoinButton.setOnClickListener {
                val pretest_score = sharedPreferences.getString("pretest_score", "0")
                if(pretest_score=="-1"){
                    showToast("Please take the pretest first before joining a session")
                }
                else{
                    val intent = Intent(requireContext(), Section_main::class.java)
                    startActivity(intent)
                }
            }
        }
        else if(flag=="trainer"){
            gotojoinButton.visibility = View.GONE
            goToTrainerButton.visibility = View.VISIBLE
            goToTrainerButton.setOnClickListener {
                // Start TrainerActivity when the button is clicked
                val intent = Intent(requireContext(), Section_main::class.java)
                startActivity(intent)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)

        val email = sharedPreferences.getString("email"," ")
        val flag = sharedPreferences.getString("flag_login", "user")
        val email_obj = email?.let { flag_credentials(it) }

        if(flag=="user") {

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
                    }
                    override fun onResponse(
                        call: Call<sessionResponse>,
                        response: Response<sessionResponse>
                    ) {
                        if (response.isSuccessful) {
                            showToast("Inside")
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
                                    Toast.makeText(requireContext(),"No sessions available",Toast.LENGTH_SHORT).show()
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

    private fun showToast(s: String) {
        Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
    }
}