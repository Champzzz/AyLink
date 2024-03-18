package com.ayush.instagram_clone.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.AllPostRvDgBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class Post_Adapter(var context:Context, var post_list:ArrayList<Post>):RecyclerView.Adapter<Post_Adapter.MyHolder>() {

    inner class MyHolder(var binding:AllPostRvDgBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = AllPostRvDgBinding.inflate(LayoutInflater.from(context),parent,false)

        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return post_list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        try {
            Firebase.firestore.collection(USER_NODE).document(post_list.get(position).Uid).get().addOnSuccessListener {

                var user = it.toObject<User>()
                Glide.with(context).load(user!!.image).placeholder(R.drawable.account_icon).into(holder.binding.profileImageDisplay )
                holder.binding.PostUsername.text = user.name
            }
        }catch (e:Exception){

        }

        Glide.with(context).load(post_list.get(position).postUrl).placeholder(R.drawable.account_icon).into(holder.binding.postImageShow)

        try {
            val nowtime = TimeAgo.using(post_list.get(position).time.toLong())

            holder.binding.PostTime.text = nowtime
        }catch (e:Exception){
            holder.binding.PostTime.text = ""
        }

        holder.binding.share.setOnClickListener{
            var i = Intent(Intent.ACTION_SEND)
            i.type="text/plain"
            i.putExtra(Intent.EXTRA_TEXT,post_list.get(position).postUrl)
            context.startActivity(i)
        }


        holder.binding.Caption.text = post_list.get(position).caption

        holder.binding.Like.setOnClickListener {

            holder.binding.Like.setImageResource(R.drawable.heart)
        }
    }
}