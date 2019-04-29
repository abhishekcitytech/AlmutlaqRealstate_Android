package com.almutlaq.realstate.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.adapter.ComplaintlistPagerAdapter
import com.almutlaq.realstate.adapter.NotifyListAdapter
import com.almutlaq.realstate.adapter.StatusListAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.NotificationListResponse
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.NotifyListModel
import com.almutlaq.realstate.dto.StatusListModel
import com.almutlaq.realstate.fragment.RegisterComplaintFragment
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.complaint_list_activity.*
import kotlinx.android.synthetic.main.notification_list_activity.*
import kotlinx.android.synthetic.main.toolbar_for_activity.*

class NotificationListActivity  : AppCompatActivity()  {

    var ctoolbar : RelativeLayout? = null

    private var pd: Dialog? = null

    private var apiService: ApiInterface? = null
    //  private var _pref: Pref? = null
    var login_response: LoginResponse? = null
    private var connectioncheck: NetworkConnetionCheck? = null

  //  private var rvNotification: RecyclerView? = null
    private var myAdapter: NotifyListAdapter? = null
    private var arrayList: ArrayList<NotifyListModel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_list_activity)
        init()

        getNotificationList()

    }

    private fun init() {
        ctoolbar = findViewById(R.id.ctoolbar)
        toolbartitle.setText("Message List")
        imgToolback.setOnClickListener { onBackPressed() }

        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(this)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        // _pref = Pref(activity)
        login_response = Commonfunctions.get_login_data(this)


        arrayList = ArrayList<NotifyListModel>()
        layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        myAdapter = NotifyListAdapter(arrayList!!, this, R.layout.notification_row)

        rvNotification?.layoutManager = LinearLayoutManager(this)
        rvNotification?.adapter = myAdapter
    }


    private fun getNotificationList() {
        if(connectioncheck!!.isNetworkAvailable()) {
            // progress_history.visibility = View.VISIBLE

            val notifylistsuccessRespeObservable = apiService!!.getallNotificationList("Bearer"+ login_response!!.data.token)

            notifylistsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<NotificationListResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(notifyListResponse: NotificationListResponse) {
                            //  progress_dashboard.visibility = View.GONE
                            //  Log.d("resp", "onNext: get tenantbook response " + orderListResponse.ResponseMessage)
                            var ResponseMessage = notifyListResponse.message
                            pd!!.dismiss()
                            if (notifyListResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (arrayList!!.size > 0) {
                                    arrayList!!.clear()
                                }
                                if (notifyListResponse.data!!.size > 0) {

                                    for (i in 0..notifyListResponse.data!!.size - 1) {
                                        val notifyListModel = NotifyListModel()

                                        notifyListModel.notidate = notifyListResponse.data!!.get(i).posted_date
                                        notifyListModel.notitime = notifyListResponse.data!!.get(i).posted_time
                                        notifyListModel.notimsg = notifyListResponse.data!!.get(i).message_text
                                        notifyListModel.notisub = notifyListResponse.data!!.get(i).message_subject

                                        arrayList!!.add(notifyListModel)

                                    }
                                    rvNotification!!.visibility = View.VISIBLE
                                    tv_notify_no_record!!.visibility = View.GONE
                                    myAdapter!!.notifyDataSetChanged()

                                }else{
                                    pd!!.dismiss()
                                    Log.e("servicelist_error", "error")
                                    rvNotification!!.visibility = View.GONE
                                    tv_notify_no_record!!.visibility = View.VISIBLE
                                }

                            } else {
                                pd!!.dismiss()
                                Log.e("servicelist_error", "error")
                                rvNotification!!.visibility = View.GONE
                                tv_notify_no_record!!.visibility = View.VISIBLE
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