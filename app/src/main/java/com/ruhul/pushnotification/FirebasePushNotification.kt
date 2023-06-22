package com.ruhul.pushnotification

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ruhul.pushnotification.App.Companion.channel1
import java.io.IOException
import java.net.URL
import java.util.*


class FirebasePushNotification : FirebaseMessagingService() {

    private val log = "PushNotification"

    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private lateinit var pendingIntent: PendingIntent

    override fun onNewToken(s: String) {
        Log.d(log, "onNewToken $s")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(log, "Notification : " + remoteMessage.data.toString())

            initIntent(remoteMessage)
            showNotification(remoteMessage)

        } else {
            Log.e(log, "Notification : " + remoteMessage.data.toString())
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun initIntent(remoteMessage: RemoteMessage) {
        val intent = Intent(this, DisplayNotificationActivity::class.java)
        intent.putExtra("message", remoteMessage.notification?.body)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
    }

    private fun showNotification(message: RemoteMessage) {

        notificationManagerCompat = NotificationManagerCompat.from(this)


        val expandedView = RemoteViews(
            packageName,
            com.ruhul.pushnotification.R.layout.notification_expanded
        )
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channel1)
                .setSmallIcon(R.drawable.ic_notification_overlay)
                .setContentTitle(message.notification!!.title)
                .setCustomBigContentView(expandedView)
                .setContentText(message.notification!!.body)
              /*  .setStyle(
                    NotificationCompat.BigPictureStyle().bigLargeIcon(
                        downloadBanner(
                            message.notification!!.imageUrl
                        )
                    )
                )*/
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(false)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val random = Random()
        val notificationId = random.nextInt(100)

        notificationManagerCompat.notify(notificationId, builder.build())

    }
}

private fun downloadBanner(imageUrl: Uri?): Bitmap? {
    var bannerBitmap: Bitmap? = null

    try {
        val url = URL(imageUrl.toString())
        bannerBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
    } catch (e: IOException) {
        println(e)
    }
    return bannerBitmap

}