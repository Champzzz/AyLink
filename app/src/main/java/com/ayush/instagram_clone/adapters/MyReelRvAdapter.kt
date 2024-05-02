package com.ayush.instagram_clone.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.Reels
import com.ayush.instagram_clone.Profile_PostView
import com.ayush.instagram_clone.Profile_ReelView
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.MyPostRvDesignBinding
import com.ayush.instagram_clone.databinding.ReelDgBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso


class MyReelRvAdapter(var context: Context, var reels_list:ArrayList<Reels>): RecyclerView.Adapter<MyReelRvAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: MyPostRvDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reels_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(context).
//                load(reels_list[position].ReelUrl)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(holder.binding.myPostRvDesignImageView)

        Glide
            .with(context)
            .load(reels_list[position].ReelUrl)
            .centerCrop()
            .placeholder(R.drawable.account_icon)
            .into(holder.binding.myPostRvDesignImageView)

        holder.binding.myPostRvDesignImageView.setOnClickListener {

            val intent = Intent(context, Profile_ReelView::class.java)
            intent.putExtra("url", reels_list.get(position).ReelUrl)
            intent.putExtra("username",reels_list.get(position).profile_name)
            intent.putExtra("caption",reels_list.get(position).caption)
            intent.putExtra("email",reels_list.get(position).profile_link)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.animate_shrink_enter, R.anim.animate_shrink_exit)
        }


     }
}
