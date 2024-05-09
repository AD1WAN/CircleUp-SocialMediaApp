package com.example.socialmediaapp.Ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialmediaapp.Interfaces.AuthenticationInterface
import com.example.socialmediaapp.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class LoginFragment : Fragment() {

    private var callback: AuthenticationInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = context as AuthenticationInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goToRegister: TextView = view.findViewById<TextView>(R.id.go_to_register)

        goToRegister.setOnClickListener{
           fragmentManager?.beginTransaction()
                ?.replace(R.id.auth_fragment_container, RegisterFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        val emailText: TextInputLayout = view.findViewById(R.id.email_text)
        val passwordText: TextInputLayout = view.findViewById(R.id.password_text)
        val loginButton: Button = view.findViewById(R.id.login_button)
        val loginProgress: ProgressBar = view.findViewById(R.id.login_progress)

        fun validateAndLoginUser(email: String,password: String) {

            if (TextUtils.isEmpty(email)){
                emailText.error = "Email is Required Field"
                return
            }

            if (TextUtils.isEmpty(password))  {
                passwordText.error = "Password is a Required Field"
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.error = "Please Enter a Valid Email"
                return
            }

            loginProgress.visibility = View.VISIBLE

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    loginProgress.visibility = View.GONE

                    if (task.isSuccessful){
                        callback?.onSuccessfulAuth()
                } else {
                        Toast.makeText(activity, "Something Went Wrong. Please Try Again", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        loginButton.setOnClickListener {
            val email = emailText.editText?.text.toString()
            val password = passwordText.editText?.text.toString()
            emailText.error = null
            passwordText.error = null

            validateAndLoginUser(email, password)
        }

    }
}