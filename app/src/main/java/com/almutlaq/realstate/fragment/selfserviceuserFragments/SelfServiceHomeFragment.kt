package com.almutlaq.realstate.fragment.selfserviceuserFragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.adapter.CalendarGridCellAdapter
import com.almutlaq.realstate.adapter.SelfCalendrListAdapter
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.CSdashboarddateResponse
import com.almutlaq.realstate.base.CsCalendardatesResponse
import com.almutlaq.realstate.dto.CalendrdatelistModel
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.SelfCalendrlistModel
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_drawer.*
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("ValidFragment")
class SelfServiceHomeFragment @SuppressLint("ValidFragment") constructor
(act: Activity) : Fragment(){

    val activity = act
    var calendar : Calendar? = null
    private var pd: Dialog? = null
    private var rel_calender_show: RelativeLayout? = null
    private var calenderView: GridView? = null
    private var month: Int = 0
    private var year: Int = 0
    private var day: Int = 0
    private var _calendar: Calendar? = null
    private var calendar_adapter: CalendarGridCellAdapter? = null
    private val dateTemplate = "MMMM yyyy"
    private var prevMonth: ImageView? = null
    private var nextMonth: ImageView? = null
    private var currentMonth: Button? = null
    private var dt_list: ArrayList<CalendrdatelistModel>? = null
    val user: String? = ""
    var  tvnodatelist : TextView? = null
    var  lv_date : TextView? = null
    private var apiService: ApiInterface? = null
    var login_response: LoginResponse? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    private var rv_monthlist: RecyclerView? = null
    private var myAdapter: SelfCalendrListAdapter? = null
    private var calendar_customer_service_List: ArrayList<SelfCalendrlistModel>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    var  selectedmonth : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater.inflate(R.layout.fragment_self_serviceuser_home, container, false)
        init(rootview)
        performclick()
        return rootview
    }

    @SuppressLint("ResourceAsColor")
    override fun onPause() {
        super.onPause()
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)//R.drawable.toolbar_bg
        //  (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(resources.getColor("ffffff"))
        (getActivity() as DashboardNavigation). actionBarDrawerToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white, getActivity()!!.theme))
        }else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white))
        }
    }

    override fun onResume() {
        super.onResume()
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white, getActivity()!!.theme))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white))
        }
    }


    private fun init(rootview: View?) {

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white, getActivity()!!.theme))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white))
        }

       // (getActivity() as DashboardNavigation).imgNotify.visibility = View.VISIBLE

        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient.getClient().create(ApiInterface::class.java)
        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        // _pref = Pref(activity)
        login_response = Commonfunctions.get_login_data(activity)

        dt_list = ArrayList()
        //for calendar

        rel_calender_show = rootview!!.findViewById(R.id.rel_calender_show)
        tvnodatelist = rootview.findViewById(R.id.tvnodatelist)
        lv_date = rootview.findViewById(R.id.lv_date)


        rv_monthlist = rootview.findViewById(R.id.rv_monthlist)
        calendar_customer_service_List = ArrayList()


        layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        myAdapter = SelfCalendrListAdapter(calendar_customer_service_List!!, this.activity, R.layout.selfservice_calendrlist_row)
        rv_monthlist?.layoutManager = LinearLayoutManager(context)
        rv_monthlist?.adapter = myAdapter


        calendar = Calendar.getInstance()
        pd!!.show()
        getCalendarSelectedDateList(calendar!!.get(Calendar.YEAR).toString(),(calendar!!.get(Calendar.MONTH)+1).toString())

    }

    private fun getCalendarSelectedDateList(yr: String,month: String) {  ///get CALENDAR service dates
        //Sending month and year. day is static.
        var currentdate = "";
        if(connectioncheck!!.isNetworkAvailable()) {
            // progress_history.visibility = View.VISIBLE


            currentdate = if (month.length < 2)
                "$yr-0$month-1"
            else "$yr-$month-1"

            Log.e("calendr_date",currentdate)

            val calendarsuccessRespeObservable = apiService!!.getCalendarSelecteddatelist(currentdate,"Bearer"+ login_response!!.data.token)

            calendarsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CsCalendardatesResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(csCalendardatesResponse: CsCalendardatesResponse) {
                            //  progress_dashboard.visibility = View.GONE
                            //  Log.d("resp", "onNext: get tenantbook response " + orderListResponse.ResponseMessage)
                            val ResponseMessage = csCalendardatesResponse.message

                            if (csCalendardatesResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (dt_list!!.size > 0) {
                                    dt_list!!.clear()
                                }
                                if (csCalendardatesResponse.data.size > 0) {

                                    for (i in 0 until csCalendardatesResponse.data.size) {
                                        val calendrdatelistModel = CalendrdatelistModel()

                                        calendrdatelistModel.selecteddate = csCalendardatesResponse.data.get(i).tenant_slot_date

                                        dt_list!!.add(calendrdatelistModel)

                                    }
                                    lv_dt(dt_list)
                                }
                            } else {
                                lv_dt_nill()
                                Log.e("servicelist_error", "error")
                               // Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }
                            getCalenderView()


                            var today_day = calendar!!.get(Calendar.DAY_OF_MONTH).toString()

                          var todaymonth = if (month.length < 2)
                                "0$month"
                            else month

                            today_day = if (today_day.length < 2)
                                "0$today_day"
                            else today_day

                            val today = "$yr-$todaymonth-$today_day"
                            getCSdatedetailsList(today)
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            lv_dt_nill()
                            getCalenderView()
                        }

                        override fun onComplete() {

                        }
                    })
        }else{
            connectioncheck!!.getNetworkActiveAlert().show()
        }

    }

    fun getCSdatedetailsList(currentdate: String) {  ///get service list per date wise
        if(connectioncheck!!.isNetworkAvailable()) {
            if (!pd!!.isShowing) {
                pd!!.show()
            }
            Log.e("calendr_date1",currentdate)
            val csdatelistsuccessRespeObservable = apiService!!.getCSbookedrescheduleddate(currentdate,"Bearer"+ login_response!!.data.token)

            csdatelistsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CSdashboarddateResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(cSdashboarddateResponse: CSdashboarddateResponse) {
                            pd!!.dismiss()
                            //  Log.d("resp", "onNext: get tenantbook response " + orderListResponse.ResponseMessage)
                            val ResponseMessage = cSdashboarddateResponse.message

                            if (cSdashboarddateResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (calendar_customer_service_List!!.size > 0) {
                                    calendar_customer_service_List!!.clear()
                                }
                                if (cSdashboarddateResponse.data.size > 0) {

                                    for (i in 0 until cSdashboarddateResponse.data.size) {
                                        val selfCalendrlistModel = SelfCalendrlistModel()

                                        selfCalendrlistModel.calstatustitle = cSdashboarddateResponse.data.get(i).service_name
                                        selfCalendrlistModel.calstatusdate = cSdashboarddateResponse.data.get(i).booking_requested_date
                                        selfCalendrlistModel.calstatusaddress = cSdashboarddateResponse.data.get(i).flat_no + "," +
                                                cSdashboarddateResponse.data.get(i).building_no + "," +
                                                cSdashboarddateResponse.data.get(i).building_address

                                        selfCalendrlistModel.booking_acknowledge = cSdashboarddateResponse.data.get(i).booking_acknowledge
                                        selfCalendrlistModel.service_boooking_id = cSdashboarddateResponse.data.get(i).service_boooking_id
                                        selfCalendrlistModel.booking_status = cSdashboarddateResponse.data.get(i).booking_status


                                        calendar_customer_service_List!!.add(selfCalendrlistModel)

                                    }

                                    myAdapter!!.notifyDataSetChanged()
                                    rv_monthlist!!.visibility = View.VISIBLE
                                   // tvnodatelist!!.visibility = View.GONE*/

                                    lv_date!!.text = currentdate
//
                                }else{
                                    lv_date!!.text = ""
                                    rv_monthlist!!.visibility = View.GONE
                                }

                            } else {
                                lv_date!!.text = ""
                                Log.e("servicelist_error", "error")
                                rv_monthlist!!.visibility = View.GONE
                              //  tvnodatelist!!.visibility = View.VISIBLE
                              //  tvnodatelist!!.setText(ResponseMessage.toString())*/

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


    private fun performclick() {
        _calendar = Calendar.getInstance(Locale.getDefault())
        month = _calendar!!.get(Calendar.MONTH) + 1
        year = _calendar!!.get(Calendar.YEAR)
        day = _calendar!!.get(Calendar.DAY_OF_MONTH)
       // Log.e("day",day.toString())
//		dt_list=new ArrayList<>();
//		dt_list.add("10");
//		dt_list.add("20");
//        getCalenderView()
        // get_calendar_list(month.toString() + "/" + _calendar!!.get(Calendar.DAY_OF_MONTH) + "/" + year)
    }


    private fun getCalenderView() {
      //   getWebService(month, year)
        val layoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addView = layoutInflater.inflate(R.layout.calenderview, null)
        prevMonth = addView.findViewById(R.id.prevMonth) as ImageView
        nextMonth = addView.findViewById(R.id.nextMonth) as ImageView
        calenderView = addView.findViewById(R.id.calendar) as GridView
        currentMonth = addView.findViewById(R.id.currentMonth) as Button
        currentMonth!!.text = DateFormat.format(dateTemplate, _calendar!!.time)

        //		adapter = new CalendarGridCellAdapter(LeaveCalenderActivity.this, R.id.calendar_day_gridcell, month, year,dt_list,user);
        //		calenderView.setAdapter(adapter);

        prevMonth!!.setOnClickListener {
            lv_date!!.text = ""
                if (month <= 1) {
                        month = 12
                        year--
                    } else {
                        month--
                    }

            selectedmonth = year.toString() + "-" + month.toString() + "-" + day
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {
                val selecteddate = sdf.parse(selectedmonth)
               selectedmonth =  sdf.format(selecteddate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            getCalendarSelectedDateList(year.toString(),month.toString())

            }

            nextMonth!!.setOnClickListener {
                lv_date!!.text = ""
                if (month > 11) {
                    month = 1
                    year++
                } else {
                    month++
                }

                if (month < 10)
                    selectedmonth = year.toString() + "-" +"0"+ month.toString() + "-" + day
                else selectedmonth = year.toString() + "-" + month.toString() + "-" + day
                Log.e("monthnxt",month.toString())
                Log.e("selectedmonthnxt",selectedmonth)
               /* val sdf = SimpleDateFormat("yyyy-MM-dd")
                try {
                 //   val selecteddate = sdf.parse(selectedmonth)
                    selectedmonth =  sdf.format(selectedmonth)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }*/

                Log.e("selectedmonthnxt1",selectedmonth)
                getCalendarSelectedDateList( year.toString() , month.toString())

            }

        Thread(Runnable {
            // TODO Auto-generated method stub
            Log.e("dtlist", dt_list!!.size.toString())
            calendar_adapter = CalendarGridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year, dt_list,this)
            activity.runOnUiThread {
                // TODO Auto-generated method stub
                calendar_adapter!!.notifyDataSetChanged()
                calenderView!!.adapter = calendar_adapter
                rel_calender_show!!.addView(addView)

            }
        }).start()

       /* var background = object : Thread(){
            override fun run() {
                adapter = CalendarGridCellAdapter(activity, R.id.calendar_day_gridcell, month, year, dt_list, user)
                    adapter!!.notifyDataSetChanged()
                    calenderView!!.setAdapter(adapter)
                    rel_calender_show!!.addView(addView)    // todo : crash

            }
        }.start()*/

    }


    @SuppressLint("SetTextI18n")
    private fun setGridCellAdapterToDate(month: Int, year: Int) {
      pd!!.show()
        Thread(Runnable {
            // TODO Auto-generated method stub
            calendar_adapter = CalendarGridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year, dt_list,this)
            _calendar!!.set(year, month - 1, _calendar!!.get(Calendar.DAY_OF_MONTH))
            activity.runOnUiThread {

                calendar_adapter!!.notifyDataSetChanged()
                calenderView!!.adapter = this.calendar_adapter
                try {
                    println("Month " + DateFormat.format(dateTemplate, _calendar!!.time) + "...."+year + (month -1))
                  //  currentMonth!!.text = DateFormat.format(dateTemplate, _calendar!!.time)
                    currentMonth!!.text = DateFormatSymbols().months[month-1] + " "+year
                }catch (e : Exception ){
                    e.printStackTrace()
                }
               pd!!.dismiss()
            }
        }).start()
    }

    fun lv_dt(dt_lst: ArrayList<CalendrdatelistModel>?) {
        this.dt_list = dt_lst
        setGridCellAdapterToDate(month, year)
       // txt_no_holiday.setVisibility(View.GONE)
       // rv_showleave.setVisibility(View.VISIBLE)
      //  this.lst.clear()
       // leaveCalendarAdapter.notifyDataSetChanged()
    }

    fun lv_dt_nill() {
        this.dt_list = dt_list
        setGridCellAdapterToDate(month, year)
        /*this.lst.clear()
        leaveCalendarAdapter.notifyDataSetChanged()
        txt_no_holiday.setText(StatusConstant.no_leave_on_date)
        txt_no_holiday.setVisibility(View.VISIBLE)
        rv_showleave.setVisibility(View.GONE)*/
    }


}