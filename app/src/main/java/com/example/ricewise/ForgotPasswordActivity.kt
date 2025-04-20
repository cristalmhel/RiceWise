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

class ForgotPasswordActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val resetBtn = findViewById<Button>(R.id.resetPasswordButton)
        val backToLogin = findViewById<TextView>(R.id.backToLogin)

        resetBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty()) {
                // TODO: Implement actual reset logic
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        backToLogin.setOnClickListener {
            finish() // or startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}