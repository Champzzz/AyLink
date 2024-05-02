package com.ayush.instagram_clone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.MyPostRvAdapter
import com.ayush.instagram_clone.databinding.FragmentProfileMyPostsBinding
import com.ayush.instagram_clone.utils.MY_POST_FOLDER
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class   Profile_MyPostsFragment2 : Fragment() {

    private lateinit var binding : FragmentProfileMyPostsBinding

    lateinit var post_list : ArrayList<Post>
    lateinit var myPost_adapter : MyPostRvAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileMyPostsBinding.inflate(inflater,container,false)


        post_list = ArrayList<Post>()
        myPost_adapter = MyPostRvAdapter(requireContext(),post_list)
        binding.MyPostRV.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        binding.MyPostRV.adapter = myPost_adapter

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {


            var user: User? = it.toObject<User>()

            Firebase.firestore.collection(user!!.email + POST).get()
                .addOnSuccessListener {
                    var tempList = ArrayList<Post>()

                    for (i in it.documents) {
                        var post: Post = i.toObject<Post>()!!
                        tempList.add(post)
                    }
                    post_list.addAll(tempList)
                    myPost_adapter.notifyDataSetChanged()
                }
            myPost_adapter.notifyDataSetChanged()
        }


        return binding.root
    }

    companion object {

    }
}