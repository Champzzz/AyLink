package com.ayush.instagram_clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.SearchRvDgBinding
import com.ayush.instagram_clone.utils.FOLLOW
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SearchAdapter(var context: Context, var user_list : ArrayList<User>) :RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    inner class ViewHolder(var binding:SearchRvDgBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchRvDgBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return user_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isFollow=false

        Glide.with(context).load(user_list.get(position).image).placeholder(R.drawable.account_icon).into(holder.binding.SearchUserImage)
        holder.binding.SearchUserName.text = user_list.get(position).name

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
            .whereEqualTo("email",user_list.get(position).email).get().addOnSuccessListener {

                if(it.documents.size==0){
                    isFollow=false
                }else{
                    holder.binding.FollowButton.text = "UnFollow"
                    isFollow=true
                }
            }

        holder.binding.FollowButton.setOnClickListener {

            if(isFollow){
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                    .whereEqualTo("email",user_list.get(position).email).get().addOnSuccessListener {

                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document(it.documents.get(0).id).delete()
                        holder.binding.FollowButton.text="Follow "
                        isFollow=false
                    }
            }else{
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document().set(user_list.get(position))
                holder.binding.FollowButton.text="UnFollow"
                isFollow=true
            }


        }
    }

}