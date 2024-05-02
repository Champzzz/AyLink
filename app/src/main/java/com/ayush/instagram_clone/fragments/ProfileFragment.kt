package com.ayush.instagram_clone.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayush.instagram_clone.EditProfileActivity
import com.ayush.instagram_clone.LogInActivity
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.MySaved_Posts
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.Profile_ViewPagerAdapter
import com.ayush.instagram_clone.databinding.FragmentProfileBinding
import com.ayush.instagram_clone.utils.FOLLOW
import com.ayush.instagram_clone.utils.FOLLOWTHEM
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var viewPagerAdapter: Profile_ViewPagerAdapter

    var fabvisible = false

    lateinit var receivedUid :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentProfileBinding.inflate(inflater, container, false)


        viewPagerAdapter= Profile_ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(Profile_MyPostsFragment2(),"MY POST")
        viewPagerAdapter.addFragments(Profile_MyReelsFragment2(),"MY REELS")
        binding.profileViewPager.adapter = viewPagerAdapter
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)



        binding.MyProfileLogout.hide()
        binding.MyProfileSavedPost.hide()

        fabvisible = false
        binding.MyProfileMore.setOnClickListener{

            if(!fabvisible){
                binding.MyProfileLogout.show()
                binding.MyProfileSavedPost.show()

                binding.MyProfileLogout.visibility = View.VISIBLE
                binding.MyProfileSavedPost.visibility = View.VISIBLE

                binding.MyProfileMore.setImageDrawable(resources.getDrawable(R.drawable.expand_button))
                fabvisible=true
            }else{
                binding.MyProfileLogout.hide()
                binding.MyProfileSavedPost.hide()

                binding.MyProfileLogout.visibility = View.GONE
                binding.MyProfileSavedPost.visibility = View.GONE

                binding.MyProfileMore.setImageDrawable(resources.getDrawable(R.drawable.application))
                fabvisible=false
            }

        }


        binding.MyProfileLogout.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { dialog, which ->
                    // User clicked Yes, perform logout
                    Firebase.auth.signOut()
                    val intent = Intent(context, LogInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("No") { dialog, which ->
                    // User clicked No, dismiss the dialog
                    dialog.dismiss()
                }
                .show()


        }

        binding.MyProfileSavedPost.setOnClickListener {

            startActivity(Intent(context,MySaved_Posts::class.java))
            activity?.overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_swipe_left_exit)

        }




        binding.ProfileEditProfile.setOnClickListener {
            startActivity(Intent(activity,EditProfileActivity::class.java))
        }


        binding.ProfileShareProfile.setOnClickListener {

            var i = Intent(Intent.ACTION_SEND)
            i.type="text/plain"
            i.putExtra(Intent.EXTRA_TEXT,Firebase.auth.currentUser!!.uid)
            startActivity(i)

        }






        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val user:User = it.toObject<User>()!!
            binding.profileUsernameDisplay.text = user.username
            binding.MyProfileFullname.text = user.name

            var num_posts = Firebase.firestore.collection(user!!.email + POST).get().addOnSuccessListener{

                binding.profilePostsNumberDisplay.text = it.size().toString()

            }


            var num_followers = Firebase.firestore.collection(user.email + FOLLOWTHEM).get().addOnSuccessListener {

                binding.profileNumberFollowerDisplay.text = it.size().toString()

            }

            var num_following = Firebase.firestore.collection(user.email + FOLLOW).get().addOnSuccessListener {

                binding.profileNumberFollowingDisplay.text = it.size().toString()

            }


            if(!user.image.isNullOrEmpty()){
                Picasso.get().load(user.image).into(binding.profileImageDisplay)
            }



        }

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get().addOnSuccessListener {

            var j=0
            for (i in it.documents){
                j=j+1
            }
            binding.profileNumberFollowerDisplay.text=j.toString()

        }
    }
}