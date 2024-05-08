package com.ayush.instagram_clone.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.instagram_clone.ChatsActivity
import com.ayush.instagram_clone.Models.Post
import com.ayush.instagram_clone.Models.User
import com.ayush.instagram_clone.R
import com.ayush.instagram_clone.adapters.Follow_RV_Adapter
import com.ayush.instagram_clone.adapters.Post_Adapter
import com.ayush.instagram_clone.databinding.FragmentHomeBinding
import com.ayush.instagram_clone.utils.FOLLOW
import com.ayush.instagram_clone.utils.POST
import com.ayush.instagram_clone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class HomeFragment : Fragment() , SensorEventListener{

    private lateinit var binding: FragmentHomeBinding

    private var post_list =ArrayList<Post>()
    private lateinit var adapter: Post_Adapter

    private lateinit var follow_Adapter:Follow_RV_Adapter
    private var follow_list = ArrayList<User>()

    private lateinit var sensorManager: SensorManager
    private lateinit var brightnessSensor: Sensor




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)

        setupBrightnessSensor()
        checkAndRequestWifi()

        binding.homeChatButton.setOnClickListener {

            startActivity(Intent(context,ChatsActivity::class.java))
            activity?.overridePendingTransition(R.anim.animate_slide_left_enter,R.anim.animate_slide_left_exit)

        }

        adapter= Post_Adapter(requireContext(),post_list)
        binding.AllPostView.layoutManager=LinearLayoutManager(requireContext())
        binding.AllPostView.adapter=adapter

        follow_Adapter = Follow_RV_Adapter(requireContext(),follow_list)
        binding.homeFollowRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.homeFollowRv.adapter=follow_Adapter

        setHasOptionsMenu(true)




        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

            var user: User = it.toObject<User>()!!


            Firebase.firestore.collection(user.email + FOLLOW).get().addOnSuccessListener {

                var temp_list=ArrayList<User>()
                follow_list.clear()

                for (i in it.documents){
                    var user:User = i.toObject<User>()!!
                    temp_list.add(user)
                }
                follow_list.addAll(temp_list)
                follow_Adapter.notifyDataSetChanged()

            }

        }



//        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get().addOnSuccessListener {
//
//            var temp_list=ArrayList<User>()
//            follow_list.clear()
//
//            for (i in it.documents){
//                var user:User = i.toObject<User>()!!
//                temp_list.add(user)
//            }
//            follow_list.addAll(temp_list)
//            follow_Adapter.notifyDataSetChanged()
//
//
//        }



        Firebase.firestore.collection(POST).get().addOnSuccessListener {

            var templist = ArrayList<Post>()
            post_list.clear()
            for (i in it.documents){
                var post:Post = i.toObject<Post>()!!
                templist.add(post)
            }
            post_list.addAll(templist)
            adapter.notifyDataSetChanged()
        }

        return binding.root

    }

    private fun checkAndRequestWifi() {
        if (!isWifiEnabled()) {
            showWifiAlertDialog()
        }
    }
    private fun isWifiEnabled(): Boolean {
        val wifiManager = requireContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
    private fun showWifiAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Wi-Fi not enabled")
        builder.setMessage("You are not connected to Wi-Fi. Do you want to turn on Wi-Fi?")

        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            // User clicked Yes, turn on Wi-Fi
            enableWifi()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // User clicked No, dismiss the dialog
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()
//        alertDialog.show()
    }

    private fun enableWifi() {
        val wifiManager = requireContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true
    }

    private fun setupBrightnessSensor() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        brightnessSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
        if (brightnessSensor == null) {
            // Handle the case when the device doesn't have a brightness sensor
            // You might want to show a message or take alternative actions
            // For now, I'm just logging a message
            println("Device doesn't have a brightness sensor.")
        }
    }
    override fun onResume() {
        super.onResume()
        checkAndRequestWifi()
        // Register the sensor listener
        brightnessSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this example
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_LIGHT) {
            val brightness = event.values[0]

            // Update the ImageView based on brightness
            if (brightness <= LOW_THRESHOLD) {
                // Set your low brightness image
                binding.sun.setImageResource(R.drawable.sun)
            } else if (brightness <= MID_THRESHOLD) {
                // Set your mid brightness image
                binding.sun.setImageResource(R.drawable.brightnessmid)
            } else {
                // Set your high brightness image
                binding.sun.setImageResource(R.drawable.brightness)
            }
        }
    }



    companion object {
        private const val LOW_THRESHOLD = 0f // You can adjust this threshold as needed
        private const val MID_THRESHOLD = 25f // You can adjust this threshold as needed
    }
}