package com.spym.navchenta_welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
//import com.example.navchenta_welcome.navigation.Drawer
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Sing_up : AppCompatActivity() {

    private lateinit var name : EditText
    private lateinit var age : EditText
    private lateinit var phone_no:EditText
    private lateinit var Gender: EditText
//    private lateinit var name_of_school : EditText
    private lateinit var school_phone : EditText
    private lateinit var school_email : EditText
    private lateinit var add_school : EditText
    private lateinit var your_email : EditText
    private lateinit var state:EditText
    private lateinit var district : EditText
    private lateinit var password: EditText
    private lateinit var sign_button : Button
    private lateinit var emailerrortext : TextView

    private lateinit var sign_up_api : myapi_signup
    private val serverURL = "http://165.22.212.47/api/sign_up/"

    private lateinit var stateSpinner: Spinner
    private lateinit var districtSpinner: Spinner

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
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)


        var go_to_login =findViewById<TextView>(R.id.go_to_login)

        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        phone_no = findViewById(R.id.phone_no)
        Gender = findViewById(R.id.gender)
        school_phone = findViewById(R.id.school_phone)
        school_email = findViewById(R.id.school_email)
        add_school = findViewById(R.id.address_school)
        your_email = findViewById(R.id.your_email)
//        state = findViewById(R.id.state)
//        district = findViewById(R.id.district)
        stateSpinner = findViewById(R.id.stateSpinner)
        districtSpinner = findViewById(R.id.districtSpinner)
        password =findViewById(R.id.password)
        sign_button = findViewById(R.id.sign_up_button)
        emailerrortext = findViewById(R.id.email_error_text)
//new state district code added here
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


        val retrofit = Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        sign_up_api =retrofit.create(myapi_signup::class.java)


        school_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (isEmailFormatValid(email)) {
                    emailerrortext.visibility = View.GONE
                } else {
                    emailerrortext.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isEmpty()) {
                    emailerrortext.visibility = View.GONE
                }
            }
        })
        your_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (isEmailFormatValid(email)) {
                    emailerrortext.visibility = View.GONE
                } else {
                    emailerrortext.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isEmpty()) {
                    emailerrortext.visibility = View.GONE
                }
            }
        })

        sign_button.setOnClickListener {
            val name = name.text.toString()
            val age = age.text.toString()
            val phonenumber = phone_no.text.toString()
            val gender = Gender.text.toString()
//            val schoolname = name_of_school.text.toString()
            val schoolphone = school_phone.text.toString()
            val schoolemail = school_email.text.toString()
            val schooladdress = add_school.text.toString()
            val emailid = your_email.text.toString()
//            val state = state.text.toString()
//            val district = district.text.toString()
            val state = stateSpinner.selectedItem.toString()
            val district = districtSpinner.selectedItem.toString()
            val password = password.text.toString()

            //showToast("Input taken successfully")

            val credentials_sign_up = credentials_sign_up(
                name, age, phonenumber, gender, schoolphone, schoolemail,
                schooladdress, emailid, state, district, password
            )
            sign_up_api.add_cred(credentials_sign_up).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string()
                        if (response.code() == 200 && responseString!=null){
//                            showToast("main yahan hun")
                            val jsonObject = JSONObject(responseString)
                            val OTP = jsonObject.getString("otp")
                            val intent = Intent(this@Sing_up, Verificationpage::class.java)
                            intent.putExtra("emailid",emailid.toString())
                            intent.putExtra("name",name.toString())
                            intent.putExtra("age",age.toString())
                            intent.putExtra("phonenumber",phonenumber.toString())
                            intent.putExtra("gender",gender.toString())
                            intent.putExtra("schoolphone",schoolphone.toString())
                            intent.putExtra("schoolemail",schoolemail.toString())
                            intent.putExtra("schooladdress",schooladdress.toString())
                            intent.putExtra("state",state.toString())
                            intent.putExtra("district",district.toString())
                            intent.putExtra("password",password.toString())
                            intent.putExtra("otp",OTP.toString())
                            startActivity(intent)
                            finish()

                        }
                        else if (response.code() == 201) {
                            showToast("Email Already existing")
                            showToast(response.code().toString())
                        }
                    }
                    else {
                        showToast("Request Failed")
                        showToast(response.code().toString())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showToast("Failed to connect to the server")
                }
            })







        }

        go_to_login.setOnClickListener {
                val intent = Intent(this, login::class.java)
                startActivity(intent)
            }
        val sharedPreferences = getSharedPreferences("navchetna_welcome", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putBoolean("isAppInstalled", true) // Store the flag indicating app installation
        editor.apply()
    }

    private fun isEmailFormatValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}