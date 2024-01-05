package com.spym.navchenta_welcome

// Import statements
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spym.navchenta_welcome.navigation.Drawer

class LanguageSelectionActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var languageSpinner: Spinner
    private lateinit var btnSelectLanguage: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onBackPressed() {
        val intent = Intent(this, Drawer::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_selection)
        sharedPreferences = getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)

        languageSpinner = findViewById(R.id.languageSpinner)
        btnSelectLanguage = findViewById(R.id.btnSelectLanguage)

        // Set up the Spinner adapter and item selection listener
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.language,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter
        languageSpinner.onItemSelectedListener = this

        btnSelectLanguage.setOnClickListener{
            val selectedLanguage = languageSpinner.selectedItem.toString()
            val editor = sharedPreferences.edit()
//            editor.putBoolean("isLoggedIn", false)
            editor.putString("language_selected", selectedLanguage)
            editor.apply()
            //showToast(selectedLanguage)
            val intent = Intent(this, quiz_page::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Handle the selected item from the spinner if needed
        val selectedLanguage = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this, "Selected Language: $selectedLanguage", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle the case when nothing is selected (if needed)
    }

    fun onSelectLanguageButtonClick(view: View) {
        // Perform any additional actions when the "Select" button is clicked (if needed)
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
