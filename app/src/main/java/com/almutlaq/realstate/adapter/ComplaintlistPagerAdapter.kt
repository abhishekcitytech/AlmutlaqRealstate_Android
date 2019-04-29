package com.almutlaq.realstate.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.almutlaq.realstate.activity.ComplaintstatusActivity
import com.almutlaq.realstate.fragment.ComplaintFragment
import com.almutlaq.realstate.utility.StatusConstant
import com.almutlaq.realstate.utility.StatusConstant.INTENT_TABPOSITION

class ComplaintlistPagerAdapter(fm: FragmentManager?, tabCount: Int,complaintstatusActivity: ComplaintstatusActivity) : FragmentStatePagerAdapter(fm) {

    var tabcnt=tabCount
    val activity = complaintstatusActivity

    override fun getItem(position: Int): Fragment? {
   //  StatusConstant.INTENT_TABPOSITION = position

        when (position) {
            0 -> {
                return ComplaintFragment(activity,"C")
            }
            1 -> {
                return  ComplaintFragment(activity,"S")
            }

        }
        return null
    }

    override fun getCount(): Int {
        return tabcnt
    }


}