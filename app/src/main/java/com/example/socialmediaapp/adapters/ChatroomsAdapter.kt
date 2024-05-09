package com.example.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.Chatroom
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Ui.ChatFragment
import com.example.socialmediaapp.Ui.MainActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatroomsAdapter(options: FirestoreRecyclerOptions<Chatroom>, val context: Context):
    FirestoreRecyclerAdapter<Chatroom, ChatroomsAdapter.ChatroomsViewHolder>(options) {

    class ChatroomsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val chatroomsName: TextView = itemView.findViewById(R.id.chatroom_name)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatroom_item, parent, false)
        return ChatroomsViewHolder(itemView)
            }

    override fun onBindViewHolder(holder: ChatroomsViewHolder, position: Int, chatroom: Chatroom) {
        holder.chatroomsName.text = chatroom.name

        holder.itemView.setOnClickListener {

            ChatFragment.show(
                fragmentManager = (context as MainActivity).supportFragmentManager,
                chatroomId = chatroom.id)
        }
    }
}