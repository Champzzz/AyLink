package com.ayush.instagram_clone.Posts

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ayush.instagram_clone.HomeActivity
import com.ayush.instagram_clone.Models.Reels
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivityUploadReelsBinding
import com.ayush.instagram_clone.utils.MY_REEL_FOLDER
import com.ayush.instagram_clone.utils.MY_RELS
import com.ayush.instagram_clone.utils.USER_NODE
import com.ayush.instagram_clone.utils.uploadVideo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class Upload_ReelsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityUploadReelsBinding.inflate(layoutInflater)
    }

    var Upload_VideoUrl:String? = null
    lateinit var progressDialog:ProgressDialog

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let{
            uploadVideo(uri, MY_REEL_FOLDER,progressDialog ){ url->
                if(url!=null){
                    Upload_VideoUrl = url
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        binding.AddReelMaterialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Upload_ReelsActivity, HomeActivity::class.java))
            finish()
        }

        binding.AddVideotoPreviewBtn.setOnClickListener{
            launcher.launch("video/*")
        }

        binding.VideoToUpload.setOnClickListener{
            if(Upload_VideoUrl!=null){
                val mediaControler = MediaController(this)
                mediaControler.setAnchorView(binding.VideoToUpload)
                val uri = Uri.parse(Upload_VideoUrl)
                binding.VideoToUpload.setVideoURI(uri)
                binding.VideoToUpload.requestFocus()
                binding.VideoToUpload.start()
            }
        }

        binding.createReelDiscardButton.setOnClickListener {
            startActivity(Intent(this@Upload_ReelsActivity ,HomeActivity::class.java))
            finish()
        }

        binding.createReelPostButton.setOnClickListener {

            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user:User = it.toObject<User>()!!

                val reel: Reels = Reels(Upload_VideoUrl!!,binding.ReelcaptionTextToUpload.text.toString(),user.image!!)

                Firebase.firestore.collection(MY_RELS).document().set(reel).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+MY_RELS).document().set(reel).addOnSuccessListener {
                        startActivity(Intent(this@Upload_ReelsActivity,HomeActivity::class.java))
                        finish()
                    }
                }
            }




        }

    }
}