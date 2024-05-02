package com.ayush.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.adapters.Profile_ViewPagerAdapter
import com.ayush.instagram_clone.databinding.ActivityHomeBinding
import com.ayush.instagram_clone.databinding.ActivityViewProfileBinding
import com.ayush.instagram_clone.fragments.Profile_MyPostsFragment
import com.ayush.instagram_clone.fragments.Profile_MyReelsFragment
import com.ayush.instagram_clone.utils.FOLLOW
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class View_Profile : AppCompatActivity() {

    private lateinit var binding: ActivityViewProfileBinding
    private lateinit var viewPagerAdapter: Profile_ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val receivedUid = intent.getStringExtra("uid")

        binding.ViewProfileBackimg.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.animate_swipe_right_enter, R.anim.animate_swipe_right_exit)
        }

        Firebase.firestore.collection(USER_NODE).document(receivedUid!!).get()
            .addOnSuccessListener {
                val user: User = it.toObject<User>()!!

                binding.profileNameDisplay.text = user.username

                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImageDisplay)
                } else {
                    Picasso.get().load(R.drawable.account_icon)
                        .into(binding.profileImageDisplay)
                }

                Firebase.firestore.collection(user.email + POST).get()
                    .addOnSuccessListener {
                        binding.profilePostsNumberDisplay.text = it.size().toString()
                    }

                viewPagerAdapter =
                    Profile_ViewPagerAdapter(this@View_Profile.supportFragmentManager)

                val myPostsFragment = Profile_MyPostsFragment.newInstance(receivedUid)
                val myReelsFragment = Profile_MyReelsFragment.newInstance(receivedUid)


                viewPagerAdapter.addFragments(myPostsFragment, "MY POST")
                viewPagerAdapter.addFragments(myReelsFragment, "MY REELS")
                binding.profileViewPager.adapter = viewPagerAdapter
                binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)

            }



    }



}
