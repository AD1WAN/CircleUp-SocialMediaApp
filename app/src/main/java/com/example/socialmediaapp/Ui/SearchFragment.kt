package com.example.socialmediaapp.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.User
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Utlis.UserUtils
import com.example.socialmediaapp.adapters.SearchAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment() {

    private lateinit var searchRV: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        val toolbar: Toolbar = view.findViewById(R.id.search_toolbar)
        toolbar.title = "Search User"

        (activity as? MainActivity)?.setSupportActionBar(toolbar)
        (activity as? MainActivity)?.supportActionBar?.show()

        setHasOptionsMenu(true)

        val query = FirebaseFirestore.getInstance().collection("Users")
            .whereEqualTo("id", UserUtils.user?.id)

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java).build()

        searchAdapter = context?.let { SearchAdapter(firestoreRecyclerOptions, it) }!!

        searchRV = view.findViewById(R.id.search_rv)

        searchRV.adapter = searchAdapter
        searchRV.layoutManager = LinearLayoutManager(context)

        searchRV.itemAnimator = null

    }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_menu, menu)

        val searchView = SearchView(requireContext())
        menu.findItem(R.id.action_search).actionView = searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                val firestore = FirebaseFirestore.getInstance()

                val newQuery = firestore.collection("Users").whereEqualTo("name", searchQuery)
                    .whereNotEqualTo("id", UserUtils.user?.id)

                val newFirestoreREcyclerOptions = FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(newQuery, User::class.java).build()

                searchAdapter.updateOptions(newFirestoreREcyclerOptions)

                searchAdapter.notifyDataSetChanged()
                searchRV.visibility = View.VISIBLE

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchView.visibility = View.INVISIBLE
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        searchAdapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        searchAdapter.stopListening()
    }

}


