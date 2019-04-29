package com.almutlaq.realstate.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.adapter.IntroPageAdapter
import com.almutlaq.realstate.utility.Commonfunctions
import kotlinx.android.synthetic.main.activity_intro.*

class IntroPage : AppCompatActivity() {

    private var layouts: IntArray? = null
    private var dots: Array<TextView?>? = null
    private var intropageadapter: IntroPageAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        init()
    }

    private fun init() {

        Commonfunctions.set_intro_data(this,true)
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        layouts = intArrayOf(R.layout.splash_intro1,
                R.layout.splash_intro1,
                R.layout.splash_intro1,
                R.layout.splash_intro1)

        addBottomDots(0)
        changeStatusBarColor()

        intropageadapter = IntroPageAdapter(this, layouts!!)
        view_pager.adapter=intropageadapter
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_skip.setOnClickListener{
            launchHomeScreen()
        }
        btn_next.setOnClickListener{
            val current=getItem(+1)
            if (current < layouts!!.size) {
                // move to next screen
                view_pager.currentItem = current as Int
            } else {
                launchHomeScreen()
            }
        }

    }

    private fun getItem(i: Int): Int {
        return view_pager.currentItem + i
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls<TextView>(layouts!!.size)

        val colorsActive: IntArray = resources.getIntArray(R.array.array_dot_active)
        val colorsInActive: IntArray = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout.removeAllViews()

        for (i in 0 until dots!!.size) {
            dots!![i] = TextView(this)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots!![i]!!.text = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
            } else dots!![i]!!.text = Html.fromHtml("&#8226;")

            dots!![i]!!.textSize = 35F
            dots!![i]!!.setTextColor(colorsInActive[currentPage])
            dotsLayout.addView(dots!![i])
        }

        if (dots!!.isNotEmpty())
            dots!![currentPage]!!.setTextColor(colorsActive[currentPage])

    }


    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun launchHomeScreen() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }


    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts!!.size - 1) {
                // last page. make button text to GOT IT
                btn_next.text = getString(R.string.start)
                btn_skip.visibility=View.GONE
            } else {
                // still pages are left
                btn_next.text = getString(R.string.next)
                btn_skip.visibility=View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }
}