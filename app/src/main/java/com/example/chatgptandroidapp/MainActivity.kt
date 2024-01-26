package com.example.chatgptandroidapp

import ChatResponse
import OpenAIApiService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.example.chatgptapp.ChatAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var inputEditText: EditText
    private var currentTheme: String? = null
    private lateinit var textToSpeech: TextToSpeech



    private fun testTTS() {
        speakText("Welcome to Chat GPT. Please type your message and press send button to start chatting")
    }


    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val new_theme = sharedPreferences.getString("ThemeColor", "Default")
        if (currentTheme == null) {
            currentTheme = new_theme
        } else if (currentTheme != new_theme) {
            currentTheme = new_theme
            recreate()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val themeColor = sharedPreferences.getString("ThemeColor", "Default")

        when (themeColor) {
            "Blue" -> setTheme(R.style.AppTheme_Blue)
            "Red" -> setTheme(R.style.AppTheme_Red)
            "Green" -> setTheme(R.style.AppTheme_Green)
            else -> setTheme(R.style.Base_Theme_ChatGPTAndroidApp) // Default theme
        }
//        val themeColor = sharedPreferences.getInt("ThemeColor", Color.BLUE)
        setContentView(R.layout.activity_main)
        textToSpeech = TextToSpeech(this, this)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        inputEditText = findViewById(R.id.inputEditText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        chatAdapter = ChatAdapter(mutableListOf() )
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val messageText = inputEditText.text.toString()
            if (messageText.isNotEmpty()) {
                val userMessage = ChatMessage(messageText, true) // Assuming 'true' indicates a user message
                updateChatRecyclerView(userMessage)
                sendMessageToChatGPT(messageText)
                inputEditText.text.clear()
            }
        }
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                testTTS() // Remove this line after testing
            } else {
                Log.e("TTS", "Language not supported")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }
    private fun getSelectedModel(): String {
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("SelectedModel", "DefaultModel") ?: "DefaultModel"
    }

    private fun sendMessageToChatGPT(userMessage: String) {
        speakText(userMessage)
        val messages = listOf(Message("user", userMessage))
        val selectedModel= getSelectedModel()

        val requestBody = ChatRequestBody(messages = messages, model =selectedModel )

        RetrofitClient.retrofit.create(OpenAIApiService::class.java).postMessage(requestBody)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    if (response.isSuccessful) {
                        // Assuming the API response has the structure as shown in the documentation
                        val reply = response.body()?.choices?.firstOrNull()?.message?.content ?: "No response"
                        val responseMessage = ChatMessage(reply, false)
                        runOnUiThread {
                            updateChatRecyclerView(responseMessage)
                        }
                        speakText(responseMessage.text)
                    } else {
                        val responseMessage = ChatMessage("Sorry we are having problems now please get back later", false)
                        runOnUiThread {
                            updateChatRecyclerView(responseMessage)

                        }
                        speakText(responseMessage.text)
                        println("Failed to send message to ChatGPT with error code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    println("Failed to send message to ChatGPT")
                }
            })
    }

    private fun updateChatRecyclerView(message: ChatMessage) {
        chatAdapter.addMessage(message)

        chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }
    private fun speakText(text: String) {
        if (::textToSpeech.isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

}
