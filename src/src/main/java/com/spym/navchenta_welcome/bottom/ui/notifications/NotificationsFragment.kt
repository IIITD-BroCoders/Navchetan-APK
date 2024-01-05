package com.spym.navchenta_welcome.bottom.ui.notifications

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spym.navchenta_welcome.databinding.FragmentNotificationsBinding
import com.spym.navchenta_welcome.flag_credentials
import com.spym.navchenta_welcome.flag_receive
import com.spym.navchenta_welcome.login
import com.spym.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var logout_button: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myapi: flag_receive

    var name : String = ""
    val email : String = ""
    var age : String = ""
    var phone : String = ""

    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://165.22.212.47/api/flag_receive/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val notificationsViewModel =
//            ViewModelProvider(this).get(NotificationsViewModel::class.java)
//
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", android.content.Context.MODE_PRIVATE)
        var email = sharedPreferences.getString("email", "")
        val flag_object = flag_credentials(email.toString())
        myapi = retrofit2.create(flag_receive::class.java)
        myapi.send_email(flag_object).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    if(response.code()==200 && responseString!=null){
                        val jsonObject = JSONObject(responseString)
                        name = jsonObject.getString("name")
                        email = jsonObject.getString("email")
                        age = jsonObject.getString("age")
                        phone = jsonObject.getString("phone")
                        val profileNameTextView: TextView = binding.profileName
                        val profileAgeTextView: TextView = binding.profileAge
                        val profileEmailTextView: TextView = binding.profileEmail
                        val profileContactNoTextView: TextView = binding.profileContactNo
                        profileNameTextView.text = name
                        profileAgeTextView.text = age
                        profileEmailTextView.text = email
                        profileContactNoTextView.text = phone
//                        showToast("Response Successful")
//                        val sessionToken = jsonObject.getString("session_token")
//                        val flag = jsonObject.getString("flag")
//                        sharedPreferences.edit().putString("flag_login", flag).apply()
//                        showToast(flag.toString())
//                        saveLoginStatus(true)
//                        val intent = Intent(this@login, Drawer::class.java)
//                        intent.putExtra("sessionToken",sessionToken.toString())
//                        startActivity(intent)
//                        finish()
                    }
                    else if (response.code() == 201) {
                        showToast("Email doesn't exist. Kindly signup first")
                        showToast(response.code().toString())
                    }
                }
                else showToast("Request Failed")
            }
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("flag_login", "user")
                editor.apply()
            }
        })

//        val profileNameTextView: TextView = binding.profileName
//        val profileAgeTextView: TextView = binding.profileAge
//        val profileEmailTextView: TextView = binding.profileEmail
//        val profileContactNoTextView: TextView = binding.profileContactNo

        // Set the text for the TextViews
//        profileNameTextView.text = name
//        profileAgeTextView.text = age
//        profileEmailTextView.text = email
//        profileContactNoTextView.text = phone

        // ... (rest of your code)

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    val intent = Intent(requireContext(), Drawer::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onStart() {
        super.onStart()
            logout_button = binding.logout
            logout_button.setOnClickListener {
                sharedPreferences.edit().clear().apply()
                val intent = Intent(requireContext(), login::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

    }
    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}