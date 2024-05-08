package com.ayush.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.adapters.show_followingAdapter
import com.ayush.instagram_clone.databinding.ActivityShowFlollowingBinding
import com.ayush.instagram_clone.utils.FOLLOW
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase

import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class Show_Flollowing : AppCompatActivity() {

    private lateinit var binding:ActivityShowFlollowingBinding
    private lateinit var list: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShowFlollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list= ArrayList()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

            var user:User = it.toObject<User>()!!

            Firebase.firestore.collection(user.email + FOLLOW).get().addOnSuccessListener {

                list.clear()
                for(i in it.documents) {
                    var user = i.toObject<User>()!!
                    list.add(user)
                }
                binding.showfollowingRv.adapter = show_followingAdapter(this@Show_Flollowing,list)

            }

        }

        binding.backImage.setOnClickListener {
            finish()
        }

    }
}