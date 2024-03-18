package com.ayush.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivityEditProfileBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.ayush.instagram_clone.utils.USER_PROFILE_FOLDER
import com.ayush.instagram_clone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class EditProfileActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }



    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let{
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it==null){

                }else{
                    user.image = it
                    binding.editProfileImageDisplay.setImageURI(uri)
                }
            }
        }
    }

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user=User()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val user:User = it.toObject<User>()!!
            binding.editProfileEditName.setText(user.name)
            binding.editProfileEditUsername.setText(user.username)

            if(!user.image.isNullOrEmpty()){
                Picasso.get().load(user.image).into(binding.editProfileImageDisplay)
            }
        }


        binding.editProfileSubmitButton.setOnClickListener {

            if(binding.editProfileEditName.text.toString().equals(user.name) and
                binding.editProfileEditUsername.text.toString().equals(user.username)){
                startActivity(Intent(this@EditProfileActivity,HomeActivity::class.java))
            }
            else{

                user.name = binding.editProfileEditName.text.toString()
                user.username = binding.editProfileEditUsername.text.toString()

                val map = mutableMapOf<String,Any>()

                if(!binding.editProfileEditName.text.toString().equals(user.name)){
                    map["name"] = binding.editProfileEditName.text.toString()
                }

                if(!binding.editProfileEditUsername.text.toString().equals(user.username)){
                    map["name"] = binding.editProfileEditUsername.text.toString()
                }

                Firebase.firestore.collection(USER_NODE)
                    .document(Firebase.auth.currentUser!!.uid).set(map, SetOptions.merge()).addOnSuccessListener {
                        startActivity(Intent(this@EditProfileActivity,HomeActivity::class.java))
                    }

            }



        }


        binding.editProfileEditImageText.setOnClickListener{
            launcher.launch("image/*")
        }



    }
}