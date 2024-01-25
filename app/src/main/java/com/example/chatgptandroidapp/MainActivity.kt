package com.example.chatgptandroidapp

import ChatResponse
import OpenAIApiService
import SettingsActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.example.chatgptapp.ChatAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val themeColor = sharedPreferences.getInt("ThemeColor", Color.BLUE)
        setContentView(R.layout.activity_main)

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


    private fun sendMessageToChatGPT(userMessage: String) {
        val messages = listOf(Message("user", userMessage))
        val requestBody = ChatRequestBody(messages = messages)

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
                    } else {
                        // Handle API error
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    // Handle network error
                }
            })
    }

    private fun updateChatRecyclerView(message: ChatMessage) {
        chatAdapter.addMessage(message)

        chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }



}
