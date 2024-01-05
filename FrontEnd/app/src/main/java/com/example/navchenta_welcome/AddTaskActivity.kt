package com.example.navchenta_welcome

import android.app.Activity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navchenta_welcome.databinding.ActivityAddTaskBinding
//import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var session_create_api: session_create
    private lateinit var stateSpinner : Spinner
    private lateinit var districtSpinner : Spinner

    private val states = arrayOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
        "Haryana", "Himachal Pradesh", "Jammu Kashmir", "Jharkhand", "Karnataka", "Kerala",
        "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
        "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
        "Uttarakhand", "West Bengal", "Andaman Nicobar", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu",
        "Delhi", "Lakshadweep", "Ladakh", "Puducherry"
    )




    private val districtsMap = mapOf(
        "Andhra Pradesh" to arrayOf(
            "Anantapur", "Chittoor", "East Godavari", "Alluri Sitarama Raju",
            "Anakapalli", "Annamaya", "Bapatla", "Eluru", "Guntur", "Kadapa",
            "Kakinada", "Konaseema", "Krishna", "Kurnool", "Manyam", "N T Rama Rao",
            "Nandyal", "Nellore", "Palnadu", "Prakasam", "Sri Balaji", "Sri Satya Sai",
            "Srikakulam", "Visakhapatnam", "Vizianagaram", "West Godavari"
        ),
        "Arunachal Pradesh" to arrayOf(
            "Anjaw", "Siang", "Changlang", "Dibang Valley", "East Kameng",
            "East Siang", "Kamle", "Kra Daadi", "Kurung Kumey", "Lepa Rada *",
            "Lohit", "Longding", "Lower Dibang Valley", "Lower Siang",
            "Lower Subansiri", "Namsai", "Pakke Kessang *", "Papum Pare",
            "Shi Yomi *", "Tawang", "Tirap", "Upper Siang", "Upper Subansiri",
            "West Kameng", "West Siang"
        ),
        "Assam" to arrayOf(
            "Baksa", "Barpeta", "Bongaigaon", "Cachar", "Charaideo", "Chirang",
            "Darrang", "Dhemaji", "Dhubri", "Dibrugarh", "Dima Hasao", "Goalpara",
            "Golaghat", "Hailakandi", "Hojai", "Jorhat", "Kamrup", "Kamrup Metropolitan",
            "Karbi Anglong", "Karimganj", "Kokrajhar", "Lakhimpur", "Majuli", "Morigaon",
            "Nagaon", "Nalbari", "Sivasagar", "Sonitpur", "South Salmara-Mankachar",
            "Tinsukia", "Udalguri", "West Karbi Anglong"
        ),
        "Bihar" to arrayOf(
            "Araria", "Arwal", "Aurangabad", "Banka", "Begusarai", "Bhagalpur",
            "Bhojpur", "Buxar", "Darbhanga", "East Champaran", "Gaya", "Gopalganj",
            "Jamui", "Jehanabad", "Kaimur", "Katihar", "Khagaria", "Kishanganj",
            "Lakhisarai", "Madhepura", "Madhubani", "Munger", "Muzaffarpur", "Nalanda",
            "Nawada", "Patna", "Purnia", "Rohtas", "Saharsa", "Samastipur", "Saran",
            "Sheikhpura", "Sheohar", "Sitamarhi", "Siwan", "Supaul", "Vaishali",
            "West Champaran"
        ),
        "Chhattisgarh" to arrayOf(
            "Balod", "Baloda Bazar", "Balrampur Ramanujganj*", "Bastar", "Bemetara",
            "Bijapur", "Bilaspur", "Dantewada", "Dhamtari", "Durg", "Gariaband",
            "Gaurela Pendra Marwahi*", "Janjgir Champa", "Jashpur", "Kabirdham",
            "Kanker", "Khairagarh", "Kondagaon", "Korba", "Koriya", "Mahasamund",
            "Manendragarh *", "Mohla Manpur *", "Mungeli", "Narayanpur", "Raigarh",
            "Raipur", "Rajnandgaon", "Sakti *", "Sarangarh Bilaigarh *", "Sukma",
            "Surajpur", "Surguja"
        ),
        "Goa" to arrayOf(
            "North Goa", "South Goa"
        ),
        "Gujarat" to arrayOf(
            "Ahmedabad", "Amreli", "Anand", "Aravalli", "Banaskantha", "Bharuch",
            "Bhavnagar", "Botad", "Chhota Udaipur", "Dahod", "Dang", "Devbhoomi Dwarka",
            "Gandhinagar", "Gir Somnath", "Jamnagar", "Junagadh", "Kheda", "Kutch",
            "Mahisagar", "Mehsana", "Morbi", "Narmada", "Navsari", "Panchmahal", "Patan",
            "Porbandar", "Rajkot", "Sabarkantha", "Surat", "Surendranagar", "Tapi",
            "Vadodara", "Valsad"
        ),
        "Haryana" to arrayOf(
            "Ambala", "Bhiwani", "Charkhi Dadri", "Faridabad", "Fatehabad", "Gurugram *",
            "Hisar", "Jhajjar", "Jind", "Kaithal", "Karnal", "Kurukshetra", "Mahendragarh",
            "Nuh *", "Palwal", "Panchkula", "Panipat", "Rewari", "Rohtak", "Sirsa",
            "Sonipat", "Yamunanagar"
        ),
        "Himachal Pradesh" to arrayOf(
            "Bilaspur", "Chamba", "Hamirpur", "Kangra", "Kinnaur", "Kullu",
            "Lahaul Spiti", "Mandi", "Shimla", "Sirmaur", "Solan", "Una"
        ),
        "Jammu Kashmir" to arrayOf(
            "Anantnag", "Bandipora", "Baramulla", "Budgam", "Doda", "Ganderbal",
            "Jammu", "Kathua", "Kishtwar", "Kulgam", "Kupwara", "Poonch", "Pulwama",
            "Rajouri", "Ramban", "Reasi", "Samba", "Shopian", "Srinagar", "Udhampur"
        ),
        "Jharkhand" to arrayOf(
            "Bokaro", "Chatra", "Deoghar", "Dhanbad", "Dumka", "East Singhbhum",
            "Garhwa", "Giridih", "Godda", "Gumla", "Hazaribagh", "Jamtara", "Khunti",
            "Koderma", "Latehar", "Lohardaga", "Pakur", "Palamu", "Ramgarh", "Ranchi",
            "Sahebganj", "Seraikela Kharsawan", "Simdega", "West Singhbhum"
        ),
        "Karnataka" to arrayOf(
            "Bagalkot", "Bangalore Rural", "Bangalore Urban", "Belgaum", "Bellary", "Bidar",
            "Chamarajanagar", "Chikkaballapur", "Chikkamagaluru", "Chitradurga", "Dakshina Kannada",
            "Davanagere", "Dharwad", "Gadag", "Kalaburagi", "Hassan", "Haveri", "Kodagu", "Kolar",
            "Koppal", "Mandya", "Mysore", "Raichur", "Ramanagara", "Shimoga", "Tumkur", "Udupi",
            "Uttara Kannada", "Vijayanagara *", "Vijayapura *", "Yadgir"
        ),
        "Kerala" to arrayOf(
            "Alappuzha", "Ernakulam", "Idukki", "Kannur", "Kasaragod", "Kollam", "Kottayam",
            "Kozhikode", "Malappuram", "Palakkad", "Pathanamthitta", "Thiruvananthapuram",
            "Thrissur", "Wayanad"
        ),
        "Madhya Pradesh" to arrayOf(
            "Agar Malwa", "Alirajpur", "Anuppur", "Ashoknagar", "Balaghat", "Barwani",
            "Betul", "Bhind", "Bhopal", "Burhanpur", "Chachaura *", "Chhatarpur", "Chhindwara",
            "Damoh", "Datia", "Dewas", "Dhar", "Dindori", "Guna", "Gwalior", "Harda", "Hoshangabad",
            "Indore", "Jabalpur", "Jhabua", "Katni", "Khandwa", "Khargone", "Maihar *", "Mandla",
            "Mandsaur", "Morena", "Nagda *", "Narsinghpur", "Neemuch", "Niwari *", "Panna", "Raisen",
            "Rajgarh", "Ratlam", "Rewa", "Sagar", "Satna", "Sehore", "Seoni", "Shahdol", "Shajapur",
            "Sheopur", "Shivpuri", "Sidhi", "Singrauli", "Tikamgarh", "Ujjain", "Umaria", "Vidisha"
        ),
        "Maharashtra" to arrayOf(
            "Ahmednagar", "Akola", "Amravati", "Aurangabad", "Beed", "Bhandara", "Buldhana",
            "Chandrapur", "Dhule", "Gadchiroli", "Gondia", "Hingoli", "Jalgaon", "Jalna", "Kolhapur",
            "Latur", "Mumbai City", "Mumbai Suburban", "Nagpur", "Nanded", "Nandurbar", "Nashik",
            "Osmanabad", "Palghar", "Parbhani", "Pune", "Raigad", "Ratnagiri", "Sangli", "Satara",
            "Sindhudurg", "Solapur", "Thane", "Wardha", "Washim", "Yavatmal"
        ),
        "Manipur" to arrayOf(
            "Bishnupur", "Chandel", "Churachandpur", "Imphal East", "Imphal West", "Jiribam",
            "Kakching", "Kamjong", "Kangpokpi", "Noney", "Pherzawl", "Senapati", "Tamenglong",
            "Tengnoupal", "Thoubal", "Ukhrul"
        ),
        "Meghalaya" to arrayOf(
            "East Garo Hills", "East Jaintia Hills", "East Khasi Hills", "Mairang (Eastern West Khasi Hills)",
            "North Garo Hills", "Ri Bhoi", "South Garo Hills", "South West Garo Hills", "South West Khasi Hills",
            "West Garo Hills", "West Jaintia Hills", "West Khasi Hills"
        ),
        "Mizoram" to arrayOf(
            "Aizawl", "Champhai", "Hnahthial", "Khawzawl", "Kolasib", "Lawngtlai", "Lunglei", "Mamit",
            "Saiha", "Saitual", "Serchhip"
        ),
        "Nagaland" to arrayOf(
            "Chumukedima *", "Dimapur", "Kiphire", "Kohima", "Longleng", "Mokokchung", "Mon", "Niuland *",
            "Noklak", "Peren", "Phek", "Shamator", "Tseminyu *", "Tuensang", "Wokha", "Zunheboto"
        ),
        "Odisha" to arrayOf(
            "Angul", "Balangir", "Balasore", "Bargarh", "Bhadrak", "Boudh", "Cuttack", "Debagarh", "Dhenkanal",
            "Gajapati", "Ganjam", "Jagatsinghpur", "Jajpur", "Jharsuguda", "Kalahandi", "Kandhamal",
            "Kendrapara", "Kendujhar", "Khordha *", "Koraput", "Malkangiri", "Mayurbhanj", "Nabarangpur",
            "Nayagarh", "Nuapada", "Puri", "Rayagada", "Sambalpur", "Subarnapur", "Sundergarh"
        ),
        "Punjab" to arrayOf(
            "Amritsar", "Barnala", "Bathinda", "Faridkot", "Fatehgarh Sahib", "Fazilka", "Firozpur",
            "Gurdaspur", "Hoshiarpur", "Jalandhar", "Kapurthala", "Ludhiana", "Malerkotla *", "Mansa", "Moga",
            "Mohali", "Muktsar", "Pathankot", "Patiala", "Rupnagar", "Sangrur", "Shaheed Bhagat Singh Nagar",
            "Tarn Taran"
        ),
        "Rajasthan" to arrayOf(
            "Ajmer", "Alwar", "Banswara", "Baran", "Barmer", "Bharatpur", "Bhilwara", "Bikaner",
            "Bundi", "Chittorgarh", "Churu", "Dausa", "Dholpur", "Dungarpur", "Sri Ganganagar",
            "Hanumangarh", "Jaipur", "Jaisalmer", "Jalore", "Jhalawar", "Jhunjhunu", "Jodhpur",
            "Karauli", "Kota", "Nagaur", "Pali", "Pratapgarh", "Rajsamand", "Sawai Madhopur",
            "Sikar", "Sirohi", "Tonk", "Udaipur"
        ),
        "Sikkim" to arrayOf(
            "East Sikkim", "North Sikkim", "Pakyong", "Soreng", "South Sikkim", "West Sikkim"
        ),
        "Tamil Nadu" to arrayOf(
            "Ariyalur", "Chengalpattu *", "Chennai", "Coimbatore", "Cuddalore", "Dharmapuri", "Dindigul",
            "Erode", "Kallakurichi *", "Kanchipuram", "Kanyakumari", "Karur", "Krishnagiri", "Madurai",
            "Mayiladuthurai*", "Nagapattinam", "Namakkal", "Nilgiris", "Perambalur", "Pudukkottai",
            "Ramanathapuram", "Ranipet*", "Salem", "Sivaganga", "Tenkasi *", "Thanjavur", "Theni",
            "Thoothukudi", "Tiruchirappalli", "Tirunelveli", "Tirupattur*", "Tiruppur", "Tiruvallur",
            "Tiruvannamalai", "Tiruvarur", "Vellore", "Viluppuram", "Virudhunagar"
        ),
        "Telangana" to arrayOf(
            "Adilabad", "Bhadradri Kothagudem", "Hyderabad", "Jagtial", "Jangaon", "Jayashankar Bhupalpally",
            "Jogulamba Gadwal", "Kamareddy", "Karimnagar", "Khammam", "Komaram Bheem", "Mahabubabad",
            "Mahbubnagar", "Mancherial", "Medak", "Medchal Malkajgiri", "Mulugu *", "Nagarkurnool",
            "Nalgonda", "Narayanpet *", "Nirmal", "Nizamabad", "Peddapalli", "Rajanna Sircilla",
            "Ranga Reddy", "Sangareddy", "Siddipet", "Suryapet", "Vikarabad", "Wanaparthy", "Warangal",
            "Hanamkonda", "Yadadri Bhuvanagiri"
        ),
        "Tripura" to arrayOf(
            "Dhalai", "Gomati", "Khowai", "North Tripura", "Sepahijala", "South Tripura", "Unakoti", "West Tripura"
        ),
        "Uttar Pradesh" to arrayOf(
            "Agra", "Aligarh", "Prayagraj*", "Ambedkar Nagar", "Amethi *", "Amroha *", "Auraiya",
            "Azamgarh", "Baghpat", "Bahraich", "Ballia", "Balrampur", "Banda", "Barabanki", "Bareilly",
            "Basti", "Bhadohi", "Bijnor", "Budaun", "Bulandshahr", "Chandauli", "Chitrakoot", "Deoria",
            "Etah", "Etawah", "Ayodhya *", "Farrukhabad", "Fatehpur", "Firozabad", "Gautam Buddha Nagar",
            "Ghaziabad", "Ghazipur", "Gonda", "Gorakhpur", "Hamirpur", "Hapur *", "Hardoi", "Hathras *",
            "Jalaun", "Jaunpur", "Jhansi", "Kannauj", "Kanpur Dehat *", "Kanpur Nagar", "Kasganj *",
            "Kaushambi", "Kheri", "Kushinagar", "Lalitpur", "Lucknow", "Maharajganj", "Mahoba", "Mainpuri",
            "Mathura", "Mau", "Meerut", "Mirzapur", "Moradabad", "Muzaffarnagar", "Pilibhit", "Pratapgarh",
            "Raebareli", "Rampur", "Saharanpur", "Sambhal *", "Sant Kabir Nagar", "Shahjahanpur", "Shamli *",
            "Shravasti", "Siddharthnagar", "Sitapur", "Sonbhadra", "Sultanpur", "Unnao", "Varanasi"
        ),
        "Uttarakhand" to arrayOf(
            "Almora", "Bageshwar", "Chamoli", "Champawat", "Dehradun", "Haridwar", "Nainital",
            "Pauri", "Pithoragarh", "Rudraprayag", "Tehri", "Udham Singh Nagar", "Uttarkashi"
        ),
        "West Bengal" to arrayOf(
            "Alipurduar", "Bankura", "Birbhum", "Cooch Behar", "Dakshin Dinajpur", "Darjeeling",
            "Hooghly", "Howrah", "Jalpaiguri", "Jhargram", "Kalimpong", "Kolkata", "Malda",
            "Murshidabad", "Nadia", "North 24 Parganas", "Paschim Bardhaman", "Paschim Medinipur",
            "Purba Bardhaman", "Purba Medinipur", "Purulia", "South 24 Parganas", "Uttar Dinajpur"
        ),
        "Andaman Nicobar" to arrayOf(
            "Nicobar", "North Middle Andaman", "South Andaman"
        ),
        "Chandigarh" to arrayOf("Chandigarh"),
        "Dadra and Nagar Haveli and Daman and Diu" to arrayOf("Dadra and Nagar Haveli", "Daman", "Diu"),
        "Delhi" to arrayOf(
            "Central Delhi", "East Delhi", "New Delhi", "North Delhi", "North East Delhi",
            "North West Delhi", "Shahdara", "South Delhi", "South East Delhi", "South West Delhi", "West Delhi"
        ),
        "Lakshadweep" to arrayOf("Lakshadweep"),
        "Ladakh" to arrayOf("Kargil", "Leh"),
        "Puducherry" to arrayOf("Karaikal", "Mahe", "Puducherry", "Yanam")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stateSpinner = findViewById(R.id.state_spinner)
        districtSpinner = findViewById(R.id.district_spinner)
        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, states)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stateSpinner.adapter = stateAdapter

        val emptyDistricts = emptyArray<String>()
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emptyDistricts)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        districtSpinner.adapter = districtAdapter

        stateSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedState = states[position]
                val selectedDistricts = districtsMap[selectedState] ?: emptyArray()
                val newDistrictAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, selectedDistricts)
                newDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                districtSpinner.adapter = newDistrictAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.firstemail
                binding.tilDate.text = it.date
                binding.tilTimer.text = it.hour
                binding.secondEmail.text = it.secondemail
