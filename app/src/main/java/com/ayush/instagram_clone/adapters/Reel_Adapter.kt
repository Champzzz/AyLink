package com.ayush.instagram_clone.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.Reels
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.MyPostRvDesignBinding
import com.ayush.instagram_clone.databinding.ReelDgBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.squareup.picasso.Picasso


class Reel_Adapter(var context: Context, var reels_list:ArrayList<Reels>): RecyclerView.Adapter<Reel_Adapter.ViewHolder>() {

    inner class ViewHolder(var binding: ReelDgBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ReelDgBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Reel_Adapter.ViewHolder, position: Int) {

        Firebase.firestore.collection(USER_NODE).whereEqualTo("email",reels_list.get(position).profile_link.toString()).get().addOnSuccessListener {

            for (document in it) {
                val userimage = document.get("image")

                Glide.with(context).load(userimage).placeholder(R.drawable.account_icon).into(holder.binding.reelDGProfilPic)



            }
        }


        holder.binding.reelDGCaption.setText(reels_list.get(position).caption)
        holder.binding.reelDGName.setText(reels_list.get(position).profile_name)
        holder.binding.videoView.setVideoPath(reels_list.get(position).ReelUrl)
        holder.binding.videoView.setOnPreparedListener{
            holder.binding.ReelWaitProgressBar.visibility = View.GONE
            holder.binding.videoView.start()
        }
    }

    override fun getItemCount(): Int {
        return reels_list.size
    }

}