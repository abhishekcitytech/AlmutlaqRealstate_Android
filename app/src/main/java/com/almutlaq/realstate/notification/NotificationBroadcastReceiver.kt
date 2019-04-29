package com.almutlaq.realstate.notification

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.almutlaq.realstate.utility.FcmAlertdialogue
import com.almutlaq.realstate.utility.StatusConstant

class NotificationBroadcastReceiver : BroadcastReceiver() {

    private var act: Activity? = null
    internal var msg = ""
    internal var tag: String? = ""
    internal var page = ""
    internal var bookid = ""

    override fun onReceive(context: Context?, intent: Intent?) {
        msg = intent!!.getStringExtra(StatusConstant.fcm_message)
        tag = intent!!.getStringExtra(StatusConstant.fcm_tag)
        page = intent!!.getStringExtra(StatusConstant.fcm_page)
        bookid = intent!!.getStringExtra(StatusConstant.fcm_booking_id)
       // println("outputMsg BroadcastReciever$tag")
        act = context as Activity

        val mydialog = FcmAlertdialogue(act,msg,bookid,page)
         mydialog.show()
    }

}