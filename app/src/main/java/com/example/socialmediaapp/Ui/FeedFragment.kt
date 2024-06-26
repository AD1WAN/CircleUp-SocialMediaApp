package com.example.socialmediaapp.Ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.Post
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapters.FeedAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class FeedFragment : Fragment() {

    private lateinit var feedRV: RecyclerView
    private var feedAdapter: FeedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<FloatingActionButton>(R.id.create_post_fab)

        fab.setOnClickListener {
            val intent = Intent( activity , CreatePostActivity::class.java)
            startActivity(intent)
        }

        feedRV = view.findViewById(R.id.feed_rv)

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Posts")

        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java).build()

        feedAdapter = context?.let {
                FeedAdapter(recyclerViewOptions, it)
        }

        feedRV.adapter = feedAdapter
        feedRV.layoutManager = LinearLayoutManager(activity)

        feedRV.itemAnimator = null
    }


    override fun onResume() {
        super.onResume()

        feedAdapter?.startListening()
    }

    override fun onPause() {
        super.onPause()
        feedAdapter?.stopListening()
    }

}