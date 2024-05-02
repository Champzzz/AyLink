package com.ayush.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.adapters.MyPostRvAdapter
import com.ayush.instagram_clone.databinding.ActivityEditProfileBinding
import com.ayush.instagram_clone.databinding.ActivityMySavedPostsBinding
import com.ayush.instagram_clone.utils.SAVE
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MySaved_Posts : AppCompatActivity() {

    val binding by lazy {
        ActivityMySavedPostsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        var post_list = ArrayList<Post>()
        var myPost_adapter = MyPostRvAdapter(this@MySaved_Posts,post_list)
        binding.SavedPostRV.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.SavedPostRV.adapter = myPost_adapter

        binding.backbtn.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.animate_swipe_right_enter, R.anim.animate_swipe_right_exit)

        }


        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

            var user:User = it.toObject<User>()!!

            Firebase.firestore.collection(user.email + SAVE).get().addOnSuccessListener {

                var tempList = ArrayList<Post>()

                for (i in it.documents) {
                    var post: Post = i.toObject<Post>()!!
                    tempList.add(post)
                }
                post_list.addAll(tempList)
                myPost_adapter.notifyDataSetChanged()

            }

        }

    }
}