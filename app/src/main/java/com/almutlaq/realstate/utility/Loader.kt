package com.almutlaq.realstate.utility

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.widget.ImageView
import com.almutlaq.realstate.R

class Loader : ImageView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        try {
            setBackgroundResource(R.drawable.loader)
            val frameAnimation = background as AnimationDrawable
            post { frameAnimation.start() }
        } catch (e: Exception) {
            print("Error loader" + e.toString())
        }

    }
}