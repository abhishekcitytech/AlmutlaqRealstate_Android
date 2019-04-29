package com.almutlaq.realstate.utility

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ThankyouActivity
import com.almutlaq.realstate.adapter.DialougePercentageAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.CSUpdateStatusResponse
import com.almutlaq.realstate.base.CSengineernameResponse
import com.almutlaq.realstate.base.CSpercentageResponse
import com.almutlaq.realstate.dto.CSupdateServiceStatusInput
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.SdialoguePercentModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.service_status_dialogue.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.almutlaq.realstate.base.CSrescheduledtimeResponse
import kotlinx.android.synthetic.main.acknowledgement_dialogue.*
import java.text.ParseException

class ServiceStatusdialogue (context: Context, booking_req_date: String?, booking_req_time: String?, labour_name: String?, bookingid: Int?, booking_completion_percentage: String?, updated_percent_note: String?,event_create : Boolean) : Dialog(context) , DatePickerDialog.OnDateSetListener {

    var contxt = context
    var rq_date = booking_req_date
    var rq_time = booking_req_time
    var labour_nm = labour_name
    var book_id = bookingid
    var booking_complt_percentage = booking_completion_percentage
    var updated_p_note = updated_percent_note
    var event_flag = event_create

    var login_response: LoginResponse? = null
    private var pd: Dialog? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    private var rvStatusPercent: RecyclerView? = null
//    private var myAdapter: SdialoguePercentListAdapter? = null
    private var myAdapter: DialougePercentageAdapter? = null
    private var arrayList: ArrayList<SdialoguePercentModel>? = null
    private var percentlayoutManager: RecyclerView.LayoutManager? = null

    var service_percentage : String? = ""

    var SpinnerHintString = "Select engineer name"

    var engineername : String? = null
    var engineer_id : String? = null

    var stDate: String? = null

    var engineer_id_list: ArrayList<String>? =  ArrayList()
    var rslottime: ArrayList<String>? =  ArrayList()

    var reschedule_slottime: String? = null
    var SptimeHintString = "Reschedule time"

    var year = 0
    var month = 0
    var day = 0
    var strDate : Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.service_status_dialogue)

        init()
       // val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
      //  ActivityCompat.requestPermissions(context as Activity, permissions,0)
        pd!!.show()
        getpercentlist()
        performclick()
    }


    private fun init() {

        login_response = Commonfunctions.get_login_data(contxt as Activity)
        pd = Commonfunctions.custom_loader(contxt as Activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(contxt)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)

        tvss_datetime.setText(rq_date + " " + rq_time )

        rvStatusPercent = findViewById(R.id.rvStatusPercent)
        rvStatusPercent!!.isNestedScrollingEnabled = false
        arrayList = ArrayList<SdialoguePercentModel>()
        percentlayoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false) as RecyclerView.LayoutManager?
