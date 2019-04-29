package com.almutlaq.realstate.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.Splash
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.almutlaq.realstate.utility.Commonfunctions.checkappinBackground
import com.almutlaq.realstate.utility.StatusConstant

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(fcmtoken: String?) {
        super.onNewToken(fcmtoken)
        Log.e("FCM_TOKEN", fcmtoken)
        storeRegIdInPref(fcmtoken!!)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d("NOTIFY_MSG", "From: " + remoteMessage!!.from!!)
       // val msg = remoteMessage.data["message"]
       // val tag = remoteMessage.data["optMode"]
     //   val status = remoteMessage.data["optType"]
        //    Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        val data = remoteMessage.data.toString()
        Log.e("pushResponse", data)
        val title = remoteMessage.data.get("title")
        val message = remoteMessage.data.get("message")
        val page =  remoteMessage.data.get("page")
        val boookingid =  remoteMessage.data.get("service_booking_id")

        if (checkappinBackground(applicationContext)) {
            try {
                sendNotification(title, message, page,boookingid)
            }catch (e : Exception){
                e.printStackTrace()
                Log.e("noti_error", "Exception: " + e.message)
            }

        } else{
            val dialog_intent = Intent(StatusConstant.NOTIFICATION_ACTION)
            dialog_intent.putExtra(StatusConstant.fcm_message, message)
            dialog_intent.putExtra(StatusConstant.fcm_tag, title)
            dialog_intent.putExtra(StatusConstant.fcm_page, page)
            dialog_intent.putExtra(StatusConstant.fcm_booking_id, boookingid)
            sendBroadcast(dialog_intent)
        }
    }

    private fun sendNotification(messagetitle: String?, messageBody: String?, pagename: String?, service_boooking_id: String?) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mp = MediaPlayer.create(this, notification)
        mp.setVolume(8f, 8f)
        mp.isLooping = false
        mp.start()

        val intent = Intent(this, Splash::class.java)
        intent.putExtra("pagename", pagename)
        intent.putExtra("msgtitle", messagetitle)
        intent.putExtra("msgbody", messageBody)
        intent.putExtra("bookid", service_boooking_id)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        //val pintent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent, 0)

        // Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.new_launcher_logo)
                .setContentTitle(messagetitle)
                .setContentText(messageBody)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                /* .setSound(defaultSoundUri)*/
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.notify(0, notificationBuilder.build())

    }

    private fun storeRegIdInPref(token: String) {
        val pref = applicationContext.getSharedPreferences(StatusConstant.SHARED_PREF, 0)
        val editor = pref.edit()
        editor.putString(StatusConstant.FCM_TOKEN, token)
        editor.apply()
    }

}