package com.example.socialmediaapp.Models

data class Post(
    val text: String = "",
    val imageUrl: String? = null,
    val user: User = User(),
    val time: Long = 0L,
    val likersList: MutableList<String> = mutableListOf()
)
