package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.activity.ThankyouActivity
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.RegComplaintInput
import com.almutlaq.realstate.base.RegComplaintResponse
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_drawer.*

@SuppressLint("ValidFragment")
class RegisterComplaintFragment  @SuppressLint("ValidFragment") constructor
(act: Activity): Fragment()  {
    val activity = act
  //  var hdr: String="Register Complaint";

    var rdselectComplaint : RadioGroup? = null
    var rdComplaint : RadioButton? = null
    var rdSuggestion : RadioButton? = null
    var btnRegComplaintSubmit : Button? = null
    var et_subject : EditText? = null
    var etNote : EditText? = null
    var login_response: LoginResponse? = null

  //  private var _pref: Pref? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null
    private var pd: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater.inflate(R.layout.register_complaint_fragment, container, false)
        init(rootview)
        performclick()
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

    @SuppressLint("ResourceAsColor")
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
      //  if (activity is DashboardNavigation) {
            (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
            (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
            } else {
                (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
            }
       /* }else{
            activity.toolbartitle.setText(hdr)
            activity.toolbartitle.setTextColor(getResources().getColor(R.color.white))
        }*/
    }

    @SuppressLint("ResourceAsColor")
    private fun init(rootview: View?) {

      //  if (activity is DashboardNavigation) {
            (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
            (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white, getActivity()!!.getTheme()))
            } else {
                (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.getResources().getColor(android.R.color.white))
            }
      /*  }else{
            activity.toolbartitle.setText(hdr)
            activity.toolbartitle.setTextColor(getResources().getColor(R.color.white))
        }
*/
        rdselectComplaint = rootview!!.findViewById(R.id.radioGroup1)
        rdComplaint = rootview.findViewById(R.id.radio0)
        rdSuggestion = rootview.findViewById(R.id.radio1)
        btnRegComplaintSubmit = rootview.findViewById(R.id.btnRegComplaintSubmit)
        et_subject = rootview.findViewById(R.id.et_subject)
        etNote = rootview.findViewById(R.id.etNote)

        login_response = Commonfunctions.get_login_data(activity)

        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)

    }

    private fun performclick() {
/*
        et_subject!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (et_subject!!.text.toString().trim().length > 85) {
                    *//*val str = et_subject!!.getText().toString().substring(0, 85)
                    et_subject!!.setText(str)
                    et_subject!!.setSelection(str.length)*//*

                    Toast.makeText(getActivity(),"subject include maximum 85 characters only",Toast.LENGTH_LONG).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {}
        })*/

        btnRegComplaintSubmit!!.setOnClickListener {

            val selectedId = rdselectComplaint!!.getCheckedRadioButtonId()
            Log.e("selectedrdbutton",selectedId.toString())
            var complain_type : String? = null

            if (selectedId == R.id.radio0){
               complain_type = "C"
            }else if (selectedId == R.id.radio1){
                complain_type = "S"
            }

            Log.e("CTYPE",complain_type)

            val complain_subject : String = et_subject!!.text.toString().trim()
            val complain_note : String = etNote!!.text.toString().trim()

            if (complain_subject.length == 0)
                Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, StatusConstant.COMPLAIN_SUB_ERROR_MSG).show()
            else if (complain_note.length == 0){
                Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, StatusConstant.COMPLAIN_NOTE_ERROR_MSG).show()
            }
            else {
                     pd!!.show()
                     sendcomplaintdata(complain_subject,complain_note,complain_type)
            }

        }


    }

    private fun sendcomplaintdata(complain_subject: String, complain_note: String, complain_type: String?) {
        if(connectioncheck!!.isNetworkAvailable()) {

            val additem = RegComplaintInput()
            additem.complain_subject = complain_subject
            additem.complain_note = complain_note
            additem.complain_type = complain_type

            val regsuccessRespeObservable = apiService!!.addComplaint("Bearer"+ login_response!!.data.token,additem)

            regsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<RegComplaintResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onNext(response: RegComplaintResponse)
                        {
                            //  progress_changepw!!.visibility = View.GONE
                            Log.d("resp", "onNext: get reg comp response " + response.message)
                            ResponseMessage = response.message
                            pd!!.dismiss()
                            if (response.message_code.equals("1")) {

                                val intent = Intent(activity, ThankyouActivity::class.java)
                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG,"RegisterComplaintFragment")
                                intent.putExtra(StatusConstant.INTENT_THANKYOU_MSG_TXT,ResponseMessage.toString())
                                activity.startActivity(intent)

                            } else {
                                Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
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