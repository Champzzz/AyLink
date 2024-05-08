package com.ayush.instagram_clone

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ayush.instagram_clone.Models.Message_Model
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.adapters.Message_Adapter
import com.ayush.instagram_clone.databinding.ActivityChattingBinding
import com.ayush.instagram_clone.utils.CHAT
import com.ayush.instagram_clone.utils.MESSAGE
import com.ayush.instagram_clone.utils.POST_FOLDER
import com.ayush.instagram_clone.utils.USER_NODE
import com.ayush.instagram_clone.utils.uploadImage
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.util.Date

class Chatting_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityChattingBinding
    private lateinit var reciveremail: String

    private lateinit var database: FirebaseDatabase
    private lateinit var senderuid: String
    private lateinit var reciveruid: String

    private lateinit var senderRoom: String
    private lateinit var reciverRoom: String

    private lateinit var name:String
    private lateinit var image:String

    private lateinit var list: ArrayList<Message_Model>


    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            try {

                val inputStream = contentResolver.openInputStream(selectedUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                val drawable = BitmapDrawable(resources, bitmap)


                binding.root.background = drawable

            } catch (e:Exception){
                Toast.makeText(this@Chatting_Activity, e.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra("name")!!
        binding.usernameDisplay.text = name

        image = intent.getStringExtra("image")!!
        Glide.with(this).load(image).into(binding.userimg)

        binding.backImage.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_slide_out_right)
        }

        binding.chatUploadBackground.setOnClickListener {

            launcher.launch("image/*")

        }


        reciveremail = intent.getStringExtra("email")!!
        Firebase.firestore.collection(USER_NODE).whereEqualTo("email",reciveremail).get().addOnSuccessListener {
            for (document in it){
                val uid = document.id
                reciveruid = uid
                Log.d("chatuseruid",uid)

                senderuid = FirebaseAuth.getInstance().uid.toString()
                senderRoom = senderuid+reciveruid
                reciverRoom = reciveruid+senderuid

                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                    var user: User = it.toObject<User>()!!

                    val userEmailWithoutDomain = user.email?.replace("@gmail.com", "")!!
                    val otheremailwithoutDomain = reciveremail.replace("@gmail.com", "")

                    database.reference
                        .child(userEmailWithoutDomain + CHAT)
                        .child(otheremailwithoutDomain)
                        .child(CHAT)
                        .child(senderRoom)
                        .child(MESSAGE).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                list.clear()
                                for (snapshot1 in snapshot.children) {
                                    val data = snapshot1.getValue(Message_Model::class.java)
                                    list.add(data!!)
                                }
                                binding.chatMesagesRv.adapter =
                                    Message_Adapter(this@Chatting_Activity, list)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@Chatting_Activity, "error", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }
        }

        database = FirebaseDatabase.getInstance()

        list = ArrayList()

        binding.SendMessage.setOnClickListener {
            Log.d("clicked","clicked")
            if(binding.messageBox.text.isEmpty()){
                Toast.makeText(this@Chatting_Activity, "enter message", Toast.LENGTH_SHORT).show()
            }else{
                val message = Message_Model(binding.messageBox.text.toString(),senderuid,Date().time)
                val random_key = database.reference.push().key


                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                    var user: User = it.toObject<User>()!!

                    val userEmailWithoutDomain = user.email?.replace("@gmail.com", "")!!
                    val otheremailwithoutDomain = reciveremail.replace("@gmail.com", "")

                    database.reference
                        .child(userEmailWithoutDomain + CHAT)
                        .child(otheremailwithoutDomain)
                        .child(CHAT)
                        .child(senderRoom)
                        .child(MESSAGE)
                        .child(random_key!!)
                        .setValue(message)
                        .addOnSuccessListener{
                            database.reference.child(otheremailwithoutDomain + CHAT)
                                .child(userEmailWithoutDomain)
                                .child(CHAT)
                                .child(reciverRoom)
                                .child(MESSAGE)
                                .child(random_key!!)
                                .setValue(message)
                                .addOnSuccessListener {
                                    binding.messageBox.text=null
                                    Toast.makeText(this@Chatting_Activity, "message send", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
            }
        }



    }
}