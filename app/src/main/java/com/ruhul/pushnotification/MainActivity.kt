package com.ruhul.pushnotification

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.badge.BadgeDrawable
import com.google.firebase.messaging.FirebaseMessaging
import com.ruhul.pushnotification.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {


    var nCounter: BadgeDrawable? = null

    private lateinit var binding: ActivityMainBinding
    private val log = "PushNotification"
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    // Get new FCM registration token
                    val token = task.result
                    Log.d("token", token!!)
                }
            }

        binding.sendNotification.setOnClickListener {
            showNotification()
        }
    }

    /*    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
            if (isGranted) {
             askNotificationPermission()
                // FCM SDK (and your app) can post notifications.
            } else {
                // TODO: Inform user that that your app will not show notifications.
            }
        }*/

    /*    private fun askNotificationPermission() {
            // This is only necessary for API level >= 33 (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    // FCM SDK (and your app) can post notifications.
                } else if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        TODO("VERSION.SDK_INT < M")
                    }
                ) {
                    // TODO: display an educational UI explaining to the user the features that will be enabled
                    //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    //       If the user selects "No thanks," allow the user to continue without notifications.
                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }*/

    private fun showNotification() {
        notificationManagerCompat = NotificationManagerCompat.from(this)
        val expandedView = RemoteViews(
            packageName,
            R.layout.notification_expanded
        )
        expandedView.setImageViewResource(R.id.imageView, R.drawable.custom_img)
        expandedView.setTextViewText(
            R.id.title,
            "Push notifications are a powerful mobile-first communication channel that's used by every successful app today"
        )
        expandedView.setTextViewText(
            R.id.description, "What is push notification title?\n" +
                    "Push notifications are a powerful mobile-first communication channel that's used by every successful app today. These small, pop-up messages are sent to users devices by a mobile app and can be viewed from the device lock screen when an app isn't currently in use."
        )

        val intent = Intent(this, DisplayNotificationActivity::class.java)
        intent.putExtra("message", "")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, App.channel1)
                .setSmallIcon(R.drawable.notification_banner)
                .setContentTitle("push notification title")
                .setCustomBigContentView(expandedView)
                .setNumber(5)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val random = Random()
        val notificationId = random.nextInt(100)
        notificationManagerCompat.notify(notificationId, builder.build())

    }


}