package com.bengisusahin.e_commerce.service

import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bengisusahin.e_commerce.MainActivity
import com.bengisusahin.e_commerce.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// FirebaseMessagingService class to handle Firebase Cloud Messaging
class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        Log.d("newToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Message from", "${message.from}")
        Log.d("Message messageId", "${message.messageId}")
        // Check if the message contains data
        message.data.isNotEmpty().let {
            Log.d("Message data", "${message.data}")
        }
        message.notification?.let {
            Log.d("Message body", "${it.body}")
            createNotification(it.body!!)
        }
    }

    // Function to create a notification with the given message body and navigate to HomeFragment
    private fun createNotification(messageBody: String) {
        Log.d("MyFirebaseMessagingService", "createNotification started")
        val channelId = "ecommerce_notifications_id"
        val channelName = "ecommerce_notifications__name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        // Create an explicit intent for MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("navigateToHome", true) // Pass extra to navigate to HomeFragment
        }

        // Create a PendingIntent to start MainActivity
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Create a NotificationChannel for Android Oreo and higher
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New Product Available!")
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(0, builder.build())
        Log.d("MyFirebaseMessagingService", "createNotification ended")
    }
}