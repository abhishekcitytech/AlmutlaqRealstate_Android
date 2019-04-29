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
import com.almutlaq.realstate.adapter.ServicetypeAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.ServiceListResponse
import com.almutlaq.realstate.dto.Servicetypemodel
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_drawer.*

@SuppressLint("ValidFragment")
class ServiceFragment @SuppressLint("ValidFragment") constructor
(act: Activity) : Fragment() {//, IOnBackPressed

    val activity = act
   // val activity = act as ServiceFragment
    var login_response: LoginResponse? = null
    private var apiService: ApiInterface? = null
   // private var _pref: Pref? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    private var rv_service: RecyclerView? = null
    private var myAdapter: ServicetypeAdapter? = null
    private var arrayList: ArrayList<Servicetypemodel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var pd: Dialog? = null
    var tv_s_no_record : TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater!!.inflate(R.layout.activity_service_list, container, false)
        init(rootview)
        //loaddata()
        pd!!.show()
        getServicelist()
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

        /////////
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
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.white)
        (getActivity() as DashboardNavigation). actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.black, getActivity()!!.getTheme()))
        }else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.black))
        }

    }


    @SuppressLint("ResourceAsColor")
    private fun init(rootview: View?) {

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.white)
        (getActivity() as DashboardNavigation). actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.black, getActivity()!!.getTheme()))
        }else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.black))
        }
        //(getActivity() as DashboardNavigation).toolbar.setNavigationIcon(R.color.white)
        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        login_response = Commonfunctions.get_login_data(activity)

        tv_s_no_record  = rootview!!.findViewById(R.id.tv_s_no_record)

        rv_service = rootview!!.findViewById(R.id.rv_service)
        arrayList = ArrayList<Servicetypemodel>()
        layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        myAdapter = ServicetypeAdapter(arrayList!!, this!!.activity!!, R.layout.servicelist_row)

        rv_service?.layoutManager = LinearLayoutManager(context)
        rv_service?.adapter = myAdapter
    }

    private fun getServicelist() {
        if(connectioncheck!!.isNetworkAvailable()) {
           // progress_history.visibility = View.VISIBLE

            val servicesuccessRespeObservable = apiService!!.getServiceList( "Bearer"+  login_response!!.data.token)

            servicesuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ServiceListResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(servicelistResponse: ServiceListResponse) {
                            //  progress_dashboard.visibility = View.GONE
                            //  Log.d("resp", "onNext: get dtaa response " + orderListResponse.ResponseMessage)
                            var ResponseMessage = servicelistResponse.message
                            var  customer_active = servicelistResponse.customer_is_active
                            pd!!.dismiss()
                            if (servicelistResponse.message_code.equals("1")) {
                                Log.e("servicelist_success", "success")

                                if (arrayList!!.size > 0) {
                                    arrayList!!.clear()
                                }
                                if (servicelistResponse.data!!.size > 0) {

                                    for (i in 0..servicelistResponse.data!!.size - 1) {
                                        val servicemodel = Servicetypemodel()
                                        servicemodel.serviceid = servicelistResponse.data!!.get(i).service_id
                                        servicemodel.servicename = servicelistResponse.data!!.get(i).service_name.toString()
                                        servicemodel.serviceimg = servicelistResponse.data!!.get(i).service_image.toString()

                                        arrayList!!.add(servicemodel)

                                    }
                                    rv_service!!.visibility = View.VISIBLE
                                    tv_s_no_record!!.visibility = View.GONE
                                    myAdapter!!.notifyDataSetChanged()

                                }else{
                                    rv_service!!.visibility = View.GONE
                                    tv_s_no_record!!.visibility = View.VISIBLE
                                }

                            } else {
                                Log.e("servicelist_error", "error")

                                rv_service!!.visibility = View.GONE
                                tv_s_no_record!!.visibility = View.VISIBLE
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


}




