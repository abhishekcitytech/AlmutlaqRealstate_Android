package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.ChangePassworddialogue
import com.almutlaq.realstate.utility.Commonfunctions
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_main_drawer.*

@SuppressLint("ValidFragment")
class MyAccountFragment @SuppressLint("ValidFragment") constructor
(act: Activity): Fragment() {

    val activity = act
    //private var _pref: Pref? = null

    private var img_account : SimpleDraweeView? = null
    private var tv_acc_name : TextView? = null
    private var tv_acc_mail : TextView? = null
    private var tv_acc_mob : TextView? = null
    private var tv_acc_addr1 : TextView? = null
    private var tv_acc_addr2 : TextView? = null
    private var  tv_acc_exp_date : TextView? = null
    private var tv_acc_exp : TextView? = null
    private var  btnAc_status : Button? = null
    private var tv_customer_title : TextView? = null
    private var customer_title_bottom_line : View? =null
    private var ragreement_exp : RelativeLayout? = null
    var img_active : ImageView? = null
    var img_inactive : ImageView? = null
    var login_response: LoginResponse? = null
    var token : String? = null

    var rChangepw : RelativeLayout? = null
    var acUserType : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fresco.initialize(activity)
        val rootview = inflater!!.inflate(R.layout.my_account_fragment, container, false)
        init(rootview)

        performclick()
        return rootview
    }

    private fun performclick() {

        rChangepw!!.setOnClickListener {
            val mydialog = ChangePassworddialogue(activity)
            mydialog.show()
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onPause() {
        super.onPause()
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.drawable.toolbar_bg)//R.drawable.toolbar_bg
        //  (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(resources.getColor("ffffff"))
        (getActivity() as DashboardNavigation). actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        }else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        ///////// FOR BACKPRESS TO GO TO DASHBOARD FRAGMENT
        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.getAction() === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    if (login_response!!.data.user_type.equals("T")){
                        (activity as DashboardNavigation).clickDashboardfragment()
                    }else if (login_response!!.data.user_type.equals("S")){
                        (activity as DashboardNavigation).clickSserviceHomefragment()
                    }
                    true
                } else false
            }
        })

        ///////
        //  if (activity is DashboardNavigation) {
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }
        /* }else{
             activity.toolbartitle.setText(hdr)
             activity.toolbartitle.setTextColor(getResources().getColor(R.color.white))
         }*/
    }

    @SuppressLint("ResourceAsColor")
    private fun init(rootview: View?) {
      //  _pref = Pref(activity)
        login_response = Commonfunctions.get_login_data(activity)
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }

        rChangepw = rootview!!.findViewById(R.id.rChangepw)
        img_account =  rootview.findViewById(R.id.img_account)
        setimg()

        tv_acc_name = rootview.findViewById(R.id.tv_acc_name)
        tv_acc_mail = rootview.findViewById(R.id.tv_acc_mail)
        tv_acc_mob = rootview.findViewById(R.id.tv_acc_mob)
        tv_acc_addr1 = rootview.findViewById(R.id.tv_acc_addr1)
        tv_acc_addr2 = rootview.findViewById(R.id.tv_acc_addr2)
        tv_acc_exp_date = rootview.findViewById(R.id.tv_acc_exp_date)
        tv_acc_exp = rootview.findViewById(R.id.tv_acc_exp)
        img_active  = rootview.findViewById(R.id.img_active)
        img_inactive  = rootview.findViewById(R.id.img_inactive)

        btnAc_status = rootview.findViewById(R.id.btnAc_status)
        customer_title_bottom_line = rootview.findViewById(R.id.customer_title_bottom_line)
        tv_customer_title = rootview.findViewById(R.id.tv_customer_title)
        ragreement_exp = rootview.findViewById(R.id.ragreement_exp)

        acUserType =  login_response!!.data.user_type
        token = login_response!!.data.token
        tv_acc_name!!.text = login_response!!.data.customer_name
        tv_acc_mail!!.text = login_response!!.data.customer_email
        tv_acc_mob!!.text = login_response!!.data.customer_phone
        tv_acc_addr1!!.text =login_response!!.data.customer_flat_no + "," + login_response!!.data.customer_building_no
        tv_acc_addr2!!.text = login_response!!.data.customer_street_address
        tv_acc_exp_date!!.text = login_response!!.data.customer_Expiry_Date


        val acStatus : String = login_response!!.data.customer_account_status

        if (acStatus.toString().equals("Y")){
           // img_active!!.visibility = View.VISIBLE
           // img_inactive!!.visibility = View.GONE
            tv_acc_exp!!.text = "Document Expiry"
            btnAc_status!!.text = "Active"
            //tv_acc_exp!!.text = "Agreement expiring on"
        }else{
           // img_active!!.visibility = View.GONE
          //  img_inactive!!.visibility = View.VISIBLE
            tv_acc_exp!!.text = "Document Expiry"
            btnAc_status!!.text = "Inactive"
            //tv_acc_exp!!.text = "Agreement expired on"
        }

        if(acUserType.equals("S")){
            tv_customer_title!!.visibility = View.VISIBLE
            customer_title_bottom_line!!.visibility = View.VISIBLE
            img_account!!.visibility = View.INVISIBLE
            tv_acc_addr1!!.visibility = View.GONE
            tv_acc_addr2!!.visibility = View.INVISIBLE
            ragreement_exp!!.visibility = View.GONE
        }

    }

    private fun setimg() {
        val img  =login_response!!.data.customer_profilePic
        val controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(img))
                .setTapToRetryEnabled(true)
                .build()
        img_account!!.setController(controller)
    }
}