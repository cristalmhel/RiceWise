package com.example.ricewise

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

// Data classes for request and response
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullname: String,
    val region: String,
    val province: String,
    val city: String,
    val barangay: String
)

data class RegisterResponse(
    val id: String,
    val email: String
)
class SignUpActivity : Activity() {
    private val apiService = RetrofitClient.apiService
    private lateinit var spinnerRegion: Spinner
    private lateinit var spinnerProvince: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var spinnerBarangay: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val fullName = findViewById<EditText>(R.id.fullNameInput)
        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val backToLogin = findViewById<TextView>(R.id.backToLogin)

        spinnerRegion = findViewById(R.id.spinnerRegion)
        spinnerProvince = findViewById(R.id.spinnerProvince)
        spinnerCity = findViewById(R.id.spinnerCity)
        spinnerBarangay = findViewById(R.id.spinnerBarangay)

        val json = loadJSONFromAsset(this, "ph-addresses.json")
        json?.let {
            val type = object : TypeToken<Map<String, Region>>() {}.type
            val regionsMap: Map<String, Region> = Gson().fromJson(it, type)

            // Convert map values to a list for easy handling in the spinner
            val regionsList = regionsMap.values.toList()
            val regionNames = listOf("Select Region") + regionsList.mapNotNull { it.regionName }

            // Set up Region Spinner
            spinnerRegion.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                regionNames
            )

            spinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Get the selected region
                    val selectedRegion = regionsList[if (position - 1 < 0) 0 else position - 1]
                    val provinces = listOf("Select Province") + selectedRegion.provinceList.keys.toList()  // Getting province names

                    // Set up Province Spinner
                    spinnerProvince.adapter = ArrayAdapter(
                        this@SignUpActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        provinces
                    )

                    spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            // Get the selected province
                            val selectedProvinceName = provinces[position]
                            val selectedProvince = selectedRegion.provinceList[selectedProvinceName]
                            val cities = listOf("Select City/Municipality") + (selectedProvince?.municipalityList?.keys?.toList() ?: listOf())

                            // Set up City Spinner
                            spinnerCity.adapter = ArrayAdapter(
                                this@SignUpActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                cities
                            )

                            spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    // Get the selected city
                                    val selectedCityName = cities[position]
                                    val selectedCity = selectedProvince?.municipalityList?.get(selectedCityName)
                                    val barangays = listOf("Select Barangay") + (selectedCity?.barangayList ?: listOf())

                                    // Set up Barangay Spinner
                                    spinnerBarangay.adapter = ArrayAdapter(
                                        this@SignUpActivity,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        barangays
                                    )
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        registerBtn.setOnClickListener {
            val name = fullName.text.toString().trim()
            val mail = email.text.toString().trim()
            val pass = password.text.toString().trim()

            val selectedRegion = spinnerRegion.selectedItem.toString()
            val selectedProvince = spinnerProvince.selectedItem.toString()
            val selectedCity = spinnerCity.selectedItem.toString()
            val selectedBarangay = spinnerBarangay.selectedItem.toString()

            if (
                name.isEmpty() ||
                mail.isEmpty() ||
                pass.isEmpty() ||
                selectedRegion == "Select Region" ||
                selectedProvince == "Select Province" ||
                selectedCity == "Select City/Municipality" ||
                selectedBarangay == "Select Barangay"
                ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
//                startActivity(Intent(this@SignUpActivity, SignUpSuccessActivity::class.java))
//                finish()
                // TODO: Register logic
                val request = RegisterRequest(
                    email = mail,
                    password = pass,
                    fullname = name,
                    region = selectedRegion,
                    province = selectedProvince,
                    city = selectedCity,
                    barangay = selectedBarangay
                )
                apiService.register(request).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            // Save user data (optional, for session management)
                            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                            sharedPreferences.edit().apply {
                                putString("user_id", response.body()?.id)
                                putString("email", response.body()?.email)
                            }.apply()

                            startActivity(Intent(this@SignUpActivity, SignUpSuccessActivity::class.java))
                            finish()
                            Toast.makeText(this@SignUpActivity, "Account created for $name", Toast.LENGTH_SHORT).show()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: "Registration failed"
                            Toast.makeText(this@SignUpActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        backToLogin.setOnClickListener {
            finish() // or startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun loadJSONFromAsset(context: Context, filename: String): String? {
        return try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}