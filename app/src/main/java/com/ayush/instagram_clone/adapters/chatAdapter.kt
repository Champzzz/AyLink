package com.ayush.instagram_clone.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ayush.instagram_clone.Chatting_Activity
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.ChatUseritemBinding

import com.bumptech.glide.Glide

class chatAdapter(var context :Context , var user_list: ArrayList<User>) :RecyclerView.Adapter<chatAdapter.ChatViewHolder>(){
    inner class ChatViewHolder(view:View):ViewHolder(view){
        var binding : ChatUseritemBinding = ChatUseritemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_useritem,parent,false))
    }

    override fun getItemCount(): Int {
        return user_list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var user = user_list[position]
        Glide.with(context).load(user.image).placeholder(R.drawable.account_icon).into(holder.binding.userimg)
        holder.binding.username.text = user.name

        holder.itemView.setOnClickListener{
            val intent = Intent(context,Chatting_Activity::class.java)
            intent.putExtra("email",user.email.toString())
            intent.putExtra("image",user.image.toString())
            intent.putExtra("name",user.username.toString())
            Log.d("chat_email",user.email.toString())
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.animate_slide_left_enter,R.anim.animate_slide_left_exit)
        }
    }
}