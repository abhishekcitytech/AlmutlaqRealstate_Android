package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.adapter.ComplaintlistListAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.ComplaintListResponse
import com.almutlaq.realstate.dto.Complaintlistmodel
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("ValidFragment")
class ComplaintFragment @SuppressLint("ValidFragment") constructor
(act: Activity, tabtype: String) : Fragment(){

    val activity = act
    private var pd: Dialog? = null
    var  tab  : String = tabtype
    private var apiService: ApiInterface? = null
    var login_response: LoginResponse? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    private var rvComplaint: RecyclerView? = null
    private var myAdapter: ComplaintlistListAdapter? = null
    private var arrayList: ArrayList<Complaintlistmodel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    var tv_c_no_record : TextView? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater!!.inflate(R.layout.complaint_fragment, container, false)
        init(rootview)
        pd!!.show()
       if (tab.equals("C")){
           getComplaintList("C")
        }else if (tab.equals("S")){
           getComplaintList("S")
        }

        return rootview
    }

    private fun init(rootview: View?) {
        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        login_response = Commonfunctions.get_login_data(activity)

        tv_c_no_record  = rootview!!.findViewById(R.id.tv_c_no_record)

        rvComplaint = rootview!!.findViewById(R.id.rvComplaint)
        arrayList = ArrayList<Complaintlistmodel>()
        layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        myAdapter = ComplaintlistListAdapter(arrayList!!, activity, R.layout.complaintlist_row)

        rvComplaint?.layoutManager = LinearLayoutManager(context)
        rvComplaint?.adapter = myAdapter
    }


    private fun getComplaintList(stat: String) {
        if(connectioncheck!!.isNetworkAvailable()) {

            val clistsuccessRespeObservable = apiService!!.getComplaintList( stat,"Bearer"+ login_response!!.data.token)

            clistsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ComplaintListResponse> {
                        override fun onSubscribe(d: Disposable) {
                        }
                        override fun onNext(complaintListResponse: ComplaintListResponse) {
                            var ResponseMessage = complaintListResponse.message
                            pd!!.dismiss()
                            if (complaintListResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                  if (arrayList!!.size > 0) {
                                      arrayList!!.clear()
                                   }
                                   if (complaintListResponse.data!!.size > 0) {
                                       for (i in 0..complaintListResponse.data!!.size - 1) {
                                           val complaintlistmodel = Complaintlistmodel()
                                           complaintlistmodel.complainttitle = complaintListResponse.data!!.get(i).complain_subject
                                           complaintlistmodel.complaintquote = complaintListResponse.data!!.get(i).complain_note
                                           complaintlistmodel.complaintstatus = complaintListResponse.data!!.get(i).complain_status
                                           complaintlistmodel.complaintclosednote = complaintListResponse.data!!.get(i).complain_close_note
                                           arrayList!!.add(complaintlistmodel)
                                       }
                                       rvComplaint!!.visibility = View.VISIBLE
                                       tv_c_no_record!!.visibility = View.GONE
                                       myAdapter!!.notifyDataSetChanged()
                                   }else {
                                       pd!!.dismiss()
                                       Log.e("servicelist_error", "error")
                                       rvComplaint!!.visibility = View.GONE
                                       tv_c_no_record!!.visibility = View.VISIBLE
                                       //  Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                   }
                            } else {
                                pd!!.dismiss()
                                Log.e("servicelist_error", "error")
                                rvComplaint!!.visibility = View.GONE
                                tv_c_no_record!!.visibility = View.VISIBLE
                              //  Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
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