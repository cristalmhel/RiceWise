package com.example.ricewise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val message: String,
    val redirect: Boolean?
)
class ForgotPasswordActivity : Activity() {
    private val apiService = RetrofitClient.apiService
    private lateinit var emailInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailInput = findViewById(R.id.emailInput)
        val resetBtn = findViewById<Button>(R.id.resetPasswordButton)
        val backToLogin = findViewById<TextView>(R.id.backToLogin)

        resetBtn.setOnClickListener {
            handleForgotPassword()
            // startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        backToLogin.setOnClickListener {
            finish() // or startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun handleForgotPassword() {
        val mail = emailInput.text.toString().trim()
        if (mail.isEmpty()) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
        } else {
            val request = ForgotPasswordRequest(email = mail)
            RetrofitClient.apiService.forgotPassword(request).enqueue(object :
                Callback<ForgotPasswordResponse> {
                override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.redirect == true) {
                            val intent = Intent(this@ForgotPasswordActivity, ChangePasswordActivity::class.java)
                            intent.putExtra("email", mail)
                            startActivity(intent)
                        }
                        Toast.makeText(this@ForgotPasswordActivity, body?.message ?: "Operation successful", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Forgot password failed"
                        Toast.makeText(this@ForgotPasswordActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    Toast.makeText(this@ForgotPasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}