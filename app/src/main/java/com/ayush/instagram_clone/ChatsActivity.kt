package com.ayush.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.adapters.chatAdapter
import com.ayush.instagram_clone.databinding.ActivityChatsBinding
import com.ayush.instagram_clone.utils.CHAT
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userlist: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        database = FirebaseDatabase.getInstance()
        userlist=ArrayList()

        binding.backImage.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_slide_out_right)
        }

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

            var user : User = it.toObject<User>()!!

            val userEmailWithoutDomain = user.email?.replace("@gmail.com", "")

            database.reference.child(userEmailWithoutDomain+ CHAT).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userlist.clear()
                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(User::class.java)
                        userlist.add(user!!)
                    }
                    binding.userListRv.adapter = chatAdapter(this@ChatsActivity,userlist)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }



    }
}