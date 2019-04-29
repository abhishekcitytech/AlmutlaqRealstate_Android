package com.almutlaq.realstate.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.almutlaq.realstate.R
import com.almutlaq.realstate.adapter.ServiceHistoryAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.CSserviceDetailsResponse
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.ServiceHistorymodel
import com.almutlaq.realstate.utility.*
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_selfuser_servicedetails.*
import kotlinx.android.synthetic.main.toolbar_for_activity.*
import java.util.*

class SelfsuserServicesDetails : AppCompatActivity() {

    var stoolbar : RelativeLayout? = null
    var  bookingid  : Int? = null

    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    private var pd: Dialog? = null

    var login_response: LoginResponse? = null

    var servicename : String? = null

    private var img_srqPic : SimpleDraweeView? = null
    var  detailsimg  : String? = null
    var  booking_req_date : String? = null
    var booking_req_time : String? = null
    var  tenant_slot_time : String? = null
    var  tenant_slot_date : String? = null
    var  labour_name : String? = null
    var  booking_completion_percentage : String? = null
    var updated_percent_note : String? = null
    var  acknowledge_slot_date : String? = null
    var  acknowledge_requested_date : String? = null

    var layoutBottomSheet: LinearLayout? = null
    var sheetBehavior: BottomSheetBehavior<*>? = null
    var btn_dialogue_close: ImageView? = null

