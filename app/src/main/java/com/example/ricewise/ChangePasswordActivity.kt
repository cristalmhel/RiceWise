package com.example.ricewise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)

data class ResetPasswordResponse(
    val message: String
)

class ChangePasswordActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val email = intent.getStringExtra("email").toString()
        val newPassword = findViewById<EditText>(R.id.newPasswordInput)
        val confirmPassword = findViewById<EditText>(R.id.confirmPasswordInput)
        val submitButton = findViewById<Button>(R.id.submitPasswordButton)

        submitButton.setOnClickListener {
            val newPass = newPassword.text.toString()
            val confirmPass = confirmPassword.text.toString()

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else if (newPass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Implement actual password update logic
//                startActivity(Intent(this, ChangePasswordSuccessActivity::class.java))
//                finish()
                handleResetPassword(email, newPass)
            }
        }

        val backToLogin = findViewById<TextView>(R.id.backToLogin)

        backToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun handleResetPassword(mail: String, newPassword: String) {
        if (mail.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter email and new password", Toast.LENGTH_SHORT).show()
        } else {
            val request = ResetPasswordRequest(email = mail, newPassword = newPassword)
            RetrofitClient.apiService.resetPassword(request).enqueue(object :
                Callback<ResetPasswordResponse> {
                override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                    if (response.isSuccessful) {
                        startActivity(Intent(this@ChangePasswordActivity, ChangePasswordSuccessActivity::class.java))
                        finish()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Reset password failed"
                        Toast.makeText(this@ChangePasswordActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    Toast.makeText(this@ChangePasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}