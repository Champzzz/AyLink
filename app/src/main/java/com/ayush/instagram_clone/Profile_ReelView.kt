package com.ayush.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayush.instagram_clone.databinding.ActivityProfiePostViewBinding
import com.ayush.instagram_clone.databinding.ActivityProfileReelViewBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

class Profile_ReelView : AppCompatActivity() {

    private lateinit var binding: ActivityProfileReelViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileReelViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedUrl = intent.getStringExtra("url")
        val receivedusername = intent.getStringExtra("username")
        val receivedcaption = intent.getStringExtra("caption")
        val receivedemail = intent.getStringExtra("email")


        binding.reelDGCaption.setText(receivedcaption)
        binding.reelDGName.setText(receivedusername)
        Firebase.firestore.collection(USER_NODE).whereEqualTo("email",receivedemail).get().addOnSuccessListener {

            if (!it.isEmpty) {
                // Retrieve the first document (assuming there's only one user with the given email)
                val userDocument = it.documents[0]

                // Get the profile picture URL from the document
                val profilePictureUrl = userDocument.getString("profile_picture")


                Picasso.get().load(profilePictureUrl).placeholder(R.drawable.add_profile).into(binding.reelDGProfilPic)
                // Load the profile picture into an ImageView using Picasso or Glide
                // For example, using Picasso:
                // Picasso.get().load(profilePictureUrl).into(imageView)

                // If you're using Glide:
                // Glide.with(context).load(profilePictureUrl).into(imageView)
            }

        }
        binding.videoView.setVideoPath(receivedUrl)
        binding.videoView.setOnPreparedListener{
            binding.ReelWaitProgressBar.visibility = View.GONE
            binding.videoView.start()
        }




    }
}