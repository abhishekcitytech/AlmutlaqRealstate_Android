package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.adapter.StatusListAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.TenantServiceListResponse
import com.almutlaq.realstate.dto.Historymodel
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.StatusListModel
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_drawer.*

@SuppressLint("ValidFragment")
class BookedFragment @SuppressLint("ValidFragment") constructor
(act: Activity/*,tabtype: String*/) : Fragment() {

    val activity = act
    private var pd: Dialog? = null
   // var  tab  : String = tabtype  // //TABTYPE : BOOKED = 0 ,RESCHEDULED = 1 ,CLOSED = 3

    private var apiService: ApiInterface? = null
    //  private var _pref: Pref? = null
    var login_response: LoginResponse? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    private var rvStatus: RecyclerView? = null
    private var myAdapter: StatusListAdapter? = null
    private var arrayList: ArrayList<StatusListModel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    var tv_no_record : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater!!.inflate(R.layout.fragment_booked, container, false)
        init(rootview)
      //  loaddata()
        pd!!.show()
      /*  if (tab.equals("0")){
            getTServiceList("0")
        }else if (tab.equals("1")){
            getTServiceList("1")
        }else if (tab.equals("3")){
            getTServiceList("3")
        }*/
       // getTServiceList()
        return rootview
    }

    @SuppressLint("ResourceAsColor")
    override fun onPause() {
        super.onPause()
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.drawable.toolbar_bg)//R.drawable.toolbar_bg
        //  (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(resources.getColor("ffffff"))
        (getActivity() as DashboardNavigation). actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        }else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }

    }

    override fun onResume() {
        super.onResume()
        ///////// FOR BACKPRESS TO GO TO DASHBOARD FRAGMENT
        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.getAction() === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    (activity as DashboardNavigation).clickDashboardfragment()
                    true
                } else false
            }
        })

        ///////
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }

        getTServiceList()

    }

    private fun init(rootview: View?) {

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }


        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        // _pref = Pref(activity)
        login_response = Commonfunctions.get_login_data(activity)

        tv_no_record  = rootview!!.findViewById(R.id.tv_no_record)

        rvStatus = rootview!!.findViewById(R.id.rvStatus)
        arrayList = ArrayList<StatusListModel>()
        layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        myAdapter = StatusListAdapter(arrayList!!, this!!.activity!!, R.layout.servicestatus_row)

        rvStatus?.layoutManager = LinearLayoutManager(context)
        rvStatus?.adapter = myAdapter
    }

   /* private fun loaddata() {
        if (arrayList!!.size > 0){
            arrayList!!.clear()
        }

        arrayList!!.add(StatusListModel("AC Maintainance", "Maintaance required",
                "2018-10-03", "Deira,PO Box 594,Dubai,UAE,B12345,F1"))
        arrayList!!.add(StatusListModel("AC Maintainance", "Maintaance required",
                "2018-10-03", "Deira,PO Box 594,Dubai,UAE,B12345,F1"))
        arrayList!!.add(StatusListModel("AC Maintainance", "Maintaance required",
                "2018-10-03", "Deira,PO Box 594,Dubai,UAE,B12345,F1"))
        arrayList!!.add(StatusListModel("AC Maintainance", "Maintaance required",
                "2018-10-03", "Deira,PO Box 594,Dubai,UAE,B12345,F1"))
        arrayList!!.add(StatusListModel("AC Maintainance", "Maintaance required",
                "2018-10-03", "Deira,PO Box 594,Dubai,UAE,B12345,F1"))

        myAdapter!!.notifyDataSetChanged()
    }*/

    private fun getTServiceList(/*servicetype: String*/) {

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
                            pd!!.dismiss()
                            if (tenantServiceListResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (arrayList!!.size > 0) {
                                    arrayList!!.clear()
                                }
                                if (tenantServiceListResponse.data!!.size > 0) {

                                    for (i in 0..tenantServiceListResponse.data!!.size - 1) {
                                        val statusListModel = StatusListModel()
                                       // statusListModel.servicetype = servicetype
                                        statusListModel.servicetype = tenantServiceListResponse.data!!.get(i).booking_status
                                        statusListModel.service_boooking_id = tenantServiceListResponse.data!!.get(i).service_boooking_id
                                        statusListModel.serviceid = tenantServiceListResponse.data!!.get(i).service_id
                                        statusListModel.subservice_id = tenantServiceListResponse.data!!.get(i).subservice_id
                                        statusListModel.statustitle = tenantServiceListResponse.data!!.get(i).service_name
                                        statusListModel.statusrequest = tenantServiceListResponse.data!!.get(i).booking_notes
                                        statusListModel.statusdate = tenantServiceListResponse.data!!.get(i).booking_requested_date
                                        statusListModel.statusaddress = tenantServiceListResponse.data!!.get(i).building_address
                                        statusListModel.statusflatno = tenantServiceListResponse.data!!.get(i).flat_no
                                        statusListModel.statusbuildigno = tenantServiceListResponse.data!!.get(i).building_no
                                        statusListModel.bookingAcknowledge = tenantServiceListResponse.data!!.get(i).booking_acknowledge
                                        statusListModel.booking_accepted = tenantServiceListResponse.data!!.get(i).booking_accepted
                                        statusListModel.acknowledge_slot_date = tenantServiceListResponse.data!!.get(i).acknowledge_slot_date
                                        statusListModel.acknowledge_requested_date = tenantServiceListResponse.data!!.get(i).acknowledge_requested_date
                                        statusListModel.bookingImage = tenantServiceListResponse.data!!.get(i).booking_image
                                        statusListModel.tenant_slot_time = tenantServiceListResponse.data!!.get(i).tenant_slot_time
                                        statusListModel.tenant_slot_date = tenantServiceListResponse.data!!.get(i).tenant_slot_date
                                        statusListModel.csk_slot_time = tenantServiceListResponse.data!!.get(i).csk_slot_time
                                        statusListModel.booking_acknowledge_note = tenantServiceListResponse.data!!.get(i).booking_acknowledge_note
                                        statusListModel.tenant_rating = tenantServiceListResponse.data!!.get(i).tenant_rating.toFloat()
                                        statusListModel.tenant_rating_note = tenantServiceListResponse.data!!.get(i).tenant_rating_note
                                        statusListModel.tenant_rating_date = tenantServiceListResponse.data!!.get(i).tenant_rating_date
                                        statusListModel.booking_completion_percentage =  tenantServiceListResponse.data!!.get(i).booking_completion_percentage
                                        statusListModel.subservice = tenantServiceListResponse.data!!.get(i).subservice_name


                                      //  statusListModel.booking_update_note =  tenantServiceListResponse.data!!.get(i).booking_update_note
                                        ////// for history list
                                        if ( tenantServiceListResponse.data!!.get(i).booking_update_note.size > 0) {

                                            for (j in 0.. tenantServiceListResponse.data!!.get(i).booking_update_note.size - 1) {
                                                val historymodel = Historymodel()
                                                historymodel.servicenote = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).note
                                                historymodel.labourname = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).labour_name
                                                historymodel.booking_update_date = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).booking_update_date
                                                historymodel.job_completion = tenantServiceListResponse.data!!.get(i).booking_update_note.get(j).job_completion
                                                statusListModel.booking_update_note.add(historymodel)

                                            }

                                        }
                                        //////
                                        Log.e("historylistsize0", tenantServiceListResponse.data!!.get(i).booking_update_note.size.toString())

                                        arrayList!!.add(statusListModel)

                                    }
                                    rvStatus!!.visibility = View.VISIBLE
                                    tv_no_record!!.visibility = View.GONE
                                    myAdapter!!.notifyDataSetChanged()

                                }else{
                                    pd!!.dismiss()
                                    Log.e("servicelist_error", "error")
                                    rvStatus!!.visibility = View.GONE
                                    tv_no_record!!.visibility = View.VISIBLE
                                }

                            } else {
                                pd!!.dismiss()
                                Log.e("servicelist_error", "error")
                                rvStatus!!.visibility = View.GONE
                                tv_no_record!!.visibility = View.VISIBLE
                                //Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }
                        }

                        override fun onError(e: Throwable) {
                            pd!!.dismiss()
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