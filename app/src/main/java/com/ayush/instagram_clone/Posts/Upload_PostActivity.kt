package com.ayush.instagram_clone.Posts

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ayush.instagram_clone.HomeActivity
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivityUploadPostBinding
import com.ayush.instagram_clone.utils.MY_POST_FOLDER
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.POST_FOLDER
import com.ayush.instagram_clone.utils.USER_NODE
import com.ayush.instagram_clone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class Upload_PostActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityUploadPostBinding.inflate(layoutInflater)
    }

    var Upload_imageUrl:String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let{
            uploadImage(uri, POST_FOLDER){     url->
                if(url!=null){
                    binding.ImageToUpload.setImageURI(uri)
                    Upload_imageUrl = url
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.AddpostMaterialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        binding.AddpostMaterialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Upload_PostActivity,HomeActivity::class.java))
            finish()
        }

        binding.AddPhototoPreviewBtn.setOnClickListener{
            launcher.launch("image/*")
        }

        binding.createPostDiscardButton.setOnClickListener {
            startActivity(Intent(this@Upload_PostActivity,HomeActivity::class.java))
            finish()
        }

        binding.createPostPostButton.setOnClickListener {

            Firebase.firestore.collection(USER_NODE).document().get().addOnSuccessListener {

                val post:Post = Post(
                    postUrl = Upload_imageUrl!!,
                    caption = binding.captionTextToUpload.text.toString() ,
                    Uid = Firebase.auth.currentUser!!.uid,
                    time = System.currentTimeMillis().toString())

                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                        startActivity(Intent(this@Upload_PostActivity,HomeActivity::class.java))
                        finish()
                    }
                }


            }

        }

    }
}