package com.example.socialmediaapp.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialmediaapp.Interfaces.AuthenticationInterface
import com.example.socialmediaapp.R

class AuthenticationActivity : AppCompatActivity(), AuthenticationInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_fragment_container, LoginFragment())
            .commit()
    }

    override fun onSuccessfulAuth() {
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}