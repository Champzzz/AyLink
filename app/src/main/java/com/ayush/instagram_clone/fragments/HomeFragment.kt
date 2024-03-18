package com.ayush.instagram_clone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.Follow_RV_Adapter
import com.ayush.instagram_clone.adapters.Post_Adapter
import com.ayush.instagram_clone.databinding.FragmentHomeBinding
import com.ayush.instagram_clone.databinding.MyPostRvDesignBinding
import com.ayush.instagram_clone.utils.FOLLOW
import com.ayush.instagram_clone.utils.POST
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var post_list =ArrayList<Post>()
    private lateinit var adapter: Post_Adapter

    private lateinit var follow_Adapter:Follow_RV_Adapter
    private var follow_list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        adapter= Post_Adapter(requireContext(),post_list)
        binding.AllPostView.layoutManager=LinearLayoutManager(requireContext())
        binding.AllPostView.adapter=adapter

        follow_Adapter = Follow_RV_Adapter(requireContext(),follow_list)
        binding.homeFollowRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.homeFollowRv.adapter=follow_Adapter

        setHasOptionsMenu(true)



        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get().addOnSuccessListener {

            var temp_list=ArrayList<User>()
            follow_list.clear()

            for (i in it.documents){
                var user:User = i.toObject<User>()!!
                temp_list.add(user)
            }
            follow_list.addAll(temp_list)
            follow_Adapter.notifyDataSetChanged()


        }



        Firebase.firestore.collection(POST).get().addOnSuccessListener {

            var templist = ArrayList<Post>()
            post_list.clear()
            for (i in it.documents){
                var post:Post = i.toObject<Post>()!!
                templist.add(post)
            }
            post_list.addAll(templist)
            adapter.notifyDataSetChanged()
        }

        return binding.root

    }

    companion object {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.option_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }
}