package com.ayush.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivityProfiePostViewBinding
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.USER_NODE
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import java.util.Locale

class Profile_PostView : AppCompatActivity() , TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityProfiePostViewBinding
    private var tts: TextToSpeech?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfiePostViewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        tts = TextToSpeech(this,this)
        binding.speak!!.setOnClickListener { speakOut() }




        val receivedUid = intent.getStringExtra("uid")!!
        val receivedTime = intent.getStringExtra("time")!!
        var postid = ""

        val timeago = receivedTime.toLong()

        val nowtime = TimeAgo.using(timeago)

        Log.d("nowtime",nowtime.toString())

        binding.PostTime.text = nowtime.toString()

        Firebase.firestore.collection(USER_NODE).document(receivedUid).get().addOnSuccessListener {

            var user : User = it.toObject<User>()!!

            Glide.with(this).load(user!!.image).placeholder(R.drawable.account_icon).into(binding.profileImageDisplay )
            binding.PostUsername.text = user.name


            Firebase.firestore.collection(user.email + POST).whereEqualTo("time",receivedTime).get().addOnSuccessListener {

                for ( i in it){
                    postid = i.id
                    Log.d("Postid",postid)
                }
                Firebase.firestore.collection(user.email + POST).document(postid).get().addOnSuccessListener {

                    var post : Post = it.toObject<Post>()!!

                    binding.Caption.text = post.caption
                    Picasso.get().load(post.postUrl).into(binding.postImageShow)

                }

            }
        }






    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if (result==TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language not supported!")
            }else{
                binding.speak!!.isEnabled = true
            }
        }
    }

    private fun speakOut() {
        val text = binding.Caption.text.toString()
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onDestroy() {

        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }


        super.onDestroy()


    }
}