package com.example.socialmediaapp.Ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.Models.Post
import com.example.socialmediaapp.Models.User
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Utlis.UserUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreatePostActivity : AppCompatActivity() {

    private lateinit var selectImage: ImageView
    private  var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        selectImage = findViewById(R.id.select_image)

        val enterText = findViewById<EditText>(R.id.enter_text)
        val postButton = findViewById<Button>(R.id.post_button)

        selectImage.setOnClickListener {
           com.github.dhaval2404.imagepicker.ImagePicker.with(this)
               .crop()
               .compress(1024)
               .maxResultSize(1080,1080)
               .start()
        }

        postButton.setOnClickListener {
            val text = enterText.text.toString()

            if (TextUtils.isEmpty(text)) {
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addPost(text)
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                selectImage.setImageURI(fileUri)
                    imageUri = fileUri
                }
            com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR ->{
                Toast.makeText(this, com.github.dhaval2404.imagepicker.ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else -> {
            Toast.makeText(this, "Task Cancelled or some error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPost(text: String){


        val user = UserUtils.user

        if (user == null) {
            UserUtils.getCurrentUser {_user ->
                this.storeImageAndSavePost(_user, text)
            }
        } else {
            this.storeImageAndSavePost(user, text)
        }

    }

    private fun storeImageAndSavePost (user: User, text: String ) {
        val firestore = FirebaseFirestore.getInstance()

        val storage = FirebaseStorage.getInstance().reference.child("Images")
            .child(UserUtils.user?.email.toString() + "_" + System.currentTimeMillis() + ".jpg")

        val uploadTask = imageUri?.let {
            storage.putFile(it)
        }
        uploadTask?.continueWithTask { task1 ->
            if (!task1.isSuccessful) {
                Log.d("UploadTask", task1.exception.toString())
                task1.exception?.let {
                    throw it
                }
            }
            storage.downloadUrl
        }?.addOnCompleteListener { urlTask ->
            val downloadUri = urlTask.result

            val post = user.let {
                Post(text , downloadUri.toString(),
                    it, System.currentTimeMillis() )
            }

            post.let {
                firestore.collection("Posts").document().set(it)
                    .addOnCompleteListener { postTask ->
                        if (postTask.isSuccessful) {
                            Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error Occurred. Please try again", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}




