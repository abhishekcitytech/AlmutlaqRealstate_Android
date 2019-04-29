package com.almutlaq.realstate.activity

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.almutlaq.realstate.R
import com.almutlaq.realstate.adapter.DialougePercentageAdapter
import com.almutlaq.realstate.adapter.ServiceHistoryAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.CSpercentageResponse
import com.almutlaq.realstate.base.TenantServiceListResponse
import com.almutlaq.realstate.dto.*
import com.almutlaq.realstate.utility.*
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_servicedetails.*
import kotlinx.android.synthetic.main.toolbar_for_activity.*

class ServiceDetails : AppCompatActivity() {

    var login_response: LoginResponse? = null
    private var pd: Dialog? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    var stoolbar: RelativeLayout? = null

    var bookingid: String? = null
    var serviceid: String? = null
    var subservice_id: String? = null
    var servicetype: String? = null
    var servicetitle: String? = null
    var servicedate: String? = null
    var serviceimg: String? = null
    var tenant_time: String? = null
    var tenant_date: String? = null
    var csk_time: String? = null
    var acknowledged: String? = null
    var booking_accepted: String? = null
    var ack_slot_date: String? = null
    var ack_req_date: String? = null
    var book_note: String? = null
    var flatno: String? = null
    var buildingno: String? = null
    var buildingaddress: String? = null
    var bookingpercent: String? = null
    var statusrequest: String? = null
    var subservice: String? = null


    private var img_service: SimpleDraweeView? = null

    var imgAck: ImageView? = null

    private var detailsList: ArrayList<StatusListModel>? = null
    private var rvTStatusPercent: RecyclerView? = null
    private var myAdapter: DialougePercentageAdapter? = null
    private var arrayList: ArrayList<SdialoguePercentModel>? = null
    private var percentlayoutManager: RecyclerView.LayoutManager? = null


    var layoutBottomSheet: LinearLayout? = null
    var sheetBehavior: BottomSheetBehavior<*>? = null
    var btn_dialogue_close: ImageView? = null

