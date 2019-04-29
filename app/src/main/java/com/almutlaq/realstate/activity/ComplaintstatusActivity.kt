package com.almutlaq.realstate.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RelativeLayout
import com.almutlaq.realstate.R
import com.almutlaq.realstate.adapter.ComplaintlistPagerAdapter
import com.almutlaq.realstate.fragment.RegisterComplaintFragment
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.complaint_list_activity.*
import kotlinx.android.synthetic.main.toolbar_for_activity.*

class ComplaintstatusActivity  : AppCompatActivity()  {

    var ctoolbar : RelativeLayout? = null
    var title=""
    internal var fragmentmanager: FragmentManager? = null
    //internal var fragmentmanager: FragmentManager? = null
    //var title=""

    var tabposition : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complaint_list_activity)
        init()
        set_tab()
    }

    private fun init() {
        ctoolbar = findViewById(R.id.ctoolbar)
        //fragmentmanager = supportFragmentManager
        toolbartitle.setText("Complaint List")
        imgToolAdd.visibility = View.VISIBLE
        imgToolback.setOnClickListener { onBackPressed() }
        fragmentmanager = supportFragmentManager
        imgToolAdd.setOnClickListener {
           //(this as DashboardNavigation).clickRegisterComplaintbtn()
            val intent = Intent(this, DashboardNavigation::class.java)
            intent.putExtra("RegisterComplaint Fragment", "RegisterComplaint Fragment")
            startActivity(intent)
        }
    }

    private fun set_tab() {
        clist_tab_layout!!.addTab(clist_tab_layout!!.newTab().setText("Complaint"))
        clist_tab_layout!!.addTab(clist_tab_layout!!.newTab().setText("Suggestion"))

        clist_tab_layout!!.tabGravity=TabLayout.GRAVITY_FILL

        val fragmentManager = supportFragmentManager
        val clistpageradapter= ComplaintlistPagerAdapter(fragmentManager,clist_tab_layout!!.tabCount,this)
        clist_pager!!.adapter=clistpageradapter


        clist_pager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(clist_tab_layout))

        clist_tab_layout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                clist_pager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onResume() {
        super.onResume()

        if (intent.extras!= null && !intent.extras.isEmpty)
            tabposition =  intent.getStringExtra(StatusConstant.INTENT_TABPOSITION)

        if (tabposition.isNullOrEmpty()) {
            tabposition = ""
            clist_pager.currentItem = 0
        }
        else if (tabposition.equals("C")){
            clist_pager.currentItem = 0
        }
        else if (tabposition.equals("S")){
            clist_pager.currentItem = 1
        }
    }

   /* public fun clickRegisterComplaintbtn() {
        imgToolAdd.visibility = View.GONE
       // title = "Register Complaint"
        val fragmenttransaction = fragmentmanager!!.beginTransaction()
        val regf: RegisterComplaintFragment = RegisterComplaintFragment(this)
        fragmenttransaction.replace(R.id.container_body_new, regf, "RegisterComplaint Fragment")
        fragmenttransaction.commit()
      //  supportActionBar!!.title = title

       *//* var fragment: Fragment? = null
        fragment= RegisterComplaintFragment(this@ComplaintstatusActivity)*//*
    }*/
}