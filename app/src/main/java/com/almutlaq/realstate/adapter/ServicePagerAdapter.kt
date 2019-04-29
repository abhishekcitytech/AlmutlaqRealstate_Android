package com.almutlaq.realstate.adapter

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ServicePagerAdapter(fm: FragmentManager?, tabCount: Int, activity: Activity) : FragmentStatePagerAdapter(fm) {

    var tabcnt=tabCount
    val act = activity


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
              //  return BookedFragment(act,"0")   //TABTYPE BOOKED  - 0 ,RESCHEDULED - 1 ,CLOSED -3
            }
            1 -> {
               // return BookedFragment(act,"1")
            }
            2 -> {
              //  return BookedFragment(act,"3")
            }
        }
        return null
    }

    override fun getCount(): Int {
        return tabcnt
    }


}