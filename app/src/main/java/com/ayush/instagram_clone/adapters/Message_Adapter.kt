package com.ayush.instagram_clone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.instagram_clone.Models.Message_Model
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.databinding.ReciverItemBinding
import com.ayush.instagram_clone.databinding.SenderItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Message_Adapter(var context:Context , var message_list : ArrayList<Message_Model>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var Item_Send = 1
    var Item_Recive = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == Item_Send) SendViewHolder(LayoutInflater.from(context).inflate(R.layout.sender_item,parent,false))
        else ReciveViewHolder(LayoutInflater.from(context).inflate(R.layout.reciver_item,parent,false))

    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid == message_list[position].senderid) Item_Send else Item_Recive
    }

    override fun getItemCount(): Int {
        return message_list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages = message_list[position]

        if(holder.itemViewType == Item_Send){
            val viewHolder = holder as SendViewHolder
            viewHolder.binding.senderMessageId.text = messages.message

            val timeInMillis = messages.timestam!!
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = Date(timeInMillis)
            val formattedTime = sdf.format(date)

            viewHolder.binding.senderTime.text = formattedTime
        }else{

            val viewHolder = holder as ReciveViewHolder
            viewHolder.binding.reciveMessgaeId.text = messages.message

            val timeInMillis = messages.timestam!!
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = Date(timeInMillis)
            val formattedTime = sdf.format(date)

            viewHolder.binding.reciverTime.text = formattedTime
        }
    }

    inner class SendViewHolder(view:View):RecyclerView.ViewHolder(view){
        var binding = SenderItemBinding.bind(view)
    }

    inner class ReciveViewHolder(view:View):RecyclerView.ViewHolder(view){
        var binding = ReciverItemBinding.bind(view)
    }
}