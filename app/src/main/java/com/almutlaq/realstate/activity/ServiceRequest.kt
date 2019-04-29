package com.almutlaq.realstate.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.almutlaq.realstate.R
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.RequestBookingSlotResponse
import com.almutlaq.realstate.base.RequestServiceResponse
import com.almutlaq.realstate.base.ViewSubServiceListResponse
import com.almutlaq.realstate.dto.BookServiceInput
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.SubServiceListModel
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_service_request.*
import kotlinx.android.synthetic.main.service_status_dialogue.*
import kotlinx.android.synthetic.main.toolbar_for_activity.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ServiceRequest : AppCompatActivity() , OnDateSetListener{

    private var pd: Dialog? = null
    var login_response: LoginResponse? = null
    var stoolbar : RelativeLayout? = null
    //internal lateinit var timeAdapter: ArrayAdapter<String>
    //internal var time = arrayOf("09:00 - 12:00", "12:00 - 15:00", "15:00 - 18:00", "18:00 - 20:00","Choose booking requested time")

    internal lateinit var timeAdapter: ArrayAdapter<String>

    private var txt_addr : TextView? = null
    private var txt_service_addr : TextView? = null
    private var txt_srq_date : TextView? = null
    private var txt_header_name : TextView? = null
    private var edt_note: EditText? = null
    private var btn_submit_request:Button? = null
    private var img_service_pic:ImageView? = null

    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    var serviceid: Int? = null
    var servicename: String? = null
    var service_request_slot: String? = null
    var subservice_list_request: String? = null
    var service_notes : String? = null
    var ResponseMessage : String? = null
    var service_image_value: String? =null

    //for image capture
    private val GALLERY = 1
    private val CAMERA = 2
    var SpinnerHintString = "Choose time"
    var SpinnerHintSubTypeString = "Choose service sub type"
    var sp_subservice_id : String? = null

    var subservicelist: ArrayList<String>? =  ArrayList()
    var subservice_id_list: ArrayList<String>? =  ArrayList()

    var subservicelistaa: ArrayList<SubServiceListModel>? =  ArrayList()

    var slottime: ArrayList<String>? =  ArrayList()

    var year = 0
    var month = 0
    var day = 0
    var strDate : Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_request)
        init()
        performclick()
    }

    private fun init() {
        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(this)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)

        login_response = Commonfunctions.get_login_data(this@ServiceRequest)

        stoolbar = findViewById(R.id.stoolbar)
        toolbartitle.setText("Service Request")
        txt_addr =  findViewById(R.id.tv_srq_building)
        txt_service_addr =  findViewById(R.id.tv_service_addr)
        txt_srq_date =  findViewById(R.id.tv_srq_date)
        txt_header_name = findViewById(R.id.tv_sreq_title)
        edt_note = findViewById(R.id.et_srq_note)
        btn_submit_request = findViewById(R.id.btn_submit_request)

        img_service_pic = findViewById(R.id.img_srqPic)

        serviceid = getIntent().getIntExtra("service_id",0)
        servicename = getIntent().getStringExtra("service_name")

        txt_addr!!.text = "Building no : "+ login_response!!.data.customer_building_no + ",Flat no : " +  login_response!!.data.customer_flat_no
        txt_service_addr!!.text = login_response!!.data.customer_street_address

        txt_header_name !!.text = servicename

        val sdf = SimpleDateFormat("yyyy-M-dd hh:mm")
        val currentDate = sdf.format(Date())
        txt_srq_date!!.text = currentDate

        val weekDayName: String
        val dayFormat = SimpleDateFormat("EEEE", Locale.US)
        val calendar = Calendar.getInstance()
        weekDayName = dayFormat.format(calendar.time)

        //StatusConstant.TENANT_BOOKING_SLOT = StatusConstant.TENANT_BOOKING_SLOT + weekDay

        imgToolback.setOnClickListener { onBackPressed() }

        pd!!.show()
        getViewSubServiceslist(serviceid!!)
        //  getRequestBookingSlotlist(weekDayName)

        spTime!!.setBackgroundResource(R.drawable.edittxt_box_gray)
        spTime.isEnabled = false
        slottime!!.add(0,SpinnerHintString)
        set_spinner_data(slottime!!)

    }

    @SuppressLint("SetTextI18n")
    private fun showCalender(selecteddate: String) {
        if(tv_req_cal_date.text.toString() == ""){
            val c = Calendar.getInstance()
             year = c.get(Calendar.YEAR)
             month = c.get(Calendar.MONTH)
             day = c.get(Calendar.DAY_OF_MONTH)
        }else{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            var dt = tv_req_cal_date.text.toString().trim()
             strDate = sdf.parse(dt)
            val dateString = dt!!.split("-")
            year = dateString[0].toInt()
            month = dateString[1].toInt() - 1
            day = dateString[2].toInt()
        }

        val dpd = DatePickerDialog(this,R.style.MyDatePickerDialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            //  tv_filterdate.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)
            //tv_res_cal_date.text =  dayOfMonth.toString() + "-" + monthOfYear.toString() + "-" + year.toString()
            var date =  year.toString()  + "-" + (monthOfYear+1).toString() +"-" + dayOfMonth.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {
                val selecteddate = sdf.parse(date)
                tv_req_cal_date.text =  sdf.format(selecteddate)
            } catch (e: ParseException) {
                e.printStackTrace()
              }

            getRequestBookingSlotlist(tv_req_cal_date.text.toString().trim())

        }, year, month, day)
        dpd.show()
       // if(tv_req_cal_date.text.toString() == ""){
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
       /* }else{
            dpd.datePicker.minDate = strDate!!.time
        }*/

        //  dpd.set(ContextCompat.getColor(contxt, R.color.new_login_button))
    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var date : String =   year.toString()  + "-" + Commonfunctions.setdate(monthOfYear+1) + "-" + Commonfunctions.setdate(dayOfMonth)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            val selecteddate = sdf.parse(date)
            tv_req_cal_date.text =  sdf.format(selecteddate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        getRequestBookingSlotlist(tv_res_cal_date.text.toString().trim())
    }

    private fun performclick() {

        tv_req_cal_date.setOnClickListener {
            showCalender(tv_req_cal_date.text.toString().trim())
        }

        spTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                service_request_slot = spTime.getSelectedItem().toString()
                Log.e("selectedSlot", service_request_slot)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        spSubType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                subservice_list_request = spSubType.getSelectedItem().toString()
                Log.e(" subservice_list", subservice_list_request)

                var sp_item_name: String = adapterView.getItemAtPosition(i).toString()
                sp_subservice_id = subservice_id_list!![i]

                //Toast.makeText(this@ServiceRequest, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }

        btn_submit_request!!.setOnClickListener {
            service_notes = edt_note!!.text.toString().trim()

            if ( subservice_list_request.equals(SpinnerHintSubTypeString)){
                Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.REQUEST_SUBSERVICE_TYPE_ERROR_MSG).show()
            }else if (tv_req_cal_date.text.toString().isEmpty()){
                Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.REQUEST_T_DATE_ERROR_MSG).show()
            }
            else if (service_request_slot.equals(SpinnerHintString)) {
                Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.SLOTTIME_ERROR_MSG).show()
            }
            else{
                pd!!.show()
                submitRequest(service_notes!!)
            }


            /*pd!!.show()
            when {
                service_notes!!.isEmpty() -> Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.REQUEST_NOTE_ERROR_MSG).show()
                else -> submitRequest(service_notes!!)
            }*/
        }

        img_service_pic!!.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)

            } else {
                showPictureDialog()
            }
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Photo")
        val pictureDialogItems = arrayOf("From gallery", "From camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    saveImage(bitmap)
                    img_service_pic!!.setImageBitmap(bitmap)
                    //Toast.makeText(this@ServiceRequest, "Image Saved!", Toast.LENGTH_SHORT).show()

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(this@ServiceRequest, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            img_service_pic!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)

            //Toast.makeText(this@ServiceRequest, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        service_image_value = Base64.encodeToString(bytes.toByteArray(), Base64.NO_WRAP)
        Log.d("note_image",service_image_value.toString())

        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/Almutlaq"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showPictureDialog()
            }
        }

    }

    private fun getViewSubServiceslist(service_id: Int){
        if(connectioncheck!!.isNetworkAvailable()) {
            val getTenantViewSevServiceList= apiService!!.getTenantViewSevServiceList( service_id,"Bearer "+ login_response!!.data.token)

            getTenantViewSevServiceList.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ViewSubServiceListResponse> {

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(requestTenantViewSevServiceResponse: ViewSubServiceListResponse) {

                            var ResponseMessage = requestTenantViewSevServiceResponse.message

                            if (requestTenantViewSevServiceResponse.message_code == 1) {
                                Log.e("slot response", "success = " + requestTenantViewSevServiceResponse.toString())

                                if (requestTenantViewSevServiceResponse.data!!.size > 0) {

                                    /*var subservicelist: ArrayList<String>? =  ArrayList()*/

                                    for (i in 0 until  requestTenantViewSevServiceResponse.data!!.size) {

                                        var subservice_name : String = requestTenantViewSevServiceResponse.data!!.get(i).sub_typename.toString()
                                        var subservice_id : String = requestTenantViewSevServiceResponse.data!!.get(i).subtype_id.toString()

                                       // subservicelist!!.add(i,subservice_name+"|"+subservice_id)
                                        subservicelist!!.add(i,subservice_name)
                                        subservice_id_list!!.add(i,subservice_id)

                                    }
                                    subservicelist!!.add(0,SpinnerHintSubTypeString)
                                    subservice_id_list!!.add(0,"0")

                                    set_spinner_subservice_list_data(subservicelist!!)
                                }else{

                                    Commonfunctions.commonDialogue(this@ServiceRequest, StatusConstant.ALERT,StatusConstant.NO_DATA_MSG).show()
                                }
                            }else{
                                Log.e("servicelist_error", "error")
                                Commonfunctions.commonDialogue(this@ServiceRequest, StatusConstant.ALERT, ResponseMessage.toString()).show()
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

    private fun set_spinner_subservice_list_data(subservice: ArrayList<String>) {

        val sub_service_aa = ArrayAdapter(this, R.layout.spiiner_item_time, subservice)
        spSubType.adapter = sub_service_aa
        spSubType.setSelection(0)

    }

    private fun getRequestBookingSlotlist(dayname: String) {
        if(connectioncheck!!.isNetworkAvailable()) {
            val getTenantBookingSlot= apiService!!.getTenantBookingSlot( dayname,"Bearer "+ login_response!!.data.token)

            getTenantBookingSlot.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RequestBookingSlotResponse> {

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(requestBookingSlotResponse: RequestBookingSlotResponse) {

                            var ResponseMessage = requestBookingSlotResponse.message
                           // pd!!.dismiss()
                            if (requestBookingSlotResponse.message_code == 1) {
                                Log.e("slot response", "success = " + requestBookingSlotResponse.toString())

                                if (requestBookingSlotResponse.data!!.size > 0) {
                                    if (slottime!!.size > 0){
                                        slottime!!.clear()
                                    }

                                    for (i in 0 until  requestBookingSlotResponse.data!!.size) {
                                        var stime_start : String = requestBookingSlotResponse.data!!.get(i).start.toString()
                                        val s1 = SimpleDateFormat("HH:mm:ss")
                                        val sd = s1.parse(stime_start)
                                        val s2 = SimpleDateFormat("HH:mm")
                                        stime_start = s2.format(sd).toLowerCase()

                                        var stime_end : String = requestBookingSlotResponse.data!!.get(i).end.toString()
                                        val e1 = SimpleDateFormat("HH:mm:ss")
                                        val ed = e1.parse(stime_end)
                                        val e2 = SimpleDateFormat("HH:mm")
                                        stime_end = e2.format(ed).toLowerCase()

                                        slottime!!.add(i,stime_start +" - "+ stime_end)

                                    }
                                    slottime!!.add(0,SpinnerHintString)
                                    set_spinner_data(slottime!!)
                                    spTime!!.setBackgroundResource(R.drawable.edittxt_box)
                                    spTime.isEnabled = true
                                }else{
                                    Commonfunctions.commonDialogue(this@ServiceRequest, StatusConstant.ALERT,StatusConstant.NO_DATA_MSG).show()
                                }
                            }else{
                                Log.e("servicelist_error", "error")
                                Commonfunctions.commonDialogue(this@ServiceRequest, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                tv_req_cal_date.hint = "Choose date"
                                tv_req_cal_date.text = ""

                                spTime!!.setBackgroundResource(R.drawable.edittxt_box_gray)
                                spTime.isEnabled = false
                                slottime!!.add(0,SpinnerHintString)
                                set_spinner_data(slottime!!)
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

    private fun set_spinner_data(slottime: ArrayList<String>) {
        val aa = ArrayAdapter(this, R.layout.spiiner_item_time, slottime)
        spTime.adapter = aa
        spTime.setSelection(0)
    }

    private fun submitRequest(notes: String) {
        if(connectioncheck!!.isNetworkAvailable()) {
            val addbodyitem = BookServiceInput()
            addbodyitem.service = serviceid
            addbodyitem.booking_note = notes
            addbodyitem.booking_image = service_image_value  //service image
            addbodyitem.tenant_preferred_time = "" // set it as Blank data
            addbodyitem.slot_time = service_request_slot
            addbodyitem.subservice = sp_subservice_id
            addbodyitem.slot_date = tv_req_cal_date!!.text.toString().trim()

            val servicebookSuccessResponseObservable = apiService!!.sendServiceRequest( "Bearer "+ login_response!!.data.token,addbodyitem)

            servicebookSuccessResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RequestServiceResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onNext(requestServiceResponse: RequestServiceResponse)
                        {
                            ResponseMessage = requestServiceResponse.message
                            pd!!.dismiss()
                            Log.e("Booking response", "success = " + ResponseMessage.toString())
                            if (requestServiceResponse.message_code.equals("1")) {
                                edt_note!!.getText().clear()
                               // startActivity(Intent(this@ServiceRequest, ThankyouActivity::class.java))
                                val intent = Intent(this@ServiceRequest, ThankyouActivity::class.java)
                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG,"ServiceRequest")
                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT,ResponseMessage.toString())
                                startActivity(intent)
                               // Commonfunctions.commonDialogue(this@ServiceRequest, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            } else {
                                Commonfunctions.commonDialogue(this@ServiceRequest, StatusConstant.ALERT, ResponseMessage.toString()).show()
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