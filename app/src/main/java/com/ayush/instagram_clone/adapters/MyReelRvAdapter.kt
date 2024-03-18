package com.ayush.instagram_clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.Reels
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
        Glide.with(context).
                load(reels_list[position].ReelUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.myPostRvDesignImageView)

     }
}
