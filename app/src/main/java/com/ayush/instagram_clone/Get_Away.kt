package com.ayush.instagram_clone

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Get_Away : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var proximitySensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_away)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        // Register the sensor listener
        sensorManager?.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Unregister the sensor listener when the activity is destroyed
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing for now
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Check if the event is from the proximity sensor
        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            // Check the distance detected by the proximity sensor
            val distance = event.values[0]
            if (distance > (proximitySensor?.maximumRange ?: Float.MAX_VALUE)) {
                // Object is far, trigger a new activity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}