package com.example.socialmediaapp.Ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.Models.Chatroom
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapters.ChatroomsAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class ChatroomFragment : Fragment() {

    private lateinit var chatroomRV: RecyclerView
    private lateinit var createChatroomFAB: FloatingActionButton

    private var chatroomsAdapter:ChatroomsAdapter? =  null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatroom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatroomRV = view.findViewById(R.id.chatroom_rv)

        createChatroomFAB = view.findViewById(R.id.create_chatroom)
        setUpChatroom()

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Chatrooms").orderBy("name")

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<Chatroom>()
            .setQuery(query, Chatroom::class.java).build()

        chatroomsAdapter = activity?.let {
            ChatroomsAdapter(firestoreRecyclerOptions, it) }

        chatroomRV.adapter = chatroomsAdapter
        chatroomRV.layoutManager = LinearLayoutManager(activity)

    }

    private fun setUpChatroom() {
        createChatroomFAB.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)

            val editText = EditText(context)
            val params = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.marginStart = dpToPixels(8f).toInt()
            params.marginEnd = dpToPixels(8f).toInt()

            editText.layoutParams = params
            editText.setPadding(0, dpToPixels(20f).toInt(), 0, dpToPixels(20f).toInt())

            alertDialog.setTitle("Create Chatroom")
            alertDialog.setMessage("Enter the Chatroom Name")

            alertDialog.setView(editText)

            var textEntered = ""

            alertDialog.setPositiveButton("Create") { _,_ ->
                textEntered = editText.text.toString()

                val document = FirebaseFirestore.getInstance().collection("Chatrooms").document()
                val chatroom = Chatroom( document.id, textEntered)
                document.set(chatroom)
            }

            alertDialog.setNegativeButton("Cancel") { dialogInterface,_ ->
                dialogInterface.dismiss()
            }

            alertDialog.show()
        }
    }

    private fun dpToPixels(dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue,
            resources.displayMetrics
        )
    }

    override fun onResume() {
        super.onResume()
        chatroomsAdapter?.startListening()
    }

    override fun onPause() {
        super.onPause()
        chatroomsAdapter?.stopListening()
    }

}

