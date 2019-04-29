package com.almutlaq.realstate.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class IntroPageAdapter(introPage: Activity, layout: IntArray) : PagerAdapter() {

    private var  act : Activity = introPage
    private var layouts: IntArray = layout
    private var layoutinflator: LayoutInflater?=null


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutinflator = act!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        val view= layoutinflator!!.inflate(layouts[position],container,false)
        container.addView(view)
        return view
    }
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount(): Int {
       return layouts.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}