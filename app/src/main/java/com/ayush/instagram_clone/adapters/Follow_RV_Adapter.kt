package com.ayush.instagram_clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.FollowRvItemBinding
import com.bumptech.glide.Glide

class Follow_RV_Adapter(var context: Context, var follow_list:ArrayList<User>) : RecyclerView.Adapter<Follow_RV_Adapter.ViewHolder>() {

    inner class ViewHolder(var binding:FollowRvItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = FollowRvItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return follow_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(follow_list.get(position).image).placeholder(R.drawable.account_icon).into(holder.binding.RvFollowUserImage)
        holder.binding.RvFollowUserName.text = follow_list.get(position).name
    }

}