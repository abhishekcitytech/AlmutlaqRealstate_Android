package com.almutlaq.realstate.utility

import android.app.Dialog
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.almutlaq.realstate.R
import com.facebook.drawee.backends.pipeline.Fresco
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.imgzoom_dialogue.*

class ImgZoomdialogue (context: Context, img_url: String?) : Dialog(context) ,View.OnTouchListener{

    var contxt = context
    var img_url = img_url
   // private var img_service_zoom : SimpleDraweeView? = null

    private var img_service_zoom : ImageView? = null

    ///for pinch to zoom
    private val TAG = "Touch"
    private val MIN_ZOOM = 1f
    private val MAX_ZOOM = 1f

    // These matrices will be used to scale points of the image
    internal var matrix = Matrix()
    internal var savedMatrix = Matrix()

    // The 3 states (events) which the user is trying to perform
    internal val NONE = 0
    internal val DRAG = 1
    internal val ZOOM = 2
    internal var mode = NONE

    // these PointF objects are used to record the point(s) the user is touching
    internal var start = PointF()
    internal var mid = PointF()
    internal var oldDist = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(context)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.imgzoom_dialogue)
        init()
        performclick()
    }

    private fun init() {

        img_service_zoom = findViewById(R.id.img_service_zoom)

        Picasso.get()
                .load(img_url)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .fit()
                .into(img_service_zoom)
       /* val controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(img_url))
                .setTapToRetryEnabled(true)
                .build()
        img_service_zoom!!.setController(controller)*/

        img_service_zoom!!.setOnTouchListener(this)

    }


    private fun performclick() {
        tv_img_cancel.setOnClickListener { dismiss() }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val view  = v as ImageView
        view.scaleType = ImageView.ScaleType.MATRIX
        var scale: Float? = null

        dumpEvent(event!!)
        // Handle touch events here...

        when (event.getAction() and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN   // first finger down only
            -> {
                savedMatrix.set(matrix)
                start.set(event.getX(), event.getY())
                Log.d(TAG, "mode=DRAG") // write to LogCat
                mode = DRAG
            }

            MotionEvent.ACTION_UP // first finger lifted
                ,

            MotionEvent.ACTION_POINTER_UP // second finger lifted
            -> {

                mode = NONE
                Log.d(TAG, "mode=NONE")
            }

            MotionEvent.ACTION_POINTER_DOWN // first and second finger down
            -> {

                oldDist = spacing(event)
                Log.d(TAG, "oldDist=$oldDist")
                if (oldDist > 5f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                    Log.d(TAG, "mode=ZOOM")
                }
            }

            MotionEvent.ACTION_MOVE ->

                if (mode == DRAG) {
                    matrix.set(savedMatrix)
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y) // create the transformation in the matrix  of points
                } else if (mode == ZOOM) {
                    // pinch zooming
                    val newDist = spacing(event)
                    Log.d(TAG, "newDist=$newDist")
                    if (newDist > 5f) {
                        matrix.set(savedMatrix)
                        scale = newDist / oldDist // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y)
                    }
                }
        }

        view.imageMatrix = matrix // display the transformation on screen

        return true // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    /** Show an event in the LogCat view, for debugging  */
    private fun dumpEvent(event: MotionEvent) {
        val names = arrayOf("DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?")
        val sb = StringBuilder()
        val action = event.action
        val actionCode = action and MotionEvent.ACTION_MASK
        sb.append("event ACTION_").append(names[actionCode])

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
            sb.append(")")
        }

        sb.append("[")
        for (i in 0 until event.pointerCount) {
            sb.append("#").append(i)
            sb.append("(pid ").append(event.getPointerId(i))
            sb.append(")=").append(event.getX(i).toInt())
            sb.append(",").append(event.getY(i).toInt())
            if (i + 1 < event.pointerCount)
                sb.append(";")
        }

        sb.append("]")
        Log.d("Touch Events ---------", sb.toString())
    }



}