package com.example.yna_fcm_training

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yna_fcm_training.adapter.NotificationListAdapter
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var tokenTextView: TextView
    private lateinit var notificationListAdapter: NotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tokenTextView = findViewById(R.id.tvToken)
        val btnTest = findViewById<Button>(R.id.btnTest)
        val rvNotifications = findViewById<RecyclerView>(R.id.rvNotifications)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        // Get FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
                tokenTextView.text = token
            }
        }

        // Setup RecyclerView
        notificationListAdapter = NotificationListAdapter()
        rvNotifications.layoutManager = LinearLayoutManager(this)
        rvNotifications.adapter = notificationListAdapter

        // Test Button Click
        btnTest.setOnClickListener {
            showNotification("Test Notification", "This is a local test notification.")
        }
    }

    private fun showNotification(title: String, message: String) {
        notificationListAdapter.addNotification("$title: $message")
    }
}