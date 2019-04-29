package com.almutlaq.realstate.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ThankyouActivity
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.CSackSlottimeResponse
import com.almutlaq.realstate.base.CSackSubmitResponse
import com.almutlaq.realstate.dto.CSacknowledgeInput
import com.almutlaq.realstate.dto.LoginResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.acknowledgement_dialogue.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Acknowledgementdialogue (context: Context, booking_req_date: String?, booking_req_time: String?, tenant_slot_time: String?,tenant_slot_date : String?, bookingid: Int?) : Dialog(context), DatePickerDialog.OnDateSetListener {

    var contxt = context
    var ack_date = booking_req_date
    var ack_time = booking_req_time
    var t_slot_time = tenant_slot_time
    var t_slot_dt = tenant_slot_date
    var book_id = bookingid

    var slottime: ArrayList<String>? = ArrayList()
    var login_response: LoginResponse? = null
    private var pd: Dialog? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ack_request_slot: String? = null
    var SpinnerHintString = "Choose reporting time"

    var year = 0
    var month = 0
    var day = 0

    //  internal lateinit var bookingtimeAdapter: ArrayAdapter<String>
    //  internal var time = arrayOf("09:00 - 12:00", "12:00 - 15:00", "15:00 - 18:00", "18:00 - 20:00","Choose booking requested time")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.acknowledgement_dialogue)
        init()
        performclick()
    }

    private fun init() {
        login_response = Commonfunctions.get_login_data(contxt as Activity)
        pd = Commonfunctions.custom_loader(contxt as Activity, StatusConstant.wait_msg)
        connectioncheck = NetworkConnetionCheck(contxt)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)

        pd!!.show()

        // tvAck_datetime.setText(ack_date + " " + ack_time)
        tvAck_datetime.setText("Booking date : " + t_slot_dt)
        tvAck_tenant_slottime.setText("  Booking slot  : " + t_slot_time)

        sp_ack_Time!!.setBackgroundResource(R.drawable.spinner_border_gray)
        slottime!!.add(0, SpinnerHintString)
        set_spinner_data(slottime!!)
        sp_ack_Time.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    private fun showCalender(selecteddate: String) {

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val strDate = sdf.parse(t_slot_dt)

        var currentdt = Date()
        if (currentdt.after(strDate)) {
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        } else {
            val dateString = t_slot_dt!!.split("-")
            year = dateString[0].toInt()
            month = dateString[1].toInt() - 1
            day = dateString[2].toInt()
        }

        val dpd = DatePickerDialog(contxt, R.style.MyDatePickerDialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            // tv_ack_cal_date.text =  dayOfMonth.toString() + "-" + monthOfYear.toString() + "-" + year.toString()
            var date = year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {
                val selecteddate = sdf.parse(date)
                tv_ack_cal_date.text = sdf.format(selecteddate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            getRequestBookingSlotlist(tv_ack_cal_date.text.toString().trim())

        }, year, month, day)
        dpd.show()
        if (currentdt.after(strDate)) {
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        }else{
            dpd.datePicker.minDate = strDate.time
        }

    //  dpd.set(ContextCompat.getColor(contxt, R.color.new_login_button))

    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        var date : String =   year.toString()  + "-" + Commonfunctions.setdate(monthOfYear+1) + "-" + Commonfunctions.setdate(dayOfMonth)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            val selecteddate = sdf.parse(date)
            tv_ack_cal_date.text =  sdf.format(selecteddate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        getRequestBookingSlotlist( tv_ack_cal_date.text.toString().trim())

    }


    private fun set_spinner_data(slottime: ArrayList<String>) {
        val aa = ArrayAdapter(contxt, R.layout.spiiner_item_time, slottime)
        sp_ack_Time.adapter = aa
        sp_ack_Time.setSelection(0)
    }

    private fun performclick() {

        sp_ack_Time.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                ack_request_slot = adapterView.getItemAtPosition(i).toString()
                Log.e("selectedreason", ack_request_slot.toString())

            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        tv_ack_cal_date.setOnClickListener {

            showCalender(tv_ack_cal_date.text.toString().trim())
        }

        tv_ack_cancel.setOnClickListener {
            if (pd!!.isShowing){
                pd!!.dismiss()
            }
            dismiss()
        }

        tv_ack_submit.setOnClickListener {

          var  notes : String = et_ack_note!!.text.toString().trim()
          //  pd!!.show()
            when {
                tv_ack_cal_date.text.toString().isEmpty()->Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.ACK_DATE_ERROR_MSG).show()
                ack_request_slot.equals(SpinnerHintString)-> Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.ACK_SLOTTIME_ERROR_MSG).show()
              //  notes!!.isEmpty() -> Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.REQUEST_NOTE_ERROR_MSG).show()

                else -> submitAcknowledgedata(notes)
            }

           }
    }

    private fun getRequestBookingSlotlist(slot_date: String) {
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()
            Log.e("bid",book_id.toString())
            val getackBookingSlot= apiService!!.getCSbookingSlot(/*this!!.book_id!!,*/slot_date!!,"Bearer "+ login_response!!.data.token)

            getackBookingSlot.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSackSlottimeResponse> {

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(cSackSlottimeResponse: CSackSlottimeResponse) {

                            var ResponseMessage = cSackSlottimeResponse.message

                            if (cSackSlottimeResponse.message_code.equals("1")) {
                                Log.e("cs ack slot response", "success = " + cSackSlottimeResponse.toString())

                                if (cSackSlottimeResponse.data!!.size > 0) {

                                 if (slottime!!.size > 0){
                                     slottime!!.clear()
                                 }

                                    for (i in 0 until cSackSlottimeResponse.data!!.size) {
                                        var stime_start : String = cSackSlottimeResponse.data!!.get(i).start.toString()
                                        val s1 = SimpleDateFormat("HH:mm:ss")
                                        val sd = s1.parse(stime_start)
                                        val s2 = SimpleDateFormat("HH:mm")
                                        stime_start = s2.format(sd).toLowerCase()

                                        var stime_end : String = cSackSlottimeResponse.data!!.get(i).end.toString()
                                        val e1 = SimpleDateFormat("HH:mm:ss")
                                        val ed = e1.parse(stime_end)
                                        val e2 = SimpleDateFormat("HH:mm")
                                        stime_end = e2.format(ed).toLowerCase()

                                        slottime!!.add(i,stime_start +" - "+ stime_end)

                                    }
                                    slottime!!.add(0,SpinnerHintString)
                                    set_spinner_data(slottime!!)
                                    sp_ack_Time!!.setBackgroundResource(R.drawable.spinner_border)
                                    sp_ack_Time.isEnabled = true
                                }else{

                                    Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT,StatusConstant.NO_DATA_MSG).show()
                                }
                            }else{
                                Log.e("servicelist_error", "error")
                                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                tv_ack_cal_date.hint = "Choose reporting date"
                                tv_ack_cal_date.text = ""
                                sp_ack_Time!!.setBackgroundResource(R.drawable.spinner_border_gray)
                                slottime!!.add(0, SpinnerHintString)
                                set_spinner_data(slottime!!)
                                sp_ack_Time.isEnabled = false
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


    private fun submitAcknowledgedata(note: String) {
        if(connectioncheck!!.isNetworkAvailable()) {

            var additem = CSacknowledgeInput()
           /* val sdffilter = SimpleDateFormat("yyyy-MM-dd",java.util.Locale.ENGLISH)//"YYYY-MM-dd"

            val next1 : String =
            val currentdate : String = sdffilter.format(next1)*/
            additem.slot_date = tv_ack_cal_date.text.toString().trim()
            additem.slot_time = ack_request_slot
            additem.booking_acknowledge_note = note

            val acksubmitResponseObservable = apiService!!.sendCSAcknowledgedData(this!!.book_id!!,"Bearer "+ login_response!!.data.token,additem)
               pd!!.show()
            acksubmitResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSackSubmitResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onNext(cSackSubmitResponse: CSackSubmitResponse)
                        {
                           var ResponseMessage = cSackSubmitResponse.message
                            pd!!.dismiss()
                            Log.e("cs ack submit response", "success = " + ResponseMessage.toString())
                            if (cSackSubmitResponse.message_code.equals("1")) {
                                et_ack_note!!.getText().clear()

                                dismiss()  //dialog dismiss
                                val intent = Intent(context, ThankyouActivity::class.java)
                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG,"Acknowledgementdialogue")
                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT,ResponseMessage.toString())
                                context.startActivity(intent)

                            } else {
                                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()


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