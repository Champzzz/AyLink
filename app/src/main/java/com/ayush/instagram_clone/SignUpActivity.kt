package com.ayush.instagram_clone

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivitySignUpBinding
import com.ayush.instagram_clone.utils.USER_NODE
import com.ayush.instagram_clone.utils.USER_PROFILE_FOLDER
import com.ayush.instagram_clone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class SignUpActivity : AppCompatActivity() {

    val binding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    lateinit var user: User

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let{
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it==null){

                }else{
                    user.image = it
                    binding.signUpImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text = "<font color=#FF000000>Already Have An Account</font> <font color=#1E88E5>Login ?</font>"
        binding.signUpLogin.setText(Html.fromHtml(text))

        window.statusBarColor = ContextCompat.getColor(this@SignUpActivity ,R.color.background_pink)

        user = User()
        binding.signUpButton.setOnClickListener {

            if(binding.signUpNameField.editText?.text.toString().equals("") or
                binding.signUpEmailField.editText?.text.toString().equals("") or
                binding.signUpPasswordField.editText?.text.toString().equals("") or
                binding.signUpUsername.editText?.text.toString().equals("")){

                Toast.makeText(this@SignUpActivity , "Please Fill All The Information", Toast.LENGTH_SHORT).show()
            }

            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.signUpEmailField.editText?.text.toString() ,
                    binding.signUpPasswordField.editText?.text.toString()
                ).addOnCompleteListener {result ->
                    
                    if(result.isSuccessful){

                        user.name = binding.signUpNameField.editText?.text.toString()
                        user.username = binding.signUpUsername.editText?.text.toString()
                        user.email = binding.signUpEmailField.editText?.text.toString()
                        user.password = binding.signUpPasswordField.editText?.text.toString()

                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnCompleteListener {
                                Toast.makeText(this@SignUpActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            }


                    }else{
                        Toast.makeText(this@SignUpActivity, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        binding.signUpImagePlus.setOnClickListener{
            launcher.launch("image/*")
        }

        binding.signUpLogin.setOnClickListener{
            startActivity(Intent(this,LogInActivity::class.java))
            finish()
        }

    }
}