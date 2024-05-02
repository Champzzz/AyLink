 package com.ayush.instagram_clone.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Profile_PostView
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MyPostRvAdapter(var context:Context , var post_list:ArrayList<Post>): RecyclerView.Adapter<MyPostRvAdapter.ViewHolder>() {

    inner class ViewHolder(var binding:MyPostRvDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return post_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(post_list.get(position).postUrl).placeholder(R.drawable.account_icon).into(holder.binding.myPostRvDesignImageView)

        holder.binding.myPostRvDesignImageView.setOnClickListener {

            val intent = Intent(context, Profile_PostView::class.java)
            intent.putExtra("uid", post_list.get(position).Uid)
            intent.putExtra("time", post_list.get(position).time)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.animate_shrink_enter, R.anim.animate_shrink_exit)

        }


    }
}