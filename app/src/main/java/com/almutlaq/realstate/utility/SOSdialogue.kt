package com.almutlaq.realstate.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.dto.CustomerListModel
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.WorkHourModel
import kotlinx.android.synthetic.main.sos_dialogue.*
import java.text.SimpleDateFormat
import java.util.*

class SOSdialogue (context: Context) : Dialog(context){

    var contxt = context
    var login_response: LoginResponse? = null
    private var tvcTitletxt: TextView? = null
    var today_day : String? = ""
    var today_time : String? = ""
    var sos_number : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.sos_dialogue)
        init()
        performclick()
    }

    private fun performclick() {
        tvcall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", sos_number, null))
            context.startActivity(intent)
            dismiss()
        }

        tvcancel.setOnClickListener { dismiss() }
    }

    private fun init() {
        login_response = Commonfunctions.get_login_data(contxt as Activity)
        tvcTitletxt = findViewById(R.id.tvcTitletxt)
        //for current day
        val dayFormat = SimpleDateFormat("EEEE", Locale.US)
        val calendar = Calendar.getInstance()
        today_day = dayFormat.format(calendar.time)
        // for current time
        val sdf = SimpleDateFormat("HH:mm:ss")
        today_time = sdf.format(Date())

        //tvcTitletxt!!.text = "Do you want to call ? "+today_day+" & today time: "+today_time
        for (i in 0..login_response!!.data.working_hour.size -1){

            var get_status = login_response!!.data.working_hour.get(i).dailytime_status
                if(get_status.equals("1")){
                    var get_day = login_response!!.data.working_hour.get(i).dailytime_day
                    if(get_day.equals(today_day)){
                        var get_open_time = login_response!!.data.working_hour.get(i).dailytime_opening
                        var get_close_time = login_response!!.data.working_hour.get(i).dailytime_closing
                        var get_open_time2 = login_response!!.data.working_hour.get(i).dailytime_opening_2
                        var get_close_time2 = login_response!!.data.working_hour.get(i).dailytime_closing_2

                        if(get_open_time < today_time.toString() && get_close_time > today_time.toString() ||
                                get_open_time2 < today_time.toString() && get_close_time2 > today_time.toString()){
                            sos_number  = login_response!!.data.Office_SOS
                            tvcTitletxt!!.text = "Do you want to call "+sos_number+"?"
                        }else{
                            sos_number  = login_response!!.data.Private_SOS
                            tvcTitletxt!!.text = "Do you want to call "+sos_number+"?"
                        }
                    }
                }else{
                    sos_number  = login_response!!.data.Private_SOS
                    tvcTitletxt!!.text = "Do you want to call "+sos_number+"?"
                }

        }

    }

    fun isBetweenValidTime(startTime: Date, endTime: Date, validateTime: Date): Boolean {
        var validTimeFlag = false

        if (endTime.compareTo(startTime) <= 0) {
            if (validateTime.compareTo(endTime) < 0 || validateTime.compareTo(startTime) >= 0) {
                validTimeFlag = true
            }
        } else if (validateTime.compareTo(endTime) < 0 && validateTime.compareTo(startTime) >= 0) {
            validTimeFlag = true
        }

        return validTimeFlag
    }

}