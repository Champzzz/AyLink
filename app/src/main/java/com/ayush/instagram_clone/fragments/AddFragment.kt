package com.ayush.instagram_clone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayush.instagram_clone.Posts.Upload_PostActivity
import com.ayush.instagram_clone.Posts.Upload_ReelsActivity
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater,container,false)

        binding.AddPostsBtn.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),Upload_PostActivity::class.java))
            activity?.finish()
        }

        binding.AddReelsBtn.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),Upload_ReelsActivity::class.java))
        }

        return binding.root
    }

    companion object {

    }
}