    private var rvHistorylist: RecyclerView? = null
    private var myAdapter: ServiceHistoryAdapter? = null
    private var arrayList: ArrayList<ServiceHistorymodel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_self_servicedetails_main)
        init()
        getServiceDetails()
        //getCustomerClosedServiceList()
        performclick()
    }

    private fun performclick() {

        btn_acknowledge.setOnClickListener {
            Log.e("bid1",bookingid.toString())
            val mydialog = Acknowledgementdialogue(this,booking_req_date,booking_req_time,tenant_slot_time,tenant_slot_date,bookingid)
            mydialog.show()
        }

        btn_update_sstatus.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.WRITE_CALENDAR), 0)

            }else {
                val ssdialog = ServiceStatusdialogue(this, booking_req_date, booking_req_time, labour_name, bookingid, booking_completion_percentage, updated_percent_note,true)
                ssdialog.show()
            }
        }

        imgTooledit.setOnClickListener {
            toggleBottomSheet()
        }

        img_srqPic!!.setOnClickListener {
            val mydialog = ImgZoomdialogue(this,detailsimg)
            mydialog.show()
        }

        sheetBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // btnBottomSheet.setText("Close Sheet");
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // btnBottomSheet.setText("Expand Sheet");
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        btn_dialogue_close!!.setOnClickListener(View.OnClickListener { toggleBottomSheet() })
    }

    fun toggleBottomSheet() {   ///for handling open and close behaviour of  bottomsheet dialogue
        if (sheetBehavior!!.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
            // btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
            //  btnBottomSheet.setText("Expand sheet");
        }
    }

    private fun init() {

        if (!intent.extras.equals("")) {
            bookingid = intent.getIntExtra(StatusConstant.INTENT_CS_BOOKING_ID,0)
        }

        connectioncheck =  NetworkConnetionCheck(this)
        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        login_response = Commonfunctions.get_login_data(this)

        img_srqPic = findViewById(R.id.img_srqPic)


        layoutBottomSheet = findViewById(R.id.servicenote_history_bottomdialogue)
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet)
        btn_dialogue_close = findViewById(R.id.btn_dialogue_close)


        rvHistorylist = findViewById(R.id.rvHistorylist)
        arrayList = ArrayList<ServiceHistorymodel>()
        layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        myAdapter = ServiceHistoryAdapter(arrayList!!, this, R.layout.service_history_row)

        rvHistorylist?.layoutManager = LinearLayoutManager(this)
        rvHistorylist?.adapter = myAdapter

    }

    private fun toolbarview() {
        stoolbar = findViewById(R.id.stoolbar)
        imgTooledit.visibility = View.VISIBLE
        toolbartitle.setText(servicename.toString())
        imgToolback.setOnClickListener { onBackPressed() }
    }

    private fun getServiceDetails() {
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()
            val detailssuccessRespeObservable = apiService!!.getCSserviceDetails(this!!.bookingid!!,"Bearer"+ login_response!!.data.token)

            detailssuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSserviceDetailsResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(cSserviceDetailsResponse: CSserviceDetailsResponse) {

                            var ResponseMessage = cSserviceDetailsResponse.message

                            if (cSserviceDetailsResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (cSserviceDetailsResponse.data!!.size > 0) {

                                    for (i in 0..cSserviceDetailsResponse.data!!.size - 1) {

                                        servicename = cSserviceDetailsResponse.data!!.get(i).service_name
                                        tv_sreq_title.setText("Tenant : " + cSserviceDetailsResponse.data!!.get(i).tenant_name)
                                        tv_subtype_title.setText(cSserviceDetailsResponse.data!!.get(i).subservice_name)
                                        tv_srq_date.setText(cSserviceDetailsResponse.data!!.get(i).booking_requested_date)
                                        tv_srq_building.setText(cSserviceDetailsResponse.data!!.get(i).flat_no + ", " + cSserviceDetailsResponse.data!!.get(i).building_no)
                                        tv_service_addr.setText(cSserviceDetailsResponse.data!!.get(i).building_address)

                                        tenant_slot_time = cSserviceDetailsResponse.data!!.get(i).tenant_slot_time
                                        tenant_slot_date = cSserviceDetailsResponse.data!!.get(i).tenant_slot_date
                                        booking_req_date = cSserviceDetailsResponse.data!!.get(i).booking_requested_date
                                        booking_req_time = cSserviceDetailsResponse.data!!.get(i).booking_requested_time
                                        labour_name = cSserviceDetailsResponse.data!!.get(i).labour_name
                                        booking_completion_percentage = cSserviceDetailsResponse.data!!.get(i).booking_completion_percentage
                                        acknowledge_slot_date = cSserviceDetailsResponse.data!!.get(i).acknowledge_slot_date
                                        acknowledge_requested_date = cSserviceDetailsResponse.data!!.get(i).acknowledge_requested_date

                                        tv_cs_service_date.setText( "Requested date: " + tenant_slot_date)
                                        if (acknowledge_requested_date.equals("")){
                                            tv_cs_reporting_date.setText("Reporting date: " +  acknowledge_slot_date.toString())
                                        } else{
                                            tv_cs_reporting_date.setText("Reporting date: " +  acknowledge_requested_date.toString())
                                        }

                                        /////////////////////////////////////////////////////// for service history list
                                        if (arrayList!!.size > 0) {
                                            arrayList!!.clear()
                                        }
                                        if (cSserviceDetailsResponse.data!!.get(i).booking_update_note.size > 0) {

                                            for (j in 0..cSserviceDetailsResponse.data!!.get(i).booking_update_note.size - 1) {
                                                val serviceHistorymodel = ServiceHistorymodel()
                                                serviceHistorymodel.servicenote = cSserviceDetailsResponse.data!!.get(i).booking_update_note.get(j).note
                                                updated_percent_note = cSserviceDetailsResponse.data!!.get(i).booking_update_note.get(0).note
                                                serviceHistorymodel.labourname = cSserviceDetailsResponse.data!!.get(i).booking_update_note.get(j).labour_name
                                                serviceHistorymodel.booking_update_date = cSserviceDetailsResponse.data!!.get(i).booking_update_note.get(j).booking_update_date
                                                serviceHistorymodel.job_completion = cSserviceDetailsResponse.data!!.get(i).booking_update_note.get(j).job_completion
                                                arrayList!!.add(serviceHistorymodel)

                                            }
                                            // progress_history.visibility = View.GONE
                                            //rvPendingList.visibility = View.VISIBLE
                                            myAdapter!!.notifyDataSetChanged()

                                        }
                                     ///////////////////////////////////////////

                                        cs_details_reqtime.setText("Requested Time \n " + tenant_slot_time)
                                        if (cSserviceDetailsResponse.data!!.get(i).csk_slot_time.equals("") || cSserviceDetailsResponse.data!!.get(i).acknowledge_requested_time.equals("")){
                                            cs_details_resptime.visibility = View.GONE
                                        }

                                        if(cSserviceDetailsResponse.data!!.get(i).acknowledge_requested_time.equals("") || cSserviceDetailsResponse.data!!.get(i).acknowledge_requested_time.equals("Reschedule time") ){
                                            cs_details_resptime.setText("Reporting Time \n " + cSserviceDetailsResponse.data!!.get(i).csk_slot_time)
                                        }else{
                                            cs_details_resptime.setText("Reporting Time \n " + cSserviceDetailsResponse.data!!.get(i).acknowledge_requested_time)
                                        }

                                           et_srq_note.setText(cSserviceDetailsResponse.data!!.get(i).booking_notes)

                                        toolbarview()
                                        detailsimg = cSserviceDetailsResponse.data!!.get(i).booking_image
                                        val controller = Fresco.newDraweeControllerBuilder()
                                                .setUri(Uri.parse(detailsimg))
                                                .setTapToRetryEnabled(true)
                                                .build()
                                        img_srqPic!!.setController(controller)

                                        if (cSserviceDetailsResponse.data!!.get(i).booking_acknowledge.equals("Y")){
                                            btn_update_sstatus.visibility = View.VISIBLE
                                            btn_acknowledge.visibility = View.GONE
                                        }

                                        var b_status = cSserviceDetailsResponse.data!!.get(i).booking_status
                                        var b_acknowledge = cSserviceDetailsResponse.data!!.get(i).booking_acknowledge
                                        var booking_accepted  =  cSserviceDetailsResponse.data!!.get(i).booking_accepted

                                        if (b_status == 0)
                                        {
                                            //Booked
                                            imgTooledit.visibility = View.GONE
                                            btn_status_cancel.visibility = View.GONE
                                            if (b_acknowledge == "N")
                                            {
                                                btn_acknowledge.visibility = View.VISIBLE
                                                btn_acknowledge.text = "Acknowledge"
                                                btn_acknowledge.isEnabled = true
                                                btn_update_sstatus.visibility = View.GONE
                                                btn_close.visibility= View.GONE
                                                tv_cs_reporting_date.visibility= View.GONE
                                                cs_details_resptime.visibility = View.GONE

                                            } else {
                                                tv_cs_reporting_date.visibility= View.VISIBLE
                                                cs_details_resptime.visibility = View.VISIBLE
                                                if (booking_accepted == "Y"){
                                                    btn_acknowledge.visibility = View.GONE
                                                    btn_update_sstatus.visibility = View.VISIBLE
                                                }else{
                                                    btn_acknowledge.visibility = View.VISIBLE
                                                    btn_update_sstatus.visibility = View.GONE
                                                    btn_acknowledge.isEnabled = false
                                                    btn_acknowledge.text = "Tenant approval pending"
                                                }
                                                btn_close.visibility= View.GONE

                                            }

                                        }
                                        else if (b_status == 1)
                                        {
                                            //Rescheduled
                                            imgTooledit.visibility = View.VISIBLE
                                            btn_status_cancel.visibility = View.GONE
                                            if (b_acknowledge == "N")
                                            {
                                                btn_acknowledge.visibility = View.VISIBLE
                                                btn_acknowledge.text = "Acknowledge"
                                                btn_acknowledge.isEnabled = true
                                                btn_update_sstatus.visibility = View.GONE
                                                btn_close.visibility= View.GONE
                                                tv_cs_reporting_date.visibility= View.GONE
                                                cs_details_resptime.visibility = View.GONE
                                            }
                                            else
                                            {
                                                tv_cs_reporting_date.visibility= View.VISIBLE
                                                cs_details_resptime.visibility = View.VISIBLE
                                                if (booking_accepted == "Y"){
                                                    btn_acknowledge.visibility = View.GONE
                                                    btn_update_sstatus.visibility = View.VISIBLE
                                                }else{
                                                    btn_acknowledge.visibility = View.VISIBLE
                                                    btn_update_sstatus.visibility = View.GONE
                                                    btn_acknowledge.isEnabled = false
                                                    btn_acknowledge.text = "Tenant approval pending"
                                                }
                                                btn_close.visibility= View.GONE
                                            }
                                        }
                                        else if (b_status == 3)
                                        {
                                            //Closed
                                            imgTooledit.visibility = View.VISIBLE
                                            btn_acknowledge.visibility = View.GONE
                                            btn_update_sstatus.visibility = View.GONE
                                            btn_status_cancel.visibility = View.GONE
                                            btn_close.visibility= View.VISIBLE
                                            tv_cs_reporting_date.visibility= View.VISIBLE
                                        }
                                       else if (b_status == 4){
                                            imgTooledit.visibility = View.GONE
                                            btn_acknowledge.visibility = View.GONE
                                            btn_update_sstatus.visibility = View.GONE
                                            btn_close.visibility = View.GONE
                                            btn_status_cancel.visibility = View.VISIBLE
                                            tv_cs_reporting_date.visibility= View.VISIBLE
                                        }

                                    }

                                }

                            } else {
                                Log.e("servicelist_error", "error")
                                Commonfunctions.commonDialogue(this@SelfsuserServicesDetails, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }

                            pd!!.dismiss()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
          /*  if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val ssdialog = ServiceStatusdialogue(this, booking_req_date, booking_req_time, labour_name, bookingid, booking_completion_percentage, updated_percent_note,true)
                ssdialog.show()
            }else{
                Log.e("not granted","not granted")
                val ssdialog = ServiceStatusdialogue(this, booking_req_date, booking_req_time, labour_name, bookingid, booking_completion_percentage, updated_percent_note,false)
                ssdialog.show()
            }*/

            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

               // Log.i(TAG, "Permission has been denied by user")
                Log.e("not granted","not granted")
                val ssdialog = ServiceStatusdialogue(this, booking_req_date, booking_req_time, labour_name, bookingid, booking_completion_percentage, updated_percent_note,false)
                ssdialog.show()

            } else {
              //  Log.i(TAG, "Permission has been granted by user")
                val ssdialog = ServiceStatusdialogue(this, booking_req_date, booking_req_time, labour_name, bookingid, booking_completion_percentage, updated_percent_note,true)
                ssdialog.show()
            }
        }
    }

}