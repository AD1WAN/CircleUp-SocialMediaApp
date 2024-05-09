package com.example.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.Models.User
import com.example.socialmediaapp.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(options: FirestoreRecyclerOptions<User>, val context: Context)
    : FirestoreRecyclerAdapter<User, SearchAdapter.SearchViewHolder>(options) {

        class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userImage: CircleImageView = itemView.findViewById(R.id.profile_image)
            val userName: TextView = itemView.findViewById(R.id.user_name)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false )
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int, user: User) {
        holder.userName.text = user.name

        Glide.with(context)
            .load(user.imageUrl)
            .placeholder(R.drawable.person_icon)
            .centerCrop()
            .into(holder.userImage)
    }
}