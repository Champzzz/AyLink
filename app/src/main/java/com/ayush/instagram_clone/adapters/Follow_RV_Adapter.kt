package com.ayush.instagram_clone.adapters

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.View_Profile
import com.ayush.instagram_clone.databinding.FollowRvItemBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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
        holder.binding.RvFollowUserName.text = follow_list.get(position).username

        var tosend =""

        var i = 0

        Log.d("email",follow_list.get(position).email.toString())

        holder.binding.RvFollowUserImage.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).whereEqualTo("email",follow_list.get(position).email.toString()).get().addOnSuccessListener {

                for (i in it) {
                    tosend = i.id
                }
            }

            Log.d("SENDTO",tosend)


            if(i==1) {
                val intent = Intent(context, View_Profile::class.java)
                intent.putExtra("uid",tosend)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(R.anim.animate_swipe_left_enter, R.anim.animate_swipe_left_exit)

            }

            i=1
        }





    }

}