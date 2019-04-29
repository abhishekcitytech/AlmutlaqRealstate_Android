package com.almutlaq.realstate.activity

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.almutlaq.realstate.R
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.LogOutResponse
import com.almutlaq.realstate.dto.LoginInput
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.LogoutInput
import com.almutlaq.realstate.fragment.*
import com.almutlaq.realstate.fragment.selfserviceuserFragments.BookedServiceFragment
import com.almutlaq.realstate.fragment.selfserviceuserFragments.ClosedServiceFragment
import com.almutlaq.realstate.fragment.selfserviceuserFragments.RescheduledServiceFragment
import com.almutlaq.realstate.fragment.selfserviceuserFragments.SelfServiceHomeFragment
import com.almutlaq.realstate.notification.NotificationBroadcastReceiver
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.SOSdialogue
import com.almutlaq.realstate.utility.StatusConstant
import com.almutlaq.realstate.utility.StatusConstant.NOTIFICATION_ACTION
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_drawer.*

open class DashboardNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var title=""
    public var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var header: View? = null
    internal var fragmentmanager: FragmentManager? = null
  //  private var _pref: Pref? = null

    private var profile_image : SimpleDraweeView? = null
    private var txt_name: TextView? = null
    private var txt_addr : TextView? = null
    private var txt_phn : TextView? = null

    var login_response: LoginResponse? = null
    var user_mobile: String? = ""
    private val TAG_FRAGMENT = "TAG_FRAGMENT"
    var fragment: Fragment? = null

    private var doubleBackToExitPressedOnce = false

    var  regfragintent  : String? = ""

    var  bookfragintent  : String? = ""
    internal var notificationBroadcastReceiver: NotificationBroadcastReceiver = NotificationBroadcastReceiver()

    private var pd: Dialog? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rs="print"
        println("Start print"+rs)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main_drawer)
      //  setContentView(R.layout.activity_main_drawer_new)
        init()
        performclick()
    }

    public override fun onResume() {
        super.onResume()

//        if (!intent.extras.equals("")) {

        regfragintent = intent.getStringExtra("RegisterComplaint Fragment")

        bookfragintent = intent.getStringExtra("Booked Fragment")

        when (regfragintent) {

            "RegisterComplaint Fragment" ->
                //here you can set Fragment B to your activity as usual;
                clickRegisterComplaintbtn()
        }


        when (bookfragintent) {

            "Booked Fragment" ->
                //here you can set Fragment B to your activity as usual;
                clickServiceStatusbtn()
        }
    }

    private fun init() {
        // _pref = Pref(this)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundResource(R.drawable.toolbar_bg)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        header = navigationView!!.getHeaderView(0)
        profile_image =  header!!.findViewById(R.id.profile_image)
        txt_name =  header!!.findViewById(R.id.txt_name)
        txt_addr =  header!!.findViewById(R.id.txt_addr)
        txt_phn =  header!!.findViewById(R.id.txt_phn)

       login_response = Commonfunctions.get_login_data(this@DashboardNavigation)

        txt_name!!.text = login_response!!.data.customer_name
        txt_addr!!.text = login_response!!.data.customer_flat_no + "," + login_response!!.data.customer_building_no +
                "," +  login_response!!.data.customer_street_address
        txt_phn!!.text = login_response!!.data.customer_phone
        user_mobile = login_response!!.data.customer_email

        setimg()

        drawer_layout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null
        display_view(R.id.nav_home)
        fragmentmanager = supportFragmentManager

        connectioncheck =  NetworkConnetionCheck(this)
        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(NOTIFICATION_ACTION)
        registerReceiver(notificationBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(notificationBroadcastReceiver)
    }


    private fun setimg() {

        val img = login_response!!.data.customer_profilePic

        val controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(img))
                .setTapToRetryEnabled(true)
                .build()
        profile_image!!.setController(controller)
    }

    private fun performclick() {

        imgNotify.setOnClickListener { ///
           startActivity(Intent(this,NotificationListActivity::class.java))
            }
        btnLogout.setOnClickListener {

            val fcmtokenn = getRegIdInPref()
            Log.e("fcmtoken_logout",fcmtokenn)
            pd!!.show()
            callLogoutdata(fcmtokenn)

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        display_view(id)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun display_view(id: Int) {

        //var fragment: Fragment? = null

        val nav_Menu = navigationView.menu

        if (login_response!!.data.user_type.equals("T")){

            nav_Menu.findItem(R.id.bookedServices).isVisible = false
            nav_Menu.findItem(R.id.rescheduledservices).isVisible = false
            nav_Menu.findItem(R.id.closedservices).isVisible = false
            imgNotify.visibility = View.GONE

            if (id==R.id.nav_home){

                fragment= DashboardFragment(this)
                title ="Home"
                imgNotify.visibility = View.VISIBLE
                //  toolbar.setBackgroundResource(R.drawable.toolbar_bg)
            } else if (id==R.id.book){

                fragment= ServiceFragment(this)
                title ="Services"
                imgNotify.visibility = View.GONE

            } else if (id==R.id.nav_service_status){

              //  fragment= ServiceStatusFragment(this)
                fragment = BookedFragment(this)
                title ="Service Status"
                imgNotify.visibility = View.GONE
            } else if (id==R.id.nav_register_complaint){

                fragment= RegisterComplaintFragment(this)
                title ="Register Complaint"
                imgNotify.visibility = View.GONE

            } else if (id==R.id.nav_my_account){

                fragment= MyAccountFragment(this)
                title ="My Account"
                imgNotify.visibility = View.GONE

            } else if (id==R.id.nav_sos){
                val mydialog = SOSdialogue(this)
                mydialog.show()
            }else if (id==R.id.nav_about_us){
                fragment= AboutUsFragment(this)
                title ="About Us"
                imgNotify.visibility = View.GONE
            }

        }else if (login_response!!.data.user_type.equals("S"))
        {
            nav_Menu.findItem(R.id.book).isVisible = false
            nav_Menu.findItem(R.id.nav_service_status).isVisible = false
            nav_Menu.findItem(R.id.nav_register_complaint).isVisible = false
            nav_Menu.findItem(R.id.nav_sos).isVisible = false
            //nav_Menu.findItem(R.id.txt_addr).isVisible = false
            txt_addr!!.text=user_mobile
            val img = this.getResources().getDrawable(R.drawable.ic_mail_white)
            txt_addr!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

            if (id==R.id.nav_home){

                fragment= SelfServiceHomeFragment(this)
                title ="Home"
                imgNotify.visibility = View.VISIBLE
                //  toolbar.setBackgroundResource(R.drawable.toolbar_bg)
            } else if (id==R.id.bookedServices){

                fragment= BookedServiceFragment(this)
                title ="Booked Services"
                imgNotify.visibility = View.GONE

            } else if (id==R.id.rescheduledservices){

                fragment= RescheduledServiceFragment(this)
                title ="Rescheduled Services"
                imgNotify.visibility = View.GONE

            } else if (id==R.id.closedservices){

                fragment= ClosedServiceFragment(this)
                title ="Closed Services"
                imgNotify.visibility = View.GONE

            } else if (id==R.id.nav_my_account){

                fragment= MyAccountFragment(this)
                title ="My Account"
                imgNotify.visibility = View.GONE
            }
            else if (id==R.id.nav_about_us){
                fragment= AboutUsFragment(this)
                title ="About Us"
                imgNotify.visibility = View.GONE
            }
        }


        if (fragment!=null){
            val fragmentmanager = supportFragmentManager
            val fragmenttransaction = fragmentmanager.beginTransaction()
            fragmenttransaction.replace(R.id.container_body,fragment)
            fragmenttransaction.commit()

            supportActionBar!!.title = title
        }

    }

    private fun getRegIdInPref(): String? {
        val pref = applicationContext.getSharedPreferences(StatusConstant.SHARED_PREF, 0)
        return pref!!.getString(StatusConstant.FCM_TOKEN,"")
    }



//methods to call below fragments from dashboardfragment

    public fun clickBookservicebtn(){
        imgNotify.visibility = View.GONE
        title ="Services"
        val fragmenttransaction = fragmentmanager!!.beginTransaction()
        val sf : ServiceFragment = ServiceFragment(this)
        fragmenttransaction.replace(R.id.container_body, sf, "Service Fragment")
        fragmenttransaction.commit()
        supportActionBar!!.title = title
    }
    public fun clickServiceStatusbtn(){
        imgNotify.visibility = View.GONE
        title ="Service Status"
        val fragmenttransaction = fragmentmanager!!.beginTransaction()
       // val statusf : ServiceStatusFragment = ServiceStatusFragment(this)
        val statusf : BookedFragment = BookedFragment(this)
        fragmenttransaction.replace(R.id.container_body, statusf, "Booked Fragment")
        fragmenttransaction.commit()
        supportActionBar!!.title = title
    }

    public fun clickRegisterComplaintbtn() {
        imgNotify.visibility = View.GONE
            title = "Register Complaint"
            val fragmenttransaction = fragmentmanager!!.beginTransaction()
            val regf: RegisterComplaintFragment = RegisterComplaintFragment(this)
            fragmenttransaction.replace(R.id.container_body, regf, "RegisterComplaint Fragment")
            fragmenttransaction.commit()
            supportActionBar!!.title = title
        }

    public fun clickMyAccountbtn(){
        imgNotify.visibility = View.GONE
        title ="My Account"
        val fragmenttransaction = fragmentmanager!!.beginTransaction()
        val myacf : MyAccountFragment = MyAccountFragment(this)
        fragmenttransaction.replace(R.id.container_body, myacf, "MyAccount Fragment")
        fragmenttransaction.commit()
        supportActionBar!!.title = title
    }

    public fun clickDashboardfragment() {  //to access to another fragment
        imgNotify.visibility = View.VISIBLE
        title = "Home"
        val fragmenttransaction = fragmentmanager!!.beginTransaction()
        val dashboardf: DashboardFragment = DashboardFragment(this)
        fragmenttransaction.replace(R.id.container_body, dashboardf, "Dashboard Fragment")
        fragmenttransaction.commit()
        supportActionBar!!.title = title
    }


    public fun clickSserviceHomefragment() {
        imgNotify.visibility = View.VISIBLE
        title = "Home"
        val fragmenttransaction = fragmentmanager!!.beginTransaction()
        val sshf: SelfServiceHomeFragment = SelfServiceHomeFragment(this)
        fragmenttransaction.replace(R.id.container_body, sshf, "SelfServiceHome Fragment")
        fragmenttransaction.commit()
        supportActionBar!!.title = title
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {

            supportFragmentManager.popBackStackImmediate()

        } else {

            super.onBackPressed()

        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    // }

    private fun callLogoutdata(fcmtoken: String?) =
            if(connectioncheck!!.isNetworkAvailable()) {
                val additem = LogoutInput()
                additem.device_id = fcmtoken

                val loginsuccessRespeObservable = apiService!!.sendLogoutData("Bearer"+  login_response!!.data.token,additem)
                loginsuccessRespeObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<LogOutResponse> {
                            override fun onSubscribe(d: Disposable) {

                            }
                            override fun onNext(logOutResponse: LogOutResponse)
                            {
                                ResponseMessage = logOutResponse.message
                                pd!!.dismiss()
                                if (logOutResponse.message_code.equals("1")) {

                                    val logout = LoginResponse()
                                    Commonfunctions.set_login_data(this@DashboardNavigation,logout)
                                    val intent = Intent(applicationContext, Login::class.java)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    finish()
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                }
                            }
                            override fun onError(e: Throwable) {
                                pd!!.dismiss()
                                Commonfunctions.commonDialogue(this@DashboardNavigation, StatusConstant.ALERT, StatusConstant.USER_LOGIN_ERROR_MSG).show()
                                e.printStackTrace()
                            }
                            override fun onComplete() {

                            }
                        })
            }else{
                connectioncheck!!.getNetworkActiveAlert().show()
            }





}