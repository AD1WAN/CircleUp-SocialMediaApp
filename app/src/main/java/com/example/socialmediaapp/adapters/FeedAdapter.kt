package com.example.socialmediaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.Models.Post
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Ui.CommentsActivity
import com.example.socialmediaapp.Ui.CommentsActivity.Companion.POST_ID
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.thunder413.datetimeutils.DateTimeStyle
import com.github.thunder413.datetimeutils.DateTimeUtils
import com.google.firebase.firestore.FirebaseFirestore

class FeedAdapter(options: FirestoreRecyclerOptions<Post>, val context: Context) :
    FirestoreRecyclerAdapter<Post, FeedAdapter.FeedViewHolder>(options) {

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.post_image)
        val postText: TextView = itemView.findViewById(R.id.post_text)
        val authorText: TextView = itemView.findViewById(R.id.post_author)
        val timeText: TextView = itemView.findViewById(R.id.post_time)
        val likeIcon: ImageView = itemView.findViewById(R.id.likes_icon)
        val likeCount: TextView = itemView.findViewById(R.id.like_count)
        val commentIcon: ImageView = itemView.findViewById(R.id.post_comment)
        val commentCount: TextView = itemView.findViewById(R.id.comment_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(p0: FeedViewHolder, p1: Int, p2: Post) {
        p0.postText.text = p2.text
        p0.authorText.text = p2.user.name

        val date = DateTimeUtils.formatDate(p2.time)
        val dateFormatted = DateTimeUtils.formatWithStyle(date, DateTimeStyle.LONG)

        p0.timeText.text = dateFormatted

        Glide.with(context)
            .load(p2.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(p0.postImage)

        p0.likeCount.text = p2.likersList.size.toString()

        val firestore = FirebaseFirestore.getInstance()
        val userId = p2.user.id


        fun setFilledLikeIcon() {
            p0.likeIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context, R.drawable.like_icon_filled
                )
            )
        }

        fun setOutlinedLikeIcon() {
            p0.likeIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.likes_icon_outline
                )
            )
        }

        if (p2.likersList.contains(userId)) {
            setFilledLikeIcon()
        } else {
            setOutlinedLikeIcon()
        }

        fun invertLike() {
            if (p2.likersList.contains(userId)) {
                p2.likersList.remove(userId)
                setOutlinedLikeIcon()
            } else {
                p2.likersList.add(userId.toString())
                setFilledLikeIcon()
            }
        }

        val postId = snapshots.getSnapshot(p1).id
        val postDocument = firestore.collection("Posts").document(postId)

        p0.likeIcon.setOnClickListener {
            invertLike()

            postDocument
                .set(p2).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Something went wrong. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        invertLike()
                    }
                }
        }

        p0.commentIcon.setOnClickListener {

            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra(POST_ID, postId)
            context.startActivity(intent)
        }

        postDocument.collection("Comments").get().addOnCompleteListener {
            if (it.isSuccessful) {
                p0.commentCount.text = it.result?.size().toString()
            }
        }

    }


}