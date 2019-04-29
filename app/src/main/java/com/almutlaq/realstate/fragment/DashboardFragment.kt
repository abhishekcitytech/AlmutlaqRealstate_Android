package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ComplaintstatusActivity
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.utility.SOSdialogue

@SuppressLint("ValidFragment")
class DashboardFragment @SuppressLint("ValidFragment") constructor
(act: Activity): Fragment() {
    private var pd: Dialog? = null
    val activity = act

    var txt: TextView?=null
    var txt1: TextView?=null
    var txt3: TextView?=null
    var txt4: TextView?=null
    var txt5: TextView?=null

    var cvBookservice : CardView? = null
    var  cvServiceStatus : CardView? = null
    var  cvRegisterComplaint : CardView? = null
    var  cvComplainStatus : CardView? = null
    var  cvMyAccount : CardView? = null
    var  cvSOS : CardView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater!!.inflate(R.layout.fragment_dahboard, container, false)
        initialize(rootview)
        performclick()
        return rootview
    }


    private fun initialize(rootview: View) {
        txt = rootview!!.findViewById(R.id.txt0)
        txt1 = rootview!!.findViewById(R.id.txt1)
        txt3 = rootview!!.findViewById(R.id.txt3)
        txt4 = rootview!!.findViewById(R.id.txt4)
        txt5 = rootview!!.findViewById(R.id.txt5)

        val text = "<font color=##000000>Book</font> <font color=#D1B62B>Service</font>"
        val text1 = "<font color=##000000>Service</font> <font color=#D1B62B>Status</font>"
        val text3 = "<font color=##000000>Register</font> <font color=#D1B62B>Complaint</font>"
        val text4 = "<font color=##000000>Complain / </font> <font color=#D1B62B>Suggestion</font>"
        val text5 = "<font color=##000000>My</font> <font color=#D1B62B>Account</font>"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txt!!.text = Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY)
            txt1!!.text = Html.fromHtml(text1,Html.FROM_HTML_MODE_LEGACY)
            txt3!!.text = Html.fromHtml(text3,Html.FROM_HTML_MODE_LEGACY)
            txt4!!.text = Html.fromHtml(text4,Html.FROM_HTML_MODE_LEGACY)
            txt5!!.text = Html.fromHtml(text5,Html.FROM_HTML_MODE_LEGACY)
        }
        else{
            txt!!.text = Html.fromHtml(text)
            txt1!!.text = Html.fromHtml(text1)
            txt3!!.text = Html.fromHtml(text3)
            txt4!!.text = Html.fromHtml(text4)
            txt5!!.text = Html.fromHtml(text5)
        }

         cvBookservice = rootview!!.findViewById(R.id.card_view0)
         cvServiceStatus = rootview!!.findViewById(R.id.card_view1)
         cvRegisterComplaint = rootview!!.findViewById(R.id.card_view3)
         cvComplainStatus = rootview!!.findViewById(R.id.card_view4)
         cvMyAccount = rootview!!.findViewById(R.id.card_view5)
         cvSOS = rootview!!.findViewById(R.id.card_view6)
    }

    @SuppressLint("ResourceType")
    private fun performclick() {

        cvBookservice!!.setOnClickListener {
            (activity as DashboardNavigation).clickBookservicebtn()
        }

        cvServiceStatus!!.setOnClickListener {
            (activity as DashboardNavigation).clickServiceStatusbtn()
        }

        cvRegisterComplaint!!.setOnClickListener {
            (activity as DashboardNavigation).clickRegisterComplaintbtn()
        }

        cvComplainStatus!!.setOnClickListener {
          activity.startActivity(Intent(activity,ComplaintstatusActivity::class.java))
        }

        cvMyAccount!!.setOnClickListener {
            (activity as DashboardNavigation).clickMyAccountbtn()
        }

        cvSOS!!.setOnClickListener {
            val mydialog = SOSdialogue(activity)
            mydialog.show()
        }
    }




}