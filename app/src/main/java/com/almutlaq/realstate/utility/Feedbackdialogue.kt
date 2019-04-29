package com.almutlaq.realstate.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AbsSeekBar
import android.widget.EditText
import android.widget.RatingBar
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ThankyouActivity
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.RatingResponse
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.RatingInput
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feedback_dialogue.*

class Feedbackdialogue (context: Context, screen_type:String,serviceid: Int?, tenant_rating: Float?, tenant_rating_note: String?) : Dialog(context){

    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null
    var login_response: LoginResponse? = null
    var s_id = serviceid
    var note = tenant_rating_note
    var contxt = context
    var rate :Float? = tenant_rating
    var screen_type = screen_type
    private var et_ss_note : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.feedback_dialogue)
        init()
/*

        if(screen_type.equals("T")){

        }
*/
        if(screen_type.equals("S")){
            et_ss_note!!.isEnabled = false
            rating_feedbck.isEnabled = false
        }

        if (rate != 0.0F){
            et_ss_note!!.isEnabled = false
            rating_feedbck.isEnabled = false

            rating_feedbck.rating = rate as Float
            et_ss_note!!.setText(note)
            ratingsmileys()
            tv_fdback_submit.visibility = View.GONE
            tv_fdback_cancel.text = "Cancel"
        }else{
            sendratingfeedback(this!!.rate!!, this!!.note!!)
        }

        performclick()
    }

    private fun init() {

        connectioncheck =  NetworkConnetionCheck(contxt)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        login_response = Commonfunctions.get_login_data(contxt as Activity)
        et_ss_note =  findViewById(R.id.et_ss_note)

        //tv_fdback_cancel.text = "Not now"


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                val progressDrawable = rating_feedbck.getProgressDrawable()
                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable!!, ContextCompat.getColor(contxt, R.color.OrangeLite))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            try {
                val field = AbsSeekBar::class.java.getDeclaredField("mTouchProgressOffset")
                field.isAccessible = true
                field.set(rating_feedbck, 0.6f)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }

    }

    private fun performclick() {

        rating_feedbck.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->

             //   rate = rating_feedbck.getRating()
            rate = rating
            ratingsmileys()
        }


        tv_fdback_cancel.setOnClickListener { dismiss() }

        tv_fdback_submit.setOnClickListener {

            val myrating : Float = rating_feedbck.getRating()
            val feedback_note : String = et_ss_note!!.text.toString().trim()
            Log.e("myrating", rating_feedbck.getRating().toString())

            if (myrating < 1.0F)
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.RATING_ERROR_MSG).show()
            else if (feedback_note.isEmpty()){
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.RATING_FEEDBACK_ERR_MSG).show()
            }
            else {
                sendratingfeedback(myrating,feedback_note)
            }



        }

    }

    private fun ratingsmileys() {
        Log.e("rating", rating_feedbck.getRating().toString())
        img_emojiHint.visibility = View.GONE
        tv_fdbck_Emoji.visibility = View.VISIBLE

        if (rate == 1.0F){
            tv_fdbck_Emoji.setText(R.string.emoji_bad)
            tv_fdbckmoji_name.setText("Very Bad")

        }else if (rate == 2.0F){
            tv_fdbck_Emoji.setText(R.string.emoji_ok)
            tv_fdbckmoji_name.setText("Bad")

        }else if (rate == 3.0F){
            tv_fdbck_Emoji.setText(R.string.emoji_good)
            tv_fdbckmoji_name.setText("Good")


        }else if (rate == 4.0F){
            tv_fdbck_Emoji.setText(R.string.emoji_happy)
            tv_fdbckmoji_name.setText("Very Good")


        }else if (rate == 5.0F){
            tv_fdbck_Emoji.setText(R.string.emoji_excellent)
            tv_fdbckmoji_name.setText("Excellent")

        }

    }

    private fun sendratingfeedback(rating: Float, feedback_note: String) {

        if(connectioncheck!!.isNetworkAvailable()) {

            val additem = RatingInput()
            additem.tenant_rating = rating.toInt()
            additem.tenant_rating_note = feedback_note

            val ratingsuccessRespeObservable = apiService!!.addrating(this!!.s_id!!,"Bearer"+ login_response!!.data.token,additem)

            ratingsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RatingResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onNext(response: RatingResponse)
                        {
                            //  progress_changepw!!.visibility = View.GONE
                            Log.e("resp", "onNext: feedback response " + response.message)
                            ResponseMessage = response.message
                            if (response.message_code.equals("1")) {
                                dismiss()
                                val nxt1 = Intent(contxt, ThankyouActivity::class.java)
                                nxt1.putExtra(StatusConstant.INTENT_THANKYOU_MSG,"Feedbackdialogue")
                                nxt1.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT,ResponseMessage.toString())
                                contxt.startActivity(nxt1)

                            } else {
                                if (ResponseMessage.equals("Rating already set")){
                                    rating_feedbck.rating = rating
                                    et_ss_note!!.setText(feedback_note)
                                    ratingsmileys()
                                    tv_fdback_submit.visibility = View.GONE
                                    tv_fdback_cancel.text = "Cancel"

                                }
                                else {
                                    Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                }
                            }
                        }
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                        override fun onComplete() {

                        }
                    })
        }else{
            connectioncheck!!.getNetworkActiveAlert().show()
        }

    }


}