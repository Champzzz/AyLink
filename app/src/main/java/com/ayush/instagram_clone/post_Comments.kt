package com.ayush.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ayush.instagram_clone.Models.Message_Model
import com.ayush.instagram_clone.adapters.CommentAdapter
import com.ayush.instagram_clone.databinding.ActivityPostCommentsBinding
import com.ayush.instagram_clone.utils.POST
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import java.util.Date

class post_Comments : AppCompatActivity() {

    private lateinit var binding: ActivityPostCommentsBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private lateinit var posturl:String
    private lateinit var useruid:String

    private lateinit var list: ArrayList<Message_Model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        posturl = intent.getStringExtra("postuid")!!
        useruid = intent.getStringExtra("useruid")!!

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        list = ArrayList()

        binding.backImage.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_slide_down_exit)
        }

        Firebase.firestore.collection(POST).whereEqualTo("postUrl",posturl).whereEqualTo("uid",useruid).get().addOnSuccessListener {
            for (document in it) {
                val postUid = document.id
                // Now, 'postUid' contains the UID of the post
                // associated with the provided user UID and post URL

                database.reference.child("Comments")
                    .child(postUid)
                    .child("comment")
                    .addValueEventListener(object :ValueEventListener{

                        override fun onDataChange(snapshot: DataSnapshot) {
                            Log.d("comments_messsgae","comments_messsgae")
                            list.clear()
                            for (snapshot1 in snapshot.children){
                                val data = snapshot1.getValue(Message_Model::class.java)
                                list.add(data!!)
                            }
                            Log.d("list",list[0].toString())
                            binding.commentRv.adapter = CommentAdapter(this@post_Comments,list)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@post_Comments, "error", Toast.LENGTH_SHORT).show()
                        }
                    })

                Glide.with(this).load(posturl).into(binding.PostImage)

                binding.SendMessage.setOnClickListener {


                    if(binding.messageBox.text.isEmpty()){
                        Toast.makeText(this@post_Comments, "enter message", Toast.LENGTH_SHORT).show()
                    }else {
                        val message = Message_Model(binding.messageBox.text.toString(),auth.uid,Date().time)
                        val random_key = database.reference.push().key!!

                        database.reference
                            .child("Comments")
                            .child(postUid)
                            .child("comment")
                            .child(random_key)
                            .setValue(message).addOnSuccessListener {
                                binding.messageBox.text=null
                                Toast.makeText(this@post_Comments, "message send", Toast.LENGTH_SHORT).show()
                            }
                    }

                }



            }
        }

    }
}