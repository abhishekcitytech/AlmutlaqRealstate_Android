package com.almutlaq.realstate.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.almutlaq.realstate.R
import kotlinx.android.synthetic.main.toolbar_for_activity.*

class Complaintdetails : AppCompatActivity() {

    var cdetailstoolbar : RelativeLayout? = null
    var  description  : String? = ""
    var  problem  : String? = ""
    var  problem_status  : String? = ""
    var  complain_closed_note  : String? = ""
    var tvCdetails_title : TextView? =null
    var tv_problem : TextView? = null
    var tv_close_note : TextView? = null
    var btn_cdetails_open : TextView? = null
    var btn_cdetails_closed : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complaint_details_activity)
        init()
    }

    private fun init() {
        if (!intent.extras.equals("")) {
            description = intent.getStringExtra("Title")
            problem = intent.getStringExtra("Problem")
            problem_status = intent.getStringExtra("Problem_Status")
            complain_closed_note = intent.getStringExtra("Closed_Note")
        }
        cdetailstoolbar = findViewById(R.id.cdetailstoolbar)
        tvCdetails_title = findViewById(R.id.tvCdetails_title)
        tv_problem = findViewById(R.id.tv_problem)
        tv_close_note = findViewById(R.id.tv_close_note)
        btn_cdetails_open = findViewById(R.id.btn_cdetails_open)
        btn_cdetails_closed = findViewById(R.id.btn_cdetails_closed)

        toolbartitle.setText("Complaint Details")
        tvCdetails_title!!.setText(description)
        tv_problem!!.setText(problem)
        if (problem_status.equals("0")){
            btn_cdetails_open!!.visibility = View.VISIBLE
            btn_cdetails_closed!!.visibility = View.GONE
            tv_close_note!!.visibility = View.GONE

        }else if (problem_status.equals("1")){
            btn_cdetails_open!!.visibility = View.GONE
            btn_cdetails_closed!!.visibility = View.VISIBLE
            tv_close_note!!.visibility = View.VISIBLE
            tv_close_note!!.setText("Closed Note: "+complain_closed_note)
        }
        imgToolback.setOnClickListener { onBackPressed() }

    }
}