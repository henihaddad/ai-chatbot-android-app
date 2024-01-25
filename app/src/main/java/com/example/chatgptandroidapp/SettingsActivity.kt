import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.chatgptandroidapp.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val blueThemeButton = findViewById<Button>(R.id.blueThemeButton)
        blueThemeButton.setOnClickListener {
            saveThemeColor(Color.BLUE)
            applyThemeColor()
        }
    }

    private fun saveThemeColor(color: Int) {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("ThemeColor", color)
        editor.apply()
    }


    private fun applyThemeColor() {
        // Apply the theme color to the activity or the entire app
    }
}
