package com.ayush.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.databinding.ActivityLogInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLogInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this@LogInActivity , R.color.background_pink)

        val text = "<font color=#FF000000>Don't have an account?</font> <font color=#1E88E5>  Sign up</font>"
        binding.loginSignUpText.setText(Html.fromHtml(text))


        binding.LoginSubmitButtonToHome.setOnClickListener {

            if (binding.LoginEmailField.editText?.text.toString().equals("") or
                binding.LoginPasswordField.editText?.text.toString().equals("")){
                Toast.makeText(this@LogInActivity, "Please Fill All details", Toast.LENGTH_SHORT).show()
            }
            else{

                var user = User(
                    binding.LoginEmailField.editText?.text.toString(),
                    binding.LoginPasswordField.editText?.text.toString()
                )

                Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!).addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this@LogInActivity,HomeActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@LogInActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.loginSignUpText.setOnClickListener {
            startActivity(Intent(this@LogInActivity,SignUpActivity::class.java))
            finish()
        }
    }
}