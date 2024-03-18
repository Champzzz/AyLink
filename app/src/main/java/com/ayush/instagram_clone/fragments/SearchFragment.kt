package com.ayush.instagram_clone.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.SearchAdapter
import com.ayush.instagram_clone.databinding.FragmentSearchBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class SearchFragment : Fragment() {

    lateinit var binding:FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var user_list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater,container,false)

        binding.SearchRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter( requireContext(),user_list)
        binding.SearchRv.adapter = adapter

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {

            var temp_list = ArrayList<User>()
            user_list.clear()

            for(i in it.documents) {

                if (i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {

                } else {

                    var user = i.toObject<User>()!!
                    temp_list.add(user)
                }
            }
            user_list.addAll(temp_list)
            adapter.notifyDataSetChanged()

        }


        binding.doSearchBtn.setOnClickListener {
            var text = binding.searchView.text.toString()

            Firebase.firestore.collection(USER_NODE).whereEqualTo("name",text).get().addOnSuccessListener {

                var temp_list = ArrayList<User>()
                user_list.clear()

                if(it.isEmpty){

                }else {

                    for (i in it.documents) {

                        if (i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {

                        } else {

                            var user = i.toObject<User>()!!
                            temp_list.add(user)
                        }
                    }
                    user_list.addAll(temp_list)
                    adapter.notifyDataSetChanged()

                }

            }

        }


        return binding.root
    }

    companion object {

    }
}