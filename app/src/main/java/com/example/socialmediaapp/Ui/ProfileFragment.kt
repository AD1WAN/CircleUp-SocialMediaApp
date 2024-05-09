package com.example.socialmediaapp.Ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.socialmediaapp.Interfaces.LogoutInterface
import com.example.socialmediaapp.Models.User
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Utlis.UserUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {

    private lateinit var userImage: CircleImageView
    private lateinit var userName: EditText
    private lateinit var userBio: EditText

    private var imageUri: Uri? = null
    private var callback: LogoutInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LogoutInterface) {
            callback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userImage = view.findViewById(R.id.user_image)
        userName = view.findViewById(R.id.user_name)
        userBio = view.findViewById(R.id.user_bio)

        val saveButton: Button = view.findViewById(R.id.save_button)
        val logOutButton: Button = view.findViewById(R.id.logout_button)

        userName.setText(UserUtils.user?.name ?: "")
        userBio.setText(UserUtils.user?.bio ?: "")

        userImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        Glide.with(requireContext())
            .load(UserUtils.user?.imageUrl)
            .placeholder(R.drawable.person_icon)
            .centerCrop()
            .into(userImage)

        saveButton.setOnClickListener {
            val newUserName = userName.text.toString()
            val newBio = userBio.text.toString()


            if (newUserName.isBlank()) {
                Toast.makeText(context, "User Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                addImageUserandUpdateDetails()
            } else {
                val userDocument = FirebaseFirestore.getInstance().collection("Users")
                    .document(UserUtils.user?.id.toString())


                val user = User(
                    id = UserUtils.user?.id.toString(),
                    name = newUserName,
                    email = UserUtils.user?.email.toString(),
                    bio = newBio,
                    imageUrl = UserUtils.user?.imageUrl
                )

                userDocument.set(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                        UserUtils.getCurrentUser()
                    } else {
                        Toast.makeText(
                            context,
                            "Something went wrong. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        logOutButton.setOnClickListener {
            callback?.logOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (resultCode == Activity.RESULT_OK) {
            val fileUri = intentData?.data
            userImage.setImageURI(fileUri)
            imageUri = fileUri
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(intentData), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Something is wrong. Please try again", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun addImageUserandUpdateDetails() {
        val currentUserFirestoreDocument = FirebaseFirestore.getInstance().collection("Users")
            .document(UserUtils.user?.id.toString())


        currentUserFirestoreDocument.get()
            .addOnCompleteListener {
                val storage = FirebaseStorage.getInstance().reference.child("Images")
                    .child(UserUtils.user?.id.toString() + ".jpg")

                val uploadTask = imageUri?.let { imageUri ->
                    storage.putFile(imageUri)
                }

                uploadTask?.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        Log.d("Upload Task", task.exception.toString())
                        task.exception?.let {
                            throw it
                        }
                    }
                    storage.downloadUrl
                }?.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        val downloadUri = urlTask.result

                        val updatedName = if (userName.text.isBlank()) {
                            UserUtils.user?.name.toString()
                        } else {
                            userName.text.toString()
                        }


                        val newUser = User(
                            id = UserUtils.user?.id.toString(),
                            name = updatedName,
                            email = UserUtils.user?.email.toString(),
                            bio = userBio.text.toString(),
                            imageUrl = downloadUri.toString()
                        )


                        currentUserFirestoreDocument.set(newUser)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    UserUtils.getCurrentUser()
                                    Toast.makeText(
                                        context,
                                        "Image uploaded and Details updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    UserUtils.getCurrentUser()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong. Please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                    } else {
                        Toast.makeText(
                            context,
                            "Something went wrong. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}