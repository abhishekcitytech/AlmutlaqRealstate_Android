package com.almutlaq.realstate.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.almutlaq.realstate.R
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.activity_deactivation.*
import kotlinx.android.synthetic.main.activity_servicedetails.*
import kotlinx.android.synthetic.main.activity_thankyou.*

class DeactivationActivity  : AppCompatActivity(){
    var message : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deactivation)

        message = StatusConstant.SERVER_MSG
        tv_message.text = message.toString()
        performclick()
    }

    private fun performclick() {
        img_btn_cross.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val nxt = Intent(this@DeactivationActivity, Login::class.java)
                StatusConstant.SERVER_MSG =""
                nxt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(nxt)
            }
        })
    }
}