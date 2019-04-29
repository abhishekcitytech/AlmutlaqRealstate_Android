package com.almutlaq.realstate.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.almutlaq.realstate.R
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.activity_thankyou.*

class ThankyouActivity  : AppCompatActivity(){

    var  msgpage  : String? = null
    var  msgtxt  : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thankyou)
        init()
        performclick()
    }

    private fun init(){
        if (!intent.extras.equals("")) {
            msgpage =  intent.getStringExtra(StatusConstant.INTENT_THANKYOU_MSG)
            msgtxt = intent.getStringExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT)
        }

        if (msgpage.equals("Acknowledgementdialogue")){
            tvThankyou1.visibility = View.VISIBLE
            tvThankyou1.text = StatusConstant.ACK_THANKYOU_MSG
        }else if (msgpage.equals("ServiceRequest")){
            tvThankyou1.visibility = View.VISIBLE
            tvThankyou1.text = msgtxt
        }
        else if (msgpage.equals("ServiceStatusEvent")){
           // tvThankyou1.visibility = View.VISIBLE
           // tvThankyou1.text = msgtxt
            tvThankyou1.visibility = View.INVISIBLE
            Commonfunctions.commonDialogue(this, StatusConstant.ALERT, this!!.msgtxt!!).show()
        }
        else{
            tvThankyou1.visibility = View.INVISIBLE
        }

    }

    private fun performclick() {
        btn_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val nxt = Intent(this@ThankyouActivity, DashboardNavigation::class.java)
                nxt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(nxt)
            }
        })
    }
}