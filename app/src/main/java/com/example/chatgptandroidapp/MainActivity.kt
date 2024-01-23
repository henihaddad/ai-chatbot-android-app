package com.example.chatgptandroidapp

import ChatRequestBody
import ChatResponse
import OpenAIApiService
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
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    }


    private fun sendMessageToChatGPT(message: String) {
        val requestBody = ChatRequestBody(message)

        RetrofitClient.retrofit.create(OpenAIApiService::class.java).postMessage(requestBody)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    if (response.isSuccessful) {
                        val reply = response.body()?.choices?.firstOrNull()?.text ?: "No response"
                        val responseMessage = ChatMessage(reply, false) // Assuming 'false' indicates a bot response
                        runOnUiThread {
                            updateChatRecyclerView(responseMessage)
                        }
                    } else {
                        println("response is not successful")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    println("response is not successful")
                    println(t.message.toString())}
            })
    }

    private fun updateChatRecyclerView(message: ChatMessage) {
        chatAdapter.addMessage(message)

        chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }



}
