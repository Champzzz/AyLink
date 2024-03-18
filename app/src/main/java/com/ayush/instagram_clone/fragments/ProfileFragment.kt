package com.ayush.instagram_clone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayush.instagram_clone.EditProfileActivity
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.Profile_ViewPagerAdapter
import com.ayush.instagram_clone.databinding.FragmentProfileBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var viewPagerAdapter: Profile_ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentProfileBinding.inflate(inflater, container, false)


        binding.ProfileEditProfile.setOnClickListener {
            startActivity(Intent(activity,EditProfileActivity::class.java))
        }

        viewPagerAdapter= Profile_ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(Profile_MyPostsFragment(),"MY POST")
        viewPagerAdapter.addFragments(Profile_MyReelsFragment(),"MY REELS")
        binding.profileViewPager.adapter = viewPagerAdapter
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)


        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val user:User = it.toObject<User>()!!
            binding.profileNameDisplay.text = user.name
            binding.profileBioDisplay.text = user.password
            if(!user.image.isNullOrEmpty()){
                Picasso.get().load(user.image).into(binding.profileImageDisplay)
            }
        }
    }
}