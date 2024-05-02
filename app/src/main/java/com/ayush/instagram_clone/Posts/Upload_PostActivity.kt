package com.ayush.instagram_clone.Posts

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ayush.instagram_clone.HomeActivity
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivityUploadPostBinding
import com.ayush.instagram_clone.utils.FOLLOWTHEM
import com.ayush.instagram_clone.utils.MY_POST_FOLDER
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.POST_FOLDER
import com.ayush.instagram_clone.utils.USER_NODE
import com.ayush.instagram_clone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.lang.Exception
import java.util.Locale


class Upload_PostActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityUploadPostBinding.inflate(layoutInflater)
    }

    val REQ = 1

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


        binding.mic.setOnClickListener {

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT,"speak to text")
            }
            try {
                startActivityForResult(intent,REQ)
            }catch (e: Exception){
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }



        binding.createPostPostButton.setOnClickListener {

            if (Upload_imageUrl != null){

                Firebase.firestore.collection(USER_NODE).document().get().addOnSuccessListener {

                    val post: Post = Post(
                        postUrl = Upload_imageUrl!!,
                        caption = binding.captionTextToUpload.text.toString(),
                        Uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )

                    Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {


                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                            var user: User? = it.toObject<User>()

//                        Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM)
//                            .whereEqualTo("email", user!!.email.toString()).get().addOnSuccessListener {
//                                Firebase.firestore.collection(user_list.get(position).email + FOLLOWTHEM).document(it.documents.get(0).id).delete()
//
//                            }

                            Firebase.firestore.collection(user!!.email + POST).document().set(post)
                                .addOnSuccessListener {

                                    startActivity(
                                        Intent(
                                            this@Upload_PostActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()

                                }

                        }


//                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
//                        startActivity(Intent(this@Upload_PostActivity,HomeActivity::class.java))
//                        finish()
//                    }
                    }


                }
        }else{
                Toast.makeText(this@Upload_PostActivity, "Please add a post image", Toast.LENGTH_SHORT).show()
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ){
            if (resultCode == RESULT_OK && data!=null){
                val result : ArrayList<String>? = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                binding.captionTextToUpload.setText(result?.get(0))
            }
        }
    }
}