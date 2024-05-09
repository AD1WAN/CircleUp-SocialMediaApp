package com.example.socialmediaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.Comment
import com.example.socialmediaapp.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.thunder413.datetimeutils.DateTimeStyle
import com.github.thunder413.datetimeutils.DateTimeUtils
import kotlin.reflect.KFunction0

class CommentsAdapter(
    options: FirestoreRecyclerOptions<Comment>,
    val showNullCommentsOrRecyclerView: KFunction0<Unit>
    ):
    FirestoreRecyclerAdapter<Comment, CommentsAdapter.CommentViewHolder>(options) {

        class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
                val commentText: TextView = itemView.findViewById(R.id.comment_text)
                val commentAuthor: TextView = itemView.findViewById(R.id.comment_author)
                val commentTime: TextView = itemView.findViewById(R.id.comment_time)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, comment: Comment) {
        holder.commentText.text = comment.commentText
        holder.commentAuthor.text = comment.commentAuthor.name

        val date = DateTimeUtils.formatDate(comment.commentTime)
        val formattedDate = DateTimeUtils.formatWithStyle(date, DateTimeStyle.LONG)

        holder.commentTime.text = formattedDate
    }

    override fun onDataChanged() {
        super.onDataChanged()
        showNullCommentsOrRecyclerView
    }
}