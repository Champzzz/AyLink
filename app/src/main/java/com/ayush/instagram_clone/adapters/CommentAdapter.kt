package com.ayush.instagram_clone.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Message_Model
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.CommentsItemBinding
import com.ayush.instagram_clone.databinding.ReciverItemBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CommentAdapter(var context: Context, var comment_list:ArrayList<Message_Model>) :RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    inner class ViewHolder(var binding: CommentsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = CommentsItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return comment_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Firebase.firestore.collection(USER_NODE).document(comment_list[position].senderid.toString()).get().addOnSuccessListener {

            var user :User = it.toObject<User>()!!

            Glide.with(context).load(user.image).placeholder(R.drawable.account_icon).into(holder.binding.userimg)
            holder.binding.commentUsename.text = user.username.toString()

        }

        val messages = comment_list[position]
        holder.binding.reciveMessgaeId.text = messages.message.toString()
        val timeInMillis = messages.timestam!!
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timeInMillis)
        val formattedTime = sdf.format(date)

        holder.binding.reciverTime.text = formattedTime
    }
}