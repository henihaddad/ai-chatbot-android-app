package com.example.chatgptandroidapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.chatgptandroidapp.R

class SettingsActivity : AppCompatActivity() {
    private var isThemeChanging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (isThemeChanging) {
            isThemeChanging = false
            return
        }
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val selectedModel = sharedPreferences.getString("SelectedModel", "DefaultModel")
        val radioGroupModels = findViewById<RadioGroup>(R.id.radioGroupModels)

        when (selectedModel){
            "gpt-4-0125-preview" -> radioGroupModels.check(R.id.radioButtonModel0)
            "gpt-4-vision-preview" -> radioGroupModels.check(R.id.radioButtonModel1)
            "gpt-3.5-turbo-1106" -> radioGroupModels.check(R.id.radioButtonModel2)
            "gpt-3.5-turbo" -> radioGroupModels.check(R.id.radioButtonModel3)
        }


        radioGroupModels.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val modelPreference = radioButton.text.toString()

            saveModelPreference(modelPreference)
        }

        val spinnerThemeColors = findViewById<Spinner>(R.id.spinnerThemeColors)
        val colors = arrayOf("Blue", "Red", "Green") // Add more colors as needed
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerThemeColors.adapter = adapter

        spinnerThemeColors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view != null) {
                    val selectedColor = parent.getItemAtPosition(position).toString()
                    saveThemeColor(selectedColor)
                    applyThemeColor(selectedColor)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish() // Closes the current activity
        }
    }
    private fun saveModelPreference(model: String) {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("SelectedModel", model).apply()
    }
    private fun saveThemeColor(color: String) {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val currentColor = sharedPreferences.getString("ThemeColor", "")
        if (currentColor != color) {
            sharedPreferences.edit().putString("ThemeColor", color).apply()
        }
    }

    private fun applyThemeColor(color: String) {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val currentColor = sharedPreferences.getString("ThemeColor", "")
        if (currentColor != color) {
            when (color) {
                "Blue" -> setTheme(R.style.AppTheme_Blue)
                "Red" -> setTheme(R.style.AppTheme_Red)
                "Green" -> setTheme(R.style.AppTheme_Green)
            }

            isThemeChanging = true
            recreate() // Recreate the activity to apply the theme change
        }
    }
}
