package com.almutlaq.realstate.utility

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ComplaintstatusActivity
import com.almutlaq.realstate.activity.NotificationListActivity
import com.almutlaq.realstate.activity.SelfsuserServicesDetails
import com.almutlaq.realstate.activity.ServiceDetails
import kotlinx.android.synthetic.main.fcm_dialogue.*

class FcmAlertdialogue (context: Activity?, status: String, bookid: String, page: String) : Dialog(context){
    var contxt = context
    var msg : String = status
    var service_bookid  = bookid
    var pagename : String = page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.fcm_dialogue)
       init()
        performclick()
    }

   private fun init(){

       heading.text = msg

   }

    private fun performclick() {
        tvcancelfcm.setOnClickListener {
            dismiss()
        }

        tvokfcm.setOnClickListener {

            if (service_bookid.isNullOrEmpty())
                  service_bookid = ""

                if(pagename.equals("customer service details")){
                    //startActivity(Intent(this@Splash, SelfsuserServicesDetails::class.java))
                    val intent = Intent(contxt, SelfsuserServicesDetails::class.java)
                    intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID, service_bookid.toInt())
                    contxt!!.startActivity(intent)
                    //finish()
                }else if(pagename.equals("tenant service details")){
                    //startActivity(Intent(this@Splash, ServiceDetails::class.java))
                    val intent = Intent(contxt, ServiceDetails::class.java);
                    intent.putExtra(StatusConstant.INTENT_TSERVICE_BOOKINGID, service_bookid)
                    contxt!!.startActivity(intent)
                    //finish()
                }else if(pagename.equals("notification")){
                    contxt!!.startActivity(Intent(contxt, NotificationListActivity::class.java))
                    //finish()
                }else if(pagename.equals("complaint listing")){
                    val intent = Intent(contxt, ComplaintstatusActivity::class.java)
                    intent.putExtra(StatusConstant.INTENT_TABPOSITION, "C")
                    contxt!!.startActivity(intent)

                }else if(pagename.equals("suggestion listing")){
                    val intent = Intent(contxt, ComplaintstatusActivity::class.java)
                    intent.putExtra(StatusConstant.INTENT_TABPOSITION, "S")
                    contxt!!.startActivity(intent)
                }

            dismiss()
        }
    }



}