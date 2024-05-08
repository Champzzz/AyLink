package com.ayush.instagram_clone.adapters

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.View_Profile
import com.ayush.instagram_clone.databinding.AllPostRvDgBinding
import com.ayush.instagram_clone.post_Comments
import com.ayush.instagram_clone.utils.SAVE
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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

        if (!(context as Activity).isDestroyed) {
            Glide.with(context).load(post_list[position].postUrl)
                .placeholder(R.drawable.account_icon).into(holder.binding.postImageShow)
        }

//        Glide.with(context).load(post_list.get(position).postUrl).placeholder(R.drawable.account_icon).into(holder.binding.postImageShow)

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

        holder.binding.comments.setOnClickListener {
            var postuid = post_list[position].postUrl
            var useruid = post_list[position].Uid
            var i = Intent(context, post_Comments::class.java)
            i.putExtra("postuid",postuid)
            i.putExtra("useruid",useruid)
            context.startActivity(i)
            (context as Activity).overridePendingTransition(R.anim.animate_slide_up_enter,R.anim.animate_fade_exit)
        }


        holder.binding.Caption.text = post_list.get(position).caption

        holder.binding.profileImageDisplay.setOnClickListener {

            val intent = Intent(context, View_Profile::class.java)
            intent.putExtra("uid",post_list.get(position).Uid)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.animate_swipe_left_enter, R.anim.animate_swipe_left_exit)

        }


        holder.binding.savePost.setOnClickListener {




            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                var user : User = it.toObject<User>()!!

                Firebase.firestore.collection(user.email+ SAVE).whereEqualTo("time",post_list.get(position).time).get().addOnSuccessListener {

                    if(it.documents.size == 0){
                        Firebase.firestore.collection(user.email+ SAVE).document().set(post_list.get(position))

                    }



                }







            }

        }
    }
}