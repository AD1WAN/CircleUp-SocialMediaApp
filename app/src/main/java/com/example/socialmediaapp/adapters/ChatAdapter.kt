package com.example.socialmediaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.Chat
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Utlis.UserUtils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>):
    FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options)  {

    companion object {
        const val MESSAGE_BY_SELF = 0
        const val MESSAGE_BY_OTHERS = 1
    }

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val chatText: TextView = itemView.findViewById(R.id.chat_text)
        val chatAuthor: TextView = itemView.findViewById(R.id.chat_author)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).author.id == UserUtils.user?.id) {
            MESSAGE_BY_SELF
        } else {
            MESSAGE_BY_OTHERS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var itemView: View? = null

        itemView = if (viewType == MESSAGE_BY_SELF) {
            LayoutInflater.from(parent.context).inflate(R.layout.self_chat_item, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.others_chat_item, parent, false)
        }
        return ChatViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, chat: Chat) {
        holder.chatText.text = chat.text
        holder.chatAuthor.text = chat.author.name
    }
}

