package com.example.chatgptandroidapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.chatgptandroidapp.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val themeColor = sharedPreferences.getString("ThemeColor", "Default")

        when (themeColor) {
            "Blue" -> setTheme(R.style.AppTheme_Blue)
            "Red" -> setTheme(R.style.AppTheme_Red)
            "Green" -> setTheme(R.style.AppTheme_Green)
            else -> setTheme(R.style.Base_Theme_ChatGPTAndroidApp) // Default theme
        }

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            // Start Chat Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
