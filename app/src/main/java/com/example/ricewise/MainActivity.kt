package com.example.ricewise

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)
        bottomNav = findViewById(R.id.bottom_navigation)
        toolbar = findViewById(R.id.toolbar)

        val headerView = navView.getHeaderView(0)
        val avatarText = headerView.findViewById<TextView>(R.id.avatarText)
        val usernameText = headerView.findViewById<TextView>(R.id.usernameText)

        val userName = "Cristal Requierme" // Replace with actual user name
        val firstLetter = userName.first().uppercaseChar().toString()

        // Set initial
        avatarText.text = firstLetter
        usernameText.text = userName

        // Generate a consistent color from the user's name
        val bgColor = generateColorFromName(userName)

        // Set circular background with dynamic color
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(bgColor)
        }
        avatarText.background = shape


        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_appbar)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        // Set up the toggle to show the hamburger icon
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Default fragment
        replaceFragment(HomeFragment())

        // Bottom nav listener
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_variety -> replaceFragment(VarietiesFragment())
                R.id.nav_pest_disease -> replaceFragment(PestDiseaseFragment())
            }
            true
        }

        // Navigation drawer listener
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    finish()
                }
                R.id.nav_logout -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun generateColorFromName(name: String): Int {
        val colors = listOf(
            0xFFEF5350.toInt(), // red
            0xFFAB47BC.toInt(), // purple
            0xFF42A5F5.toInt(), // blue
            0xFF26A69A.toInt(), // teal
            0xFF66BB6A.toInt(), // green
            0xFFFFCA28.toInt(), // yellow
            0xFFFF7043.toInt(), // orange
            0xFF8D6E63.toInt()  // brown
        )

        val index = Math.abs(name.hashCode()) % colors.size
        return colors[index]
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}


