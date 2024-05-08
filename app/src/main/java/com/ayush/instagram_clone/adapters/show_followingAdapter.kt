package com.ayush.instagram_clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.FollowRvItemBinding
import com.ayush.instagram_clone.databinding.ShowFollowingitemBinding
import com.bumptech.glide.Glide

class show_followingAdapter(var context: Context, var follow_list:ArrayList<User>) : RecyclerView.Adapter<show_followingAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ShowFollowingitemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ShowFollowingitemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return follow_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.username.text = follow_list.get(position).name.toString()
        Glide.with(context).load(follow_list.get(position).image.toString()).placeholder(R.drawable.account_icon).into(holder.binding.userimg)
    }

}