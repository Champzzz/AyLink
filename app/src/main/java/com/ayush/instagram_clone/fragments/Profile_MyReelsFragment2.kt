package com.ayush.instagram_clone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.Reels
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.MyPostRvAdapter
import com.ayush.instagram_clone.adapters.MyReelRvAdapter
import com.ayush.instagram_clone.databinding.FragmentProfileMyReelsBinding
import com.ayush.instagram_clone.utils.MY_RELS
import com.ayush.instagram_clone.utils.REELS
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class Profile_MyReelsFragment2 : Fragment() {

    private lateinit var binding: FragmentProfileMyReelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileMyReelsBinding.inflate(inflater, container, false)



        var Reels_list = ArrayList<Reels>()
        var myReels_adapter = MyReelRvAdapter (requireContext(),Reels_list)
        binding.MyReelRV.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.MyReelRV.adapter = myReels_adapter

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {


            var user: User = it.toObject<User>()!!

            Firebase.firestore.collection(user.email + REELS).get()
                .addOnSuccessListener {
                    var tempList = ArrayList<Reels>()

                    for (i in it.documents) {
                        var reel: Reels = i.toObject<Reels>()!!
                        tempList.add(reel)
                    }
                    Reels_list.addAll(tempList)
                    myReels_adapter.notifyDataSetChanged()
                }
        }


        return binding.root
    }

    companion object {

    }
}