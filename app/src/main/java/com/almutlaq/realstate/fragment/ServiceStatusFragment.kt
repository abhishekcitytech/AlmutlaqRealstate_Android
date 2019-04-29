package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.adapter.ServicePagerAdapter
import kotlinx.android.synthetic.main.activity_main_drawer.*

@SuppressLint("ValidFragment")
class ServiceStatusFragment @SuppressLint("ValidFragment") constructor
(act: Activity): Fragment()  {

    val activity = act

    var event_tab_layout: TabLayout?=null
    var event_pager: ViewPager?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater!!.inflate(R.layout.service_status_fragment, container, false)
        init(rootview)
        set_tab()
        return rootview
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

                    (activity as DashboardNavigation).clickDashboardfragment()
                    true
                } else false
            }
        })

        ///////
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun init(rootview: View?) {

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }

        event_tab_layout = rootview!!.findViewById(R.id.event_tab_layout)
        event_pager = rootview!!.findViewById(R.id.event_pager)

    }

     private fun set_tab() {
       event_tab_layout!!.addTab(event_tab_layout!!.newTab().setText("Booked"))
       event_tab_layout!!.addTab(event_tab_layout!!.newTab().setText("Rescheduled"))
       event_tab_layout!!.addTab(event_tab_layout!!.newTab().setText("Closed"))

       event_tab_layout!!.tabGravity=TabLayout.GRAVITY_FILL

       val fragmentManager = fragmentManager
//        val fragmentManager = getSupportFragmentManager()
       val srvpageradapter= ServicePagerAdapter(fragmentManager,event_tab_layout!!.tabCount,activity)
       event_pager!!.adapter=srvpageradapter

       event_pager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(event_tab_layout))

       event_tab_layout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
           override fun onTabSelected(tab: TabLayout.Tab) {
               event_pager!!.currentItem = tab.position

           }

           override fun onTabUnselected(tab: TabLayout.Tab) {

           }

           override fun onTabReselected(tab: TabLayout.Tab) {

           }
       })

   }
}