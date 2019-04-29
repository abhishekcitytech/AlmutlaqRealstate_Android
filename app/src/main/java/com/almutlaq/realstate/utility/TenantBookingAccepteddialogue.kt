package com.almutlaq.realstate.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.icu.text.DateFormat
import android.os.Bundle
import android.view.Window
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tenant_bookingaccepted_dialogue.*
import java.text.SimpleDateFormat
import java.util.*

class TenantBookingAccepteddialogue (context: Context,ack_slot_date: String?,csk_time: String?,
                                     bookingid: String?,serviceid: String?,subservice_id: String?) : Dialog(context){
    var contxt = context
    var acknowleddement_date = ack_slot_date
    var csk_slot_time = csk_time
    var service_bookingid = bookingid
    var t_service_id = serviceid
    var t_subservice_id = subservice_id
    private var pd: Dialog? = null
    private var _pref: Pref? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null
    var login_response: LoginResponse? = null
   //// var accept_status = "N"

    var ackdate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.tenant_bookingaccepted_dialogue)
        init()
        performclick()
    }

    private fun init() {
        _pref = Pref(context)
        pd = Commonfunctions.custom_loader(contxt as Activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(context)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        login_response = Commonfunctions.get_login_data(contxt as Activity)

        if (!acknowleddement_date.equals("")){
          val inputFormat  = SimpleDateFormat("yyyy-MM-dd")
          val date = inputFormat.parse(acknowleddement_date)
          ackdate = inputFormat.format(date) }
        tv_ba_date.text = "Are you available on date " + ackdate + " at"
        tv_ba_time.text = csk_slot_time + " for our service engineer to visit your premises?"
    }

    private fun performclick() {

        tv_ba_submit.setOnClickListener {
              //  accept_status = "Y"
                callbookacceptedApi("Y")
            //dismiss()
        }
        tv_ba_cancel.setOnClickListener {
           // accept_status = "N"
            callbookacceptedApi("N")
            dismiss() }
    }

    private fun callbookacceptedApi(accept_status: String) {
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()
            val additem = TBookingAcceptInput()
            additem.service = t_service_id
            additem.subservice = t_subservice_id
            additem.status = accept_status

            val bookingacceptsuccessRespeObservable = apiService!!.sendbookingaccept(this!!.service_bookingid!!,"Bearer"+  login_response!!.data.token,additem)

            bookingacceptsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<TbookingAcceptResponse> {

                        override fun onNext(tbookingAcceptResponse: TbookingAcceptResponse) {

                            ResponseMessage = tbookingAcceptResponse.message
                            pd!!.dismiss()
                            if (tbookingAcceptResponse.message_code == 1) {
                               // Commonfunctions.custom_dialog_ok(contxt as Activity, ResponseMessage.toString())

                                val intent = Intent(context, DashboardNavigation::class.java)
                                intent.putExtra("Booked Fragment", "Booked Fragment")
                                context.startActivity(intent)
                                dismiss()
                            }else {
                                Commonfunctions.custom_dialog_ok(contxt as Activity,  ResponseMessage.toString())
                            }

                        }

                        override fun onSubscribe(d: Disposable) {

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
}