package com.example.chatgptapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatgptandroidapp.ChatMessage
import com.example.chatgptandroidapp.R


class ChatAdapter(private val messages: MutableList<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textView)

        fun bind(message: ChatMessage) {
            textView.text = message.text
        }
    }
    companion object {
        private const val VIEW_TYPE_USER_MESSAGE = 1
        private const val VIEW_TYPE_CHATBOT_MESSAGE = 2
    }
    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.isUser) VIEW_TYPE_USER_MESSAGE else VIEW_TYPE_CHATBOT_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            VIEW_TYPE_USER_MESSAGE -> layoutInflater.inflate(R.layout.message_item_user, parent, false)
            VIEW_TYPE_CHATBOT_MESSAGE -> layoutInflater.inflate(R.layout.message_item_response, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return MessageViewHolder(view)
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size


    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

}
