package com.example.chatgptandroidapp

data class ChatRequestBody(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val temperature: Double = 0.7
)

data class Message(
    val role: String, // "user" or "assistant"
    val content: String
)