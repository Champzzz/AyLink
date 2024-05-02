package com.ayush.instagram_clone

import android.content.Intent
import android.net.Uri
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

    var imageuuri: Uri? = null



    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let{
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it==null){

                }else{
                    user.image = it
                    binding.editProfileImageDisplay.setImageURI(uri)
                    imageuuri = uri
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



//


        binding.editProfileSubmitButton.setOnClickListener {
            val newName = binding.editProfileEditName.text.toString()
            val newUsername = binding.editProfileEditUsername.text.toString()

            if (newName == user.name && newUsername == user.username) {
                // No changes were made, so go back to HomeActivity
                startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
            } else {
                // Changes were made, so update the user document in Firestore
                val map = mutableMapOf<String, Any>()

                if (newName != user.name) {
                    map["name"] = newName
                }

                if (newUsername != user.username) {
                    map["username"] = newUsername
                }

                // Check if the user has selected a new image
                val selectedImageUri = imageuuri
                    if (selectedImageUri != null) {
                        uploadImage(selectedImageUri, USER_PROFILE_FOLDER) { imageUrl ->
                            if (imageUrl != null) {
                                map["image"] = imageUrl // Update the image URL in Firestore
                            }

                            // Update the document in Firestore
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid)
                                .set(map, SetOptions.merge())
                                .addOnSuccessListener {
                                    startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
                                }
                                .addOnFailureListener { exception ->
                                    // Handle the failure, e.g., display an error message
                                }
                        }
                    } else {
                        // No new image selected, update other fields only
                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid)
                            .set(map, SetOptions.merge())
                            .addOnSuccessListener {
                                startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
                            }
                            .addOnFailureListener { exception ->
                                // Handle the failure, e.g., display an error message
                            }
                    }
            }
        }



        binding.editProfileEditImageText.setOnClickListener{
            launcher.launch("image/*")
        }



    }
}