//                binding.districtName.text = it.district
//                binding.stateName.text = it.state
                val districtIndex = districtAdapter.getPosition(it.district)
                binding.districtSpinner.setSelection(districtIndex)

                // Set the selected item of the state spinner
                val stateIndex = stateAdapter.getPosition(it.state)
                binding.stateSpinner.setSelection(stateIndex)
                binding.address.text = it.address
                binding.batch.text = it.batch.toString()

            }
        }

        insertListeners()
    }
    fun Date.formatToYYYYMMDD(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(this)
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).formatToYYYYMMDD()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilTimer.editText?.setOnClickListener {
            val timerPiker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timerPiker.addOnPositiveButtonClickListener {
                val minute =
                    if (timerPiker.minute in 0..9) "0${timerPiker.minute}" else timerPiker.minute
                val hour = if (timerPiker.hour in 0..9) "0${timerPiker.hour}" else timerPiker.hour

                binding.tilTimer.text = "${hour}:${minute}"
            }

            timerPiker.show(supportFragmentManager, null)
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.buttonNewTask.setOnClickListener {
            val task = Task(
                firstemail = binding.tilTitle.text,
                hour = binding.tilTimer.text,
                date = binding.tilDate.text,
                secondemail = binding.secondEmail.text,
//                state = binding.stateName.text,
//                district = binding.districtName.text,
                state = binding.stateSpinner.selectedItem.toString(),
                district = binding.districtSpinner.selectedItem.toString(),
                address = binding.address.text,
                batch = binding.batch.text.toIntOrNull() ?: 0,
                id = intent.getIntExtra(TASK_ID, 0)
            )
//
//            showToast(binding.tilTimer.text.toString())
            //showToast(binding.tilDate.text.toString() + " " + binding.tilTimer.text.toString())
//            showToast(binding.tilTitle.text.toString())
//            binding.tillDate.
            val retrofit = Retrofit.Builder()
                .baseUrl("http://165.22.212.47/api/session_create/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val session_t = session_create_data(
                emailid_T1 = binding.tilTitle.text,
                datetime = binding.tilDate.text.toString() + " " + binding.tilTimer.text.toString(),
                emailid_T2 = binding.secondEmail.text,
//                state = binding.stateName.text,
//                district = binding.districtName.text,
                state = binding.stateSpinner.selectedItem.toString(),
                district = binding.districtSpinner.selectedItem.toString(),
                address = binding.address.text,
                capacity = binding.batch.text.toIntOrNull() ?: 0,
                status = "0"
//                id = intent.getIntExtra(TASK_ID, 0)
            )
            session_create_api = retrofit.create(session_create::class.java)
            session_create_api.send_session_details(session_t).enqueue(object : retrofit2.Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if(response.isSuccessful){
                        val responseString = response.body()?.string()
                        if(response.code()==200 && responseString!=null){
                            val jsonObject = JSONObject(responseString)
                            val message = jsonObject.getString("message")
                            //showToast(response.code().toString() + " " + message)
                        }
                        else{
                            //showToast(response.code().toString())
                        }
                        Toast.makeText(this@AddTaskActivity, "Session Created", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}