//        myAdapter = SdialoguePercentListAdapter(arrayList!!,contxt, R.layout.custom_rating_layout)


        if (!booking_complt_percentage.equals("")){

            if (! updated_p_note.toString().equals("null"))
               et_ss_note.setText(updated_p_note.toString())

          //  if (! labour_nm.toString().equals("null"))     // todo : engineername selection
             //  et_ss_engineername.setText(labour_nm.toString())

         /*   if (booking_complt_percentage.equals("100")){
                tv_res_cal_date.isEnabled = false
                tv_res_cal_time.isEnabled = false
            }else{
                tv_res_cal_date.isEnabled = true
                tv_res_cal_time.isEnabled = true
            }

*/
            sp_res_cal_Time!!.setBackgroundResource(R.drawable.edittxt_box_gray)
            sp_res_cal_Time.isEnabled = false
            rslottime!!.add(0,SptimeHintString)
             set_spinner_timeslot(rslottime!!)
        }

    }

    private fun set_spinner_data(slottime: ArrayList<String>) {
        val aa = ArrayAdapter(contxt, R.layout.spiiner_item_time, slottime)
        sp_engineername.adapter = aa
        sp_engineername.setSelection(0)
    }

    private fun set_spinner_timeslot(slottime: ArrayList<String>) {
        val aa = ArrayAdapter(contxt, R.layout.spiiner_item_time, slottime)
        sp_res_cal_Time.adapter = aa
        sp_res_cal_Time.setSelection(0)
    }


    private fun performclick() {

       // getpercentstatus(this!!.service_percentage!!)

        tv_res_cal_date.setOnClickListener {
            showCalender(tv_res_cal_date.text.toString().trim())
        }

       /* tv_res_cal_time.setOnClickListener {
            //getTime()
        }*/

        sp_engineername.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                engineername = adapterView.getItemAtPosition(i).toString()
                engineer_id = engineer_id_list!![i]

                Log.e("selectedreason", engineername.toString())

            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }


        sp_res_cal_Time.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                reschedule_slottime = adapterView.getItemAtPosition(i).toString()
                Log.e("selectedreason", reschedule_slottime.toString())

            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        tv_ss_cancel.setOnClickListener {
            if(pd!!.isShowing)
                pd!!.dismiss()
            dismiss()
        }

        tv_ss_submit.setOnClickListener {

            submitUpdateStatusdata()

        }
    }

    public fun getpercentstatus(status : String) {

    }

    private fun getpercentlist() {
        if(connectioncheck!!.isNetworkAvailable()) {
            // progress_history.visibility = View.VISIBLE

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
                                if (cSpercentageResponse.data.isNotBlank() && cSpercentageResponse.data.isNotEmpty()) {

                                    var data = cSpercentageResponse.data
                                    val items : List<String> = data.split(",")
                                    for (item : String in items) {

                                        val spmodel = SdialoguePercentModel()
                                        spmodel.statuspercentage = item
                                        arrayList!!.add(spmodel)
                                    }

                                    set_data()
                                }
                               // Handler().postDelayed({  pd!!.dismiss() }, 10000)



                            } else {
                                Log.e("servicelist_error", "error")
                                pd!!.dismiss()
                                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }
                            getEngineernamelist()
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            getEngineernamelist()
                        }

                        override fun onComplete() {

                        }
                    })

        }else{
            connectioncheck!!.getNetworkActiveAlert().show()
        }

    }

    private fun set_data() {
        myAdapter=DialougePercentageAdapter(arrayList!!,contxt,booking_complt_percentage,tv_res_cal_date,sp_res_cal_Time)
        rvStatusPercent!!.setHasFixedSize(true)
        rvStatusPercent?.layoutManager = percentlayoutManager
        rvStatusPercent?.adapter = myAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun showCalender(selecteddate: String) {
        if(tv_res_cal_date.text.toString() == ""){
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        }else{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            var dt = tv_res_cal_date.text.toString().trim()
            strDate = sdf.parse(dt)
            val dateString = dt!!.split("-")
            year = dateString[0].toInt()
            month = dateString[1].toInt() - 1
            day = dateString[2].toInt()
        }

        val dpd = DatePickerDialog(contxt,R.style.MyDatePickerDialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            //  tv_filterdate.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)
            //tv_res_cal_date.text =  dayOfMonth.toString() + "-" + monthOfYear.toString() + "-" + year.toString()
           var date=  year.toString()  + "-" + (monthOfYear+1).toString() +"-" + dayOfMonth.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {
                val selecteddate = sdf.parse(date)
                tv_res_cal_date.text =  sdf.format(selecteddate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            getRescheduleTimeSlotlist(tv_res_cal_date.text.toString().trim())


        }, year, month, day)
        dpd.show()
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        //  dpd.set(ContextCompat.getColor(contxt, R.color.new_login_button))
    }

   /* fun getTime(){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            tv_res_cal_time.text = SimpleDateFormat("HH:mm").format(cal.time)
        }

       *//* tv_res_cal_time.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }*//*
    }*/

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        //var date : String = Commonfunctions.setdate(dayOfMonth) + "-" + Commonfunctions.setdate(monthOfYear + 1) + "-" + year
        var date : String =   year.toString()  + "-" + Commonfunctions.setdate(monthOfYear+1) + "-" + Commonfunctions.setdate(dayOfMonth)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            val selecteddate = sdf.parse(date)
            tv_res_cal_date.text =  sdf.format(selecteddate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        getRescheduleTimeSlotlist(tv_res_cal_date.text.toString().trim())
    }

    private fun getRescheduleTimeSlotlist(selecteddate: String) {
             var day = selecteddate
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()

            val getRtimeSlot= apiService!!.getCSRescheduletime(selecteddate,"Bearer "+ login_response!!.data.token)

            getRtimeSlot.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSrescheduledtimeResponse> {

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(cSrescheduledtimeResponse: CSrescheduledtimeResponse) {

                            var ResponseMessage = cSrescheduledtimeResponse.message

                            if (cSrescheduledtimeResponse.message_code.equals("1")) {
                                Log.e("cs ack slot response", "success = " + cSrescheduledtimeResponse.toString())

                                if (cSrescheduledtimeResponse.data!!.size > 0) {

                                    if (rslottime!!.size > 0){
                                        rslottime!!.clear()
                                    }

                                    for (i in 0 until cSrescheduledtimeResponse.data!!.size) {
                                        var stime_start : String = cSrescheduledtimeResponse.data!!.get(i).start.toString()
                                        val s1 = SimpleDateFormat("HH:mm:ss")
                                        val sd = s1.parse(stime_start)
                                        val s2 = SimpleDateFormat("HH:mm")
                                        stime_start = s2.format(sd).toLowerCase()

                                        var stime_end : String = cSrescheduledtimeResponse.data!!.get(i).end.toString()
                                        val e1 = SimpleDateFormat("HH:mm:ss")
                                        val ed = e1.parse(stime_end)
                                        val e2 = SimpleDateFormat("HH:mm")
                                        stime_end = e2.format(ed).toLowerCase()

                                        rslottime!!.add(i,stime_start +" - "+ stime_end)
                                    }
                                    rslottime!!.add(0,SptimeHintString)
                                    set_spinner_timeslot(rslottime!!)
                                    sp_res_cal_Time!!.setBackgroundResource(R.drawable.edittxt_box)
                                    sp_res_cal_Time.isEnabled = true
                                }else{

                                    Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT,StatusConstant.NO_DATA_MSG).show()
                                }
                            }else{
                                Log.e("servicelist_error", "error")
                                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                tv_res_cal_date.hint = "Reschedule date"
                                tv_res_cal_date.text = ""
                                sp_res_cal_Time!!.setBackgroundResource(R.drawable.edittxt_box_gray)
                                sp_res_cal_Time.isEnabled = false
                                rslottime!!.add(0,SptimeHintString)
                                set_spinner_timeslot(rslottime!!)
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

    private fun getEngineernamelist() {
        if(connectioncheck!!.isNetworkAvailable()) {
            Log.e("bid",book_id.toString())
            val getackBookingSlot= apiService!!.getengineernamelist("Bearer "+ login_response!!.data.token)

            getackBookingSlot.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSengineernameResponse> {

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(cSengineernameResponse: CSengineernameResponse) {

                            var ResponseMessage = cSengineernameResponse.message

                            if (cSengineernameResponse.message_code.equals("1")) {
                                Log.e("cs ack slot response", "success = " + cSengineernameResponse.toString())

                                if (cSengineernameResponse.data!!.size > 0) {

                                    var engname: ArrayList<String>? =  ArrayList()

                                    for (i in 0 until cSengineernameResponse.data!!.size) {

                                        var engineer_id : String = cSengineernameResponse.data!!.get(i).engineer_id.toString()
                                        var engineer_name : String = cSengineernameResponse.data!!.get(i).engineer_name

                                        engname!!.add(i,engineer_name)
                                        engineer_id_list!!.add(i,engineer_id)

                                    }
                                    engname!!.add(0,SpinnerHintString)
                                    engineer_id_list!!.add(0,"0")
                                    set_spinner_data(engname!!)
                                }else{

                                    Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT,StatusConstant.NO_DATA_MSG).show()
                                }
                            }else{
                                Log.e("servicelist_error", "error")
                                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()
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

    //note: String, labour_name: String, selected_percent: String?

    fun submitUpdateStatusdata() {

            var notes: String = et_ss_note!!.text.toString().trim()
            var labournm: String = engineername.toString().trim()
            service_percentage = StatusConstant.statuspercent

            if (service_percentage!!.isEmpty()) {
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.REQUEST_PERCENT_ERROR_MSG).show()

            } else if (tv_res_cal_date.text.toString()!!.isEmpty() && StatusConstant.statuspercent != "100") {
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.REQUEST_DATE_ERROR_MSG).show()

            } else if (reschedule_slottime.equals(SptimeHintString) && StatusConstant.statuspercent != "100") {
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.REQUEST_TIME_ERROR_MSG).show()

            } else if (et_ss_note!!.text.toString().trim().isEmpty()) {
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.REQUEST_NOTE_ERROR_MSG).show()

            } else if (engineername.equals(SpinnerHintString)) {
                Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, StatusConstant.REQUEST_LABOUNM_ERROR_MSG).show()
            }
            //  else ->  submitUpdateStatusdata(notes,labournm, this!!.service_percentage)

            else {
                if (connectioncheck!!.isNetworkAvailable()) {
                    pd!!.show()
                    var additem = CSupdateServiceStatusInput()
                    additem.status_update_note = notes
                    additem.reschedule_date = tv_res_cal_date.text.toString().trim()

                    additem.completion_percentage = service_percentage!!.replace("%", "")
                    additem.labour_name = labournm
                    additem.labour_id = engineer_id
                    //  additem.reschedule_time = tv_res_cal_time.text.toString().trim()
                    additem.reschedule_time = reschedule_slottime

                    if  (!tv_res_cal_date.text.toString().trim().equals("")) {   /// for alarm event time
                        val separated = reschedule_slottime!!.split("-")
                        var start_eventtime = separated[0].trim() // this will contain "Fruit"

                        stDate = tv_res_cal_date.text.toString().trim() + " " + start_eventtime //"17:10" /*
                    }

                    Log.e("percent", additem.completion_percentage.toString())

                    val updatestatusResponseObservable = apiService!!.sendCSUpdateservicestatus(this!!.book_id!!, "Bearer " + login_response!!.data.token, additem)
                    pd!!.show()
                    updatestatusResponseObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<CSUpdateStatusResponse> {
                                override fun onSubscribe(d: Disposable) {

                                }
                                override fun onNext(csUpdateStatusResponse: CSUpdateStatusResponse) {
                                    var ResponseMessage = csUpdateStatusResponse.message
                                    pd!!.dismiss()
                                    Log.e("cs res response", "success = " + ResponseMessage.toString())
                                    if (csUpdateStatusResponse.message_code.equals("1")) {
                                        et_ss_note!!.getText().clear()
                                        et_ss_engineername!!.getText().clear()

                                        if (!tv_res_cal_date.text.toString().trim().equals("") && event_flag)   /// for alarm event
                                        {
                                            val calDate = GregorianCalendar()
                                            val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                            val targetFormat = SimpleDateFormat("yyyy,MM,dd,HH,mm")
                                            var date: Date? = null
                                            try {
                                                date = originalFormat.parse(stDate)
                                                stDate = targetFormat.format(date)

                                            } catch (ex: ParseException) {
                                            }

                                            var startMillis: Long = 0
                                            val dates = stDate!!.split(",")
                                            val SD_YeaR = dates[0]
                                            val SD_MontH = dates[1]
                                            val SD_DaY = dates[2]
                                            val SD_HouR = dates[3]
                                            val SD_MinutE = dates[4]

                                            calDate.set(Integer.parseInt(SD_YeaR), Integer.parseInt(SD_MontH) - 1, Integer.parseInt(SD_DaY), Integer.parseInt(SD_HouR), Integer.parseInt(SD_MinutE))
                                            startMillis = calDate.getTimeInMillis();

                                            var description = "Reminder Date for Rescheduled Service"

                                            addEventToCalender("Reschedule Reminder", description, "", 1, startMillis, true, startMillis)
                                        }

                                        pd!!.dismiss()  //dialog dismiss

                                        if (!tv_res_cal_date.text.toString().trim().equals(""))   /// for alarm event
                                        { if (event_flag){
                                                var dt = tv_res_cal_date.text.toString().trim()
                                                var msg : String = "Event is created in your calendar for " + dt + " at " + reschedule_slottime + " Hours"
                                               // Commonfunctions.commonDialogue(context, StatusConstant.ALERT, msg).show()

                                                val intent = Intent(context, ThankyouActivity::class.java)
                                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG, "ServiceStatusEvent")
                                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT, msg)
                                                context.startActivity(intent)
                                        }else{
                                            val intent = Intent(context, ThankyouActivity::class.java)
                                            intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG, "ServiceStatusdialogue")
                                            intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT, ResponseMessage.toString())
                                            context.startActivity(intent)
                                        }
                                        }else{
                                            val intent = Intent(context, ThankyouActivity::class.java)
                                            intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG, "ServiceStatusdialogue")
                                            intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT, ResponseMessage.toString())
                                            context.startActivity(intent)
                                        }

                                    } else {
                                        pd!!.dismiss()
                                        Commonfunctions.commonDialogue(contxt, StatusConstant.ALERT, ResponseMessage.toString()).show()

                                    }
                                }

                                override fun onError(e: Throwable) {
                                    pd!!.dismiss()
                                    e.printStackTrace()
                                }

                                override fun onComplete() {

                                }
                            })

                } else {
                    connectioncheck!!.getNetworkActiveAlert().show()
                }


            }

    }


 /*  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                submitUpdateStatusdata(service_percentage)
            }
        }

    }*/

    fun addEventToCalender(title: String, addInfo: String, place: String, status: Int,
                           startDate: Long, isRemind: Boolean, endDate: Long): Long {

        val cr = contxt.getContentResolver()
        val eventUriStr = "content://com.android.calendar/events"

        val event = ContentValues()

        event.put(CalendarContract.Events.CALENDAR_ID, 1)
        event.put(CalendarContract.Events.TITLE, title)
        event.put(CalendarContract.Events.DESCRIPTION, addInfo)
        event.put(CalendarContract.Events.EVENT_LOCATION, place)

        val timeZone : String = TimeZone.getDefault().getID()
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone)

        // For next 1hr
        event.put(CalendarContract.Events.DTSTART, startDate)
        event.put(CalendarContract.Events.DTEND, endDate)

        //If it is bithday alarm or such kind (which should remind me for whole day) 0 for false, 1 for true
        // values.put("allDay", 1);
        //  event.put("eventStatus", status);
        event.put(CalendarContract.Events.HAS_ALARM, 1)

        val eventUri = cr.insert(Uri.parse(eventUriStr), event)
        val eventID = java.lang.Long.parseLong(eventUri!!.lastPathSegment)

        if (isRemind) {
            val reminderUriString = "content://com.android.calendar/reminders"
            val reminderValues = ContentValues()
            ///reminderValues.put("event_id", eventID)
            reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID)
            // Default value of the system. Minutes is a integer
            ///reminderValues.put("minutes", 5)
            reminderValues.put(CalendarContract.Reminders.MINUTES, 60)
            // Alert Methods: Default(0), Alert(1), Email(2), SMS(3)
            ///reminderValues.put("method", 1)
            reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM)
            cr.insert(Uri.parse(reminderUriString), reminderValues) //Uri reminderUri =
        }
        return eventID
    }


}