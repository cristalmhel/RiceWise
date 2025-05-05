package com.example.ricewise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val settingsIcon = findViewById<LinearLayout>(R.id.settingsIcon)
        settingsIcon.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}