    private var rvHistorylist: RecyclerView? = null
    private var mybottomAdapter: ServiceHistoryAdapter? = null
    private var arrayListbottomsheet: ArrayList<ServiceHistorymodel>? = null
    private var arrayListintenthistory: ArrayList<Historymodel>? = null
    private var bottomlayoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.tenant_service_details_main)

        init()
        //getpercentlist()
        pd!!.show()
        getTServiceList()
        performclick()
    }

    private fun init() {

        login_response = Commonfunctions.get_login_data(this)
        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        connectioncheck = NetworkConnetionCheck(this)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)

        stoolbar = findViewById(R.id.stoolbar)
        toolbartitle.setText("Service Details")
        imgToolback.setOnClickListener { onBackPressed() }
        imgAck = findViewById(R.id.imgAck)
        img_service = findViewById(R.id.img_service)

        detailsList = ArrayList<StatusListModel>()

        rvTStatusPercent = findViewById(R.id.rvT_StatusPercent)
        arrayList = ArrayList<SdialoguePercentModel>()
        percentlayoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false) as RecyclerView.LayoutManager?

        layoutBottomSheet = findViewById(R.id.servicenote_history_bottomdialogue)
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet)
        btn_dialogue_close = findViewById(R.id.btn_dialogue_close)

        rvHistorylist = findViewById(R.id.rvHistorylist)
        arrayListbottomsheet = ArrayList<ServiceHistorymodel>()
        bottomlayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        mybottomAdapter = ServiceHistoryAdapter(arrayListbottomsheet!!, this, R.layout.service_history_row)
        rvHistorylist?.layoutManager = LinearLayoutManager(this)
        rvHistorylist?.adapter = mybottomAdapter

        //getintent()

        if (!intent.extras.equals("")) {

            bookingid = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_BOOKINGID)
        }

    }

    private fun getintent() {
        if (!intent.extras.equals("")) {

            bookingid  =  intent.getStringExtra(StatusConstant.INTENT_TSERVICE_BOOKINGID)
            serviceid  =  intent.getStringExtra(StatusConstant.INTENT_TSERVICE_SERVICEID)
            subservice_id  =  intent.getStringExtra(StatusConstant.INTENT_TSERVICE_SUBSERVICE_ID)
            servicetype =  intent.getStringExtra(StatusConstant.INTENT_TSERVICE_TYPE)
            servicetitle = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_NAME)
            servicedate = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_DATE)
            serviceimg = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_IMG)
            tenant_time = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_TENANTTIME)
            tenant_date = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_TENANTDATE)
            csk_time = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_CSKTIME)
            acknowledged = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_ACK)
            booking_accepted = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_BOOK_ACCEPT)
            ack_slot_date = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_ACK_SLOT_DATE)
            ack_req_date = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_ACK_REQ_DATE)
            book_note = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_BOOKNOTE)
            flatno = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_FLATNO)
            buildingno = intent.getStringExtra(StatusConstant.INTENT_TSERVICE_BUILDINGNO)
            buildingaddress =  intent.getStringExtra(StatusConstant.INTENT_TSERVICE_BUILDINGADDRESS)
            bookingpercent = intent.getStringExtra(StatusConstant.INTENT_T_BOOKING_PERCENT)
            statusrequest = intent.getStringExtra(StatusConstant.INTENT_T_STATUS_REQUEST)
            subservice = intent.getStringExtra(StatusConstant.INTENT_SUBSERVICE)


            arrayListintenthistory = intent.getSerializableExtra(StatusConstant.INTENT_CS_HISTORY_LIST) as ArrayList<Historymodel>

            Log.e("historylistsize2",arrayListintenthistory!!.size.toString())

            if (arrayListbottomsheet!!.size > 0) {
                arrayListbottomsheet!!.clear()
            }
            if (arrayListintenthistory!!.size > 0) {

                for (j in 0..arrayListintenthistory!!.size - 1) {
                    val serviceHistorymodel = ServiceHistorymodel()
                    serviceHistorymodel.servicenote = arrayListintenthistory!!.get(j).servicenote.toString()
                    serviceHistorymodel.labourname = arrayListintenthistory!!.get(j).labourname.toString()
                    serviceHistorymodel.booking_update_date = arrayListintenthistory!!.get(j).booking_update_date.toString()
                    serviceHistorymodel.job_completion = arrayListintenthistory!!.get(j).job_completion.toString()

                    arrayListbottomsheet!!.add(serviceHistorymodel)

                }

                mybottomAdapter !!.notifyDataSetChanged()

            }

            if (servicetype.equals("0")) ///booked
            {
                imgTooledit.visibility = View.GONE
                llpercent!!.visibility = View.GONE
                if (acknowledged.equals("Y")){
                    imgAck!!.visibility = View.VISIBLE
                    tv_reporting_date!!.visibility = View.VISIBLE

                    if (booking_accepted.equals("N")){
                        val mydialog = TenantBookingAccepteddialogue(this,ack_slot_date,csk_time,bookingid,serviceid,subservice_id)
                        mydialog.show()
                    }
                }else{
                    tv_reporting_date!!.visibility = View.GONE
                    tv_report_time!!.visibility = View.GONE
                }

            }
            else if ((servicetype.equals("4"))){ //cancelled
                imgTooledit.visibility = View.GONE
                llpercent!!.visibility = View.GONE
            }
            else{ ///rescheduled and closed
                imgTooledit.visibility = View.VISIBLE
                llpercent!!.visibility = View.VISIBLE
                tv_reporting_date!!.visibility = View.VISIBLE
            }

        }

        tv_service_title.text = servicetitle.toString()
        tv_subservice.text =  subservice.toString()
        // tv_service_date.text = "Job posting date: " +  servicedate.toString()
        tv_service_date.text = "Requested Date : " + tenant_date.toString()
        if (ack_req_date.equals("")){
            tv_reporting_date.text = "Reporting Date : " +  ack_slot_date.toString()
        } else{
            tv_reporting_date.text = "Reporting Date : " +  ack_req_date.toString()
        }
        tv_Reqest_time.text =  "Requested Time \n" + tenant_time.toString()
        tv_report_time.text = "Reporting Time \n" + csk_time.toString()
        tv_building_no.text = "Building no : " + flatno.toString() + ", " + buildingno.toString()
        tv_service_addr.text = buildingaddress.toString()
        //tv_service_feedback.text = book_note.toString()
        tv_service_feedback.text = statusrequest.toString()//change on 20/11/2018

        if (csk_time.toString().equals("")){
            tv_report_time.visibility = View.GONE
        }

        if (tenant_time.equals("")){
            tv_Reqest_time.visibility = View.GONE
        }

        /*  if ( bookingpercent.equals("0")) {   // TODO : CONFIRM
              llpercent!!.visibility = View.GONE
          }*/

        val controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(serviceimg))
                .setTapToRetryEnabled(true)
                .build()
        img_service!!.controller = controller

    }


    private fun performclick() {
        imgTooledit.setOnClickListener {
            toggleBottomSheet()
        }

        btn_dialogue_close!!.setOnClickListener(View.OnClickListener { toggleBottomSheet() })

        img_service!!.setOnClickListener {
            val mydialog = ImgZoomdialogue(this,serviceimg)
            mydialog.show()
        }
    }

    private fun getpercentlist() {
        if(connectioncheck!!.isNetworkAvailable()) {
            // progress_history.visibility = View.VISIBLE
           // pd!!.show()
            val servicesuccessRespeObservable = apiService!!.getCSpercentage( "Bearer"+  login_response!!.data.token)

            servicesuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSpercentageResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(cSpercentageResponse: CSpercentageResponse) {
                            //  Log.d("resp", "onNext: get dtaa response " + orderListResponse.ResponseMessage)
                            var ResponseMessage = cSpercentageResponse.message


                            if (cSpercentageResponse.message_code.equals("1")) {
                                Log.e("servicelist_success", "success")

                                if (arrayList!!.size > 0) {
                                    arrayList!!.clear()
                                }
                                if (!cSpercentageResponse.data!!.equals("")) {

                                    var data = cSpercentageResponse.data
                                    val items : List<String> = data.split(",")
                                    for (item : String in items) {

                                        val spmodel = SdialoguePercentModel()
                                        spmodel.statuspercentage = item
                                        arrayList!!.add(spmodel)
                                    }
                                    myAdapter = DialougePercentageAdapter(arrayList!!, this@ServiceDetails, bookingpercent, null, null)
                                    rvTStatusPercent!!.setHasFixedSize(true)
                                    rvTStatusPercent?.layoutManager = percentlayoutManager
                                    rvTStatusPercent?.adapter = myAdapter
                                    myAdapter!!.notifyDataSetChanged()

                                }

                            } else {
                                Log.e("servicelist_error", "error")


                                Commonfunctions.commonDialogue(this@ServiceDetails, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }

                            pd!!.dismiss()
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            pd!!.dismiss()
                        }

                        override fun onComplete() {

                        }
                    })

        }else{
            connectioncheck!!.getNetworkActiveAlert().show()
        }

    }

    private fun getTServiceList() {   /// API called for push notification

        if(connectioncheck!!.isNetworkAvailable()) {
            // progress_history.visibility = View.VISIBLE

            val tservicelistsuccessRespeObservable = apiService!!.getTenantServiceList(/*servicetype,*/"Bearer"+ login_response!!.data.token)

            tservicelistsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<TenantServiceListResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(tenantServiceListResponse: TenantServiceListResponse) {
                            //  progress_dashboard.visibility = View.GONE
                            //  Log.d("resp", "onNext: get tenantbook response " + orderListResponse.ResponseMessage)
                            var ResponseMessage = tenantServiceListResponse.message

                            if (tenantServiceListResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (arrayListbottomsheet!!.size > 0) {
                                    arrayListbottomsheet!!.clear()
                                }
                                if (tenantServiceListResponse.data!!.size > 0) {

                                    for (i in 0..tenantServiceListResponse.data!!.size - 1) {

                                        var bookid = tenantServiceListResponse.data!!.get(i).service_boooking_id
                                        val bid: Int = bookingid!!.toInt()
                                        if (bookid == bid) {

                                            servicetype = tenantServiceListResponse.data!!.get(i).booking_status
                                            serviceid = tenantServiceListResponse.data!!.get(i).service_id.toString()
                                            subservice_id = tenantServiceListResponse.data!!.get(i).subservice_id.toString()
                                            servicetitle = tenantServiceListResponse.data!!.get(i).service_name
                                            statusrequest= tenantServiceListResponse.data!!.get(i).booking_notes
                                            servicedate = tenantServiceListResponse.data!!.get(i).booking_requested_date
                                            buildingaddress = tenantServiceListResponse.data!!.get(i).building_address
                                            flatno = tenantServiceListResponse.data!!.get(i).flat_no
                                            buildingno = tenantServiceListResponse.data!!.get(i).building_no
                                            acknowledged = tenantServiceListResponse.data!!.get(i).booking_acknowledge
                                            booking_accepted = tenantServiceListResponse.data!!.get(i).booking_accepted
                                            ack_slot_date = tenantServiceListResponse.data!!.get(i).acknowledge_slot_date
                                            ack_req_date = tenantServiceListResponse.data!!.get(i).acknowledge_requested_date
                                            serviceimg = tenantServiceListResponse.data!!.get(i).booking_image
                                            tenant_time = tenantServiceListResponse.data!!.get(i).tenant_slot_time
                                            tenant_date = tenantServiceListResponse.data!!.get(i).tenant_slot_date
                                            csk_time = tenantServiceListResponse.data!!.get(i).csk_slot_time
                                            book_note = tenantServiceListResponse.data!!.get(i).booking_acknowledge_note
                                        //statusListModel.tenant_rating = tenantServiceListResponse.data!!.get(i).tenant_rating.toFloat()
                                       // statusListModel.tenant_rating_note = tenantServiceListResponse.data!!.get(i).tenant_rating_note
                                       // statusListModel.tenant_rating_date = tenantServiceListResponse.data!!.get(i).tenant_rating_date
                                            bookingpercent = tenantServiceListResponse.data!!.get(i).booking_completion_percentage
                                            subservice = tenantServiceListResponse.data!!.get(i).subservice_name

                                        //  statusListModel.booking_update_note =  tenantServiceListResponse.data!!.get(i).booking_update_note
                                        ////// for history list
                                        if (tenantServiceListResponse.data!!.get(i).booking_update_note.size > 0) {
                                            val serviceHistorymodel = ServiceHistorymodel()
                                            for (j in 0..tenantServiceListResponse.data!!.get(i).booking_update_note.size - 1) {

                                                serviceHistorymodel.servicenote = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).note
                                                serviceHistorymodel.labourname = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).labour_name
                                                serviceHistorymodel.booking_update_date = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).booking_update_date
                                                serviceHistorymodel.job_completion = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).job_completion


                                            }
                                            arrayListbottomsheet!!.add(serviceHistorymodel)
                                        }
                                        //////

                                            mybottomAdapter !!.notifyDataSetChanged()
                                        Log.e("historylistsize0", tenantServiceListResponse.data!!.get(i).booking_update_note.size.toString())


                                            //////
                                            if (servicetype.equals("0")) ///booked
                                            {
                                                imgTooledit.visibility = View.GONE
                                                llpercent!!.visibility = View.GONE
                                                if (acknowledged.equals("Y")){
                                                    imgAck!!.visibility = View.VISIBLE
                                                    tv_reporting_date!!.visibility = View.VISIBLE

                                                    if (booking_accepted.equals("N")){
                                                        val mydialog = TenantBookingAccepteddialogue(this@ServiceDetails,ack_slot_date,csk_time,bookingid,serviceid,subservice_id)
                                                        mydialog.show()
                                                    }
                                                }else{
                                                    tv_reporting_date!!.visibility = View.GONE
                                                    tv_report_time!!.visibility = View.GONE
                                                }

                                            }
                                            else if ((servicetype.equals("4"))){ //cancelled
                                                imgTooledit.visibility = View.GONE
                                                llpercent!!.visibility = View.GONE
                                            }
                                            else{ ///rescheduled and closed
                                                imgTooledit.visibility = View.VISIBLE
                                                llpercent!!.visibility = View.VISIBLE
                                                tv_reporting_date!!.visibility = View.VISIBLE
                                            }

                                            //////
                                            tv_service_title.text = servicetitle.toString()
                                            tv_subservice.text =  subservice.toString()
                                            // tv_service_date.text = "Job posting date: " +  servicedate.toString()
                                            tv_service_date.text = "Requested Date : " + tenant_date.toString()
                                            if (ack_req_date.equals("")){
                                                tv_reporting_date.text = "Reporting Date : " +  ack_slot_date.toString()
                                            } else{
                                                tv_reporting_date.text = "Reporting Date : " +  ack_req_date.toString()
                                            }
                                            tv_Reqest_time.text =  "Requested Time \n" + tenant_time.toString()
                                            tv_report_time.text = "Reporting Time \n" + csk_time.toString()
                                            tv_building_no.text = "Building no : " + flatno.toString() + ", " + buildingno.toString()
                                            tv_service_addr.text = buildingaddress.toString()
                                            //tv_service_feedback.text = book_note.toString()
                                            tv_service_feedback.text = statusrequest.toString()//change on 20/11/2018

                                            if (csk_time.toString().equals("")){
                                                tv_report_time.visibility = View.GONE
                                            }

                                            if (tenant_time.equals("")){
                                                tv_Reqest_time.visibility = View.GONE
                                            }

                                            /*  if ( bookingpercent.equals("0")) {   // TODO : CONFIRM
                                                  llpercent!!.visibility = View.GONE
                                              }*/

                                            val controller = Fresco.newDraweeControllerBuilder()
                                                    .setUri(Uri.parse(serviceimg))
                                                    .setTapToRetryEnabled(true)
                                                    .build()
                                            img_service!!.controller = controller
/////////////////////////////////////////////////////////////////////////////
                                    }
                                }

                                }else{

                                    Log.e("servicelist_error", "error")
                                }

                            } else {

                                Log.e("servicelist_error", "error")
                                //Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }
                            getpercentlist()
                        }

                        override fun onError(e: Throwable) {
                            pd!!.dismiss()
                            e.printStackTrace()
                            getpercentlist()
                        }

                        override fun onComplete() {

                        }
                    })
        }else{
            connectioncheck!!.getNetworkActiveAlert().show()
        }

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

}