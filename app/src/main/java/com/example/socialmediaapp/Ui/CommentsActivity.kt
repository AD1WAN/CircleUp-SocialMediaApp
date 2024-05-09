package com.example.socialmediaapp.Ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.Comment
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Utlis.UserUtils
import com.example.socialmediaapp.adapters.CommentsAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class CommentsActivity : AppCompatActivity() {

    private lateinit var commentsRV:RecyclerView
    private var postid: String? = null

    private var commentsAdapter: CommentsAdapter? = null

    companion object{
        val POST_ID = "POST_ID"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        if (intent.hasExtra(POST_ID)) {
            postid = intent.getStringExtra(POST_ID)
        }

        commentsRV = findViewById(R.id.comments_rv)

        setUpRecyclerView()

        val commentET: EditText = findViewById(R.id.enter_comment)
        val sendIcon: ImageView = findViewById(R.id.send_comment)

        sendIcon.setOnClickListener {
            val commentText = commentET.text.toString()

            if (TextUtils.isEmpty(commentText)) {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }

            val firestore = FirebaseFirestore.getInstance()

            val comment = UserUtils.user?.let { curerntUser->
                Comment(commentText,curerntUser, System.currentTimeMillis())
            }

            postid?.let { postID ->
                comment?.let {
                    firestore.collection("Posts").document(postID)
                        .collection("Comments").document().set(comment)
                }
            }

            commentET.text.clear()
        }

    }

    private fun setUpRecyclerView() {
         val firestore = FirebaseFirestore.getInstance()

        val query = postid?.let { postID ->
            firestore.collection("Posts").document(postID)
                .collection("Comments").orderBy("commentTime")
        }

        val firestoreRecyclerOptions = query?.let {
            FirestoreRecyclerOptions.Builder<Comment>().setQuery(it, Comment::class.java).build()
        }

        commentsAdapter = firestoreRecyclerOptions?.let { CommentsAdapter(it, ::showNullCommentsOrRecyclerView) }

        commentsRV.adapter = commentsAdapter
        commentsRV.layoutManager = LinearLayoutManager(this)
    }

    private fun showNullCommentsOrRecyclerView(){
        if (commentsAdapter?.itemCount == 0){
            findViewById<TextView>(R.id.comments_rv).visibility = android.view.View.VISIBLE
            commentsRV.visibility = android.view.View.GONE
        } else {
            findViewById<TextView>(R.id.comments_rv).visibility = android.view.View.GONE
            commentsRV.visibility = android.view.View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        commentsAdapter?.startListening()
    }

    override fun onPause() {
        super.onPause()
        commentsAdapter?.stopListening()
    }
}