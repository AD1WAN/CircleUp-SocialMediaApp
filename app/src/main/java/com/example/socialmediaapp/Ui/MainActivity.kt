package com.example.socialmediaapp.Ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.socialmediaapp.Interfaces.LogoutInterface
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Utlis.UserUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), LogoutInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }

        UserUtils.getCurrentUser()

        setFragment(FeedFragment())

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_nav_view)

        bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.feed_item -> {
                    setFragment(FeedFragment())
                }
                R.id.chatroom_item -> {
                    setFragment(ChatroomFragment())
                }
                R.id.profile_item -> {
                    setFragment(ProfileFragment())
                }
                R.id.search_item -> {
                    setFragment(SearchFragment())
                }
                else -> {
                    setFragment(FeedFragment())
                }
            }


                }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

     override fun logOut() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }
}