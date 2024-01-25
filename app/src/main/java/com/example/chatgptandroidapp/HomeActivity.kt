package com.example.chatgptandroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.chatgptandroidapp.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            // Start Chat Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
