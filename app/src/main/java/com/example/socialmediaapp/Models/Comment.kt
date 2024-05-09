package com.example.socialmediaapp.Models

data class Comment(
    val commentText: String = "",
    val commentAuthor: User = User(),
    val commentTime: Long = 0L

)
