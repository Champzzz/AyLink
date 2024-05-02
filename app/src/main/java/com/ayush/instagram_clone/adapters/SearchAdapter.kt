package com.ayush.instagram_clone.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.View_Profile
import com.ayush.instagram_clone.databinding.SearchRvDgBinding
import com.ayush.instagram_clone.utils.FOLLOW
import com.ayush.instagram_clone.utils.FOLLOWTHEM
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class SearchAdapter(var context: Context, var user_list : ArrayList<User>) :RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    inner class ViewHolder(var binding:SearchRvDgBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchRvDgBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return user_list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isFollow=false

        Glide.with(context).load(user_list.get(position).image).placeholder(R.drawable.account_icon).into(holder.binding.SearchUserImage)
        holder.binding.SearchUserName.text = user_list.get(position).name

        Firebase.firestore.collection(Firebase.auth.currentUser!!.email.toString() + FOLLOW)
            .whereEqualTo("email",user_list.get(position).email).get().addOnSuccessListener {

                if(it.isEmpty){
                    holder.binding.FollowButton.text = "Follow"
                    isFollow=false
                }else{
                    holder.binding.FollowButton.text = "UnFollow"
                    isFollow=true
                }
            }

        var tosend =""

        var i = 0

        holder.binding.SearchUserImage.setOnClickListener {

            Firebase.firestore.collection(USER_NODE).whereEqualTo("email",user_list.get(position).email.toString()).get().addOnSuccessListener {

                for (i in it) {
                    tosend = i.id
                }
            }

            Log.d("SENDTO",tosend)


            if(i==1) {
                val intent = Intent(context, View_Profile::class.java)
                intent.putExtra("uid", tosend)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(R.anim.animate_swipe_left_enter, R.anim.animate_swipe_left_exit)
            }
            i=1
        }

//        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
//            var user : User? = it.toObject<User>()
//
//            Firebase.firestore.collection(user!!.email + FOLLOW).document(user_list.get(position).email.toString()).get().addOnSuccessListener {
//
//                holder.binding.FollowButton.text = "UnFollow"
//                isFollow=true
//
//
//            }
//        }

        holder.binding.FollowButton.setOnClickListener {

            if(isFollow){
//                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
//                    .whereEqualTo("email",user_list.get(position).email).get().addOnSuccessListener {
//
//                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document(it.documents.get(0).id).delete()
//
//                        holder.binding.FollowButton.text="Follow "
//                        isFollow=false
//                    }


                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                    var user : User? = it.toObject<User>()

//                    Firebase.firestore.collection(user!!.email + FOLLOW)
//                        .whereEqualTo("email",user_list.get(position).email).get().addOnSuccessListener {
//
//                            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document(it.documents.get(0).id).delete()
//
//                            holder.binding.FollowButton.text="Follow "
//                            isFollow=false
//                        }

                    Firebase.firestore.collection(user!!.email + FOLLOW).document(user_list.get(position).email.toString()).delete()

                    Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM)
                        .whereEqualTo("email", user!!.email.toString()).get().addOnSuccessListener {
                            Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM).document(it.documents.get(0).id).delete()
                            holder.binding.FollowButton.text="Follow "
                            isFollow=false
                        }

                }
//                Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM)
//                    .whereEqualTo("email",user_list.get(position).email).get().addOnSuccessListener {
//                        Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM).document(it.documents.get(0).id).delete()
//
//                    }
            }else{


//
//                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document().set(user_list.get(position))
//                holder.binding.FollowButton.text="UnFollow"
//                isFollow=true



                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                    var user : User? = it.toObject<User>()

                    Firebase.firestore.collection(user!!.email + FOLLOW).document(user_list.get(position).email.toString()).set(user_list.get(position))
                    holder.binding.FollowButton.text="UnFollow"
                    isFollow=true

                    Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM).document().set(user!!)

                }


            }


        }
    }

}