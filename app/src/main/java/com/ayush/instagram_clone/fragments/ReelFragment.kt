package com.ayush.instagram_clone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayush.instagram_clone.Models.Reels
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.MyReelRvAdapter
import com.ayush.instagram_clone.adapters.Reel_Adapter
import com.ayush.instagram_clone.databinding.FragmentReelBinding
import com.ayush.instagram_clone.utils.MY_RELS
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class ReelFragment : Fragment() {

    private lateinit var binding:FragmentReelBinding
    lateinit var adapter: Reel_Adapter
    var Reels_List = ArrayList<Reels>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReelBinding.inflate(inflater,container,false)


        adapter = Reel_Adapter(requireContext(),Reels_List)
        binding.ReelFragmentViewpager.adapter = adapter

        Firebase.firestore.collection(MY_RELS).get().addOnSuccessListener {
            var tempList = ArrayList<Reels>()
            Reels_List.clear()

            for(i in it.documents){
                var reel = i.toObject<Reels>()!!
                tempList.add(reel)
            }
            Reels_List.addAll(tempList)
            adapter.notifyDataSetChanged()
        }



        return binding.root
    }

    companion object {

    }
}