package com.almutlaq.realstate.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.almutlaq.realstate.R
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.StatusConstant

class Splash : AppCompatActivity(){
  //  val screen_time=15000
       // private var _pref: Pref? = null
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    var login_response: LoginResponse? = null

    var pagename : String? = ""
    var msgtitle: String? = ""
    var msgbody : String? = ""
    var bookid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
       // _pref = Pref(this)
        login_response = Commonfunctions.get_login_data(this)

        /*val wlthread= object : Thread(){
            var wait = 0
            override fun run() {
                super.run()
                try {
                    while (wait<screen_time) {
                        sleep(100)
                        wait += 1000
                    }

                }catch (e :Exception){

                }finally {
                    start_activity()
                }
            }

        }
         wlthread.start()*/

        val mRunnable: Runnable = Runnable {
            if (!isFinishing) {
                start_activity()
            }
        }
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    private fun start_activity() {
      //  val extras = intent.extras
        val  checkintro : Boolean = Commonfunctions.get_intro_data(this)
        if (Commonfunctions.checkConnectivity(this@Splash)){

            if (login_response!!.data.user_type.isEmpty()){
                 if (checkintro){
                startActivity(Intent(this@Splash,Login::class.java))
                finish()
                 } else{
                     startActivity(Intent(this@Splash,IntroPage::class.java))
                     finish()
                }
            }else{
                val extras = intent.extras
                if (login_response!!.data.forcechangepassword.equals("1")) {
                    if (extras!= null && !extras.isEmpty) {
                        intentchk()
                    }
                    else {
                        startActivity(Intent(this@Splash, DashboardNavigation::class.java))
                        finish()
                }
                }else{
                    startActivity(Intent(this@Splash, Login::class.java))
                    finish()
                }
            }

        }
    }

    private fun intentchk() {

        pagename = intent.getStringExtra("pagename")
        msgtitle = intent.getStringExtra("msgtitle")
        msgbody = intent.getStringExtra("msgbody")
        bookid = intent.getStringExtra("bookid")

          if (bookid.isNullOrEmpty())
               bookid = ""

        if(pagename.equals("customer service details")){
            //startActivity(Intent(this@Splash, SelfsuserServicesDetails::class.java))
            val intent = Intent(this@Splash,SelfsuserServicesDetails::class.java)
            intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID, bookid!!.toInt())
            startActivity(intent)
            finish()
        }else if(pagename.equals("tenant service details")){
            //startActivity(Intent(this@Splash, ServiceDetails::class.java))
            val intent = Intent(this@Splash,ServiceDetails::class.java)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_BOOKINGID, bookid)
            startActivity(intent)
            finish()
        }else if(pagename.equals("notification")){
            startActivity(Intent(this@Splash, NotificationListActivity::class.java))
            finish()
        }else if(pagename.equals("complaint listing")){
            val intent = Intent(this@Splash,ComplaintstatusActivity::class.java)
            intent.putExtra(StatusConstant.INTENT_TABPOSITION, "C")
            startActivity(intent)
            finish()
        }else if(pagename.equals("suggestion listing")){
            val intent = Intent(this@Splash,ComplaintstatusActivity::class.java)
            intent.putExtra(StatusConstant.INTENT_TABPOSITION, "S")
            startActivity(intent)
            finish()
        }else if(pagename.isNullOrEmpty()){
            startActivity(Intent(this@Splash, DashboardNavigation::class.java))
            finish()
        }

    }

}