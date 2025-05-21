package com.example.yna_fcm_training.service

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            val title = it.title ?: "Notification"
            val body = it.body ?: "Message body"

            val intent = Intent("com.example.fcmrealtime.FCM_NOTIFICATION")
            intent.putExtra("notification", "$title: $body")
            sendBroadcast(intent)
        }
    }
}