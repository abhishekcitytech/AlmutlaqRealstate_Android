package com.almutlaq.realstate.fragment.selfserviceuserFragments

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
import com.almutlaq.realstate.adapter.SelfClosedListAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.CustomerServiceListResponse
import com.almutlaq.realstate.dto.CustomerListModel
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_drawer.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ValidFragment")
class ClosedServiceFragment @SuppressLint("ValidFragment") constructor
(act: Activity): Fragment() {

    val activity = act
    private var apiService: ApiInterface? = null
    var login_response: LoginResponse? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    private var pd: Dialog? = null

    private var rvClosedservice: RecyclerView? = null
    private var myAdapter: SelfClosedListAdapter? = null
    private var arrayList: ArrayList<CustomerListModel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    var tv_c_no_record : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater!!.inflate(R.layout.closedservice_fragment, container, false)
        init(rootview)
        pd!!.show()
        getCustomerClosedServiceList()
        return rootview
    }

    private fun getCustomerClosedServiceList() {

        if(connectioncheck!!.isNetworkAvailable()) {

            val sdf = SimpleDateFormat("yyyy-M-dd")
            var currentDate = sdf.format(Date())

          //  var currentDate = "2018-11-12"
            val resschudelServiceResponseObservable = apiService!!.getCustomerServiceClosedList(currentDate,"Bearer "+ login_response!!.data.token)

            resschudelServiceResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CustomerServiceListResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }
                        override fun onNext(customerServiceListResponse: CustomerServiceListResponse) {
                            var ResponseMessage = customerServiceListResponse.message
                            pd!!.dismiss()

                            if (customerServiceListResponse.message_code.equals("1")) {
                                Log.e("customerService_success", "success")
                                if (arrayList!!.size > 0) {
                                    arrayList!!.clear()
                                }
                                if (customerServiceListResponse.data!!.size > 0) {

                                    for (i in 0..customerServiceListResponse.data!!.size - 1) {
                                        val customerListModel = CustomerListModel()

                                        customerListModel.service_boooking_id = customerServiceListResponse.data!!.get(i).service_boooking_id
                                        customerListModel.user_id = customerServiceListResponse.data!!.get(i).user_id
                                        customerListModel.booking_status = customerServiceListResponse.data!!.get(i).booking_status
                                        customerListModel.booking_notes = customerServiceListResponse.data!!.get(i).booking_notes
                                        customerListModel.tenant_slot_time = customerServiceListResponse.data!!.get(i).tenant_slot_time
                                        customerListModel.labour_name = customerServiceListResponse.data!!.get(i).labour_name
                                        customerListModel.booking_requested_date = customerServiceListResponse.data!!.get(i).booking_requested_date
                                        customerListModel.booking_requested_time = customerServiceListResponse.data!!.get(i).booking_requested_time
                                        customerListModel.booking_acknowledge_by = customerServiceListResponse.data!!.get(i).booking_acknowledge_by
                                        customerListModel.booking_acknowledge = customerServiceListResponse.data!!.get(i).booking_acknowledge
                                        customerListModel.booking_acknowledge_date = customerServiceListResponse.data!!.get(i).booking_acknowledge_date
                                        customerListModel.booking_acknowledge_time = customerServiceListResponse.data!!.get(i).booking_acknowledge_time
                                        customerListModel.csk_slot_time = customerServiceListResponse.data!!.get(i).csk_slot_time
                                        customerListModel.booking_acknowledge_note = customerServiceListResponse.data!!.get(i).booking_acknowledge_note
                                        customerListModel.tenant_rating = customerServiceListResponse.data!!.get(i).tenant_rating
                                        customerListModel.tenant_rating_note = customerServiceListResponse.data!!.get(i).tenant_rating_note
                                        customerListModel.tenant_rating_date = customerServiceListResponse.data!!.get(i).tenant_rating_date
                                        customerListModel.created_at = customerServiceListResponse.data!!.get(i).created_at
                                        customerListModel.tenant_name = customerServiceListResponse.data!!.get(i).tenant_name
                                        customerListModel.building_no = customerServiceListResponse.data!!.get(i).building_no
                                        customerListModel.building_address = customerServiceListResponse.data!!.get(i).building_address
                                        customerListModel.flat_no = customerServiceListResponse.data!!.get(i).flat_no
                                        customerListModel.service_name = customerServiceListResponse.data!!.get(i).service_name
                                        customerListModel.booking_image = customerServiceListResponse.data!!.get(i).booking_image
                                        customerListModel.booking_completion_percentage =  customerServiceListResponse.data!!.get(i).booking_completion_percentage

                                        arrayList!!.add(customerListModel)
                                    }
                                    rvClosedservice!!.visibility = View.VISIBLE
                                    tv_c_no_record!!.visibility = View.GONE
                                    myAdapter!!.notifyDataSetChanged()
                                }else{
                                    rvClosedservice!!.visibility = View.GONE
                                    tv_c_no_record!!.visibility = View.VISIBLE
                                }

                            }else {
                                Log.e("servicelist_error", "error")
                                rvClosedservice!!.visibility = View.GONE
                                tv_c_no_record!!.visibility = View.VISIBLE
                               // Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
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

    @SuppressLint("ResourceAsColor")
    override fun onPause() {
        super.onPause()
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
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
        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    (activity as DashboardNavigation).clickSserviceHomefragment()

                    true
                } else false
            }
        })

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun init(rootview: View?) {

        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        login_response = Commonfunctions.get_login_data(activity)

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
        }
        tv_c_no_record  = rootview!!.findViewById(R.id.tv_c_no_record)
        rvClosedservice = rootview!!.findViewById(R.id.rvClosedservice)
        arrayList = ArrayList<CustomerListModel>()
        layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        myAdapter = SelfClosedListAdapter(arrayList!!, this!!.activity!!, R.layout.customerservicestatus_row)

        rvClosedservice?.layoutManager = LinearLayoutManager(context)
        rvClosedservice?.adapter = myAdapter
    }


}