package com.almutlaq.realstate.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.almutlaq.realstate.R
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.LoginInput
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.ForgotPassworddialogue
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("IMPLICIT_CAST_TO_ANY", "UNUSED_VALUE")
class Login : AppCompatActivity() {

    private var pd: Dialog? = null
   // private var _pref: Pref? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        performclick()
    }

    private fun init() {
      //  _pref = Pref(this)
        connectioncheck =  NetworkConnetionCheck(this)
        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        StatusConstant.SERVER_MSG =""

        ////for firebase token generation
/*
        FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener(this@Login) { instanceIdResult ->
                    val mToken = instanceIdResult.token
                    Log.e("fcm token success: ",mToken)
                }.addOnFailureListener(this@Login){
                    Log.e("fcm token failure: ","failed")
                }*/
    }

    private fun performclick() {

        btn_login.setOnClickListener {
            val usernm: String = edt_id.text.toString().trim()
            val userpw: String = edt_password.text.toString().trim()
            val fcmtoken = getRegIdInPref()
            Log.e("fcmtoken_login",fcmtoken)
            if (usernm.length == 0){
                Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.USER_ERROR_MSG).show()
            }else if (!isValidEmail(usernm)){
                Commonfunctions.custom_dialog_ok(this, StatusConstant.EMAIL_VALID_ERROR_MSG)
            }
            else if(userpw.length == 0){
                Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.PASSWORD_ERROR_MSG).show()
            }else{
                pd!!.show()
                callLogindata(usernm,userpw,fcmtoken)
            }
            /*pd!!.show()
            when {
                usernm.isEmpty() -> Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.USER_ERROR_MSG).show()
                userpw.isEmpty() -> Commonfunctions.commonDialogue(this, StatusConstant.ALERT, StatusConstant.PASSWORD_ERROR_MSG).show()

                else -> callLogindata(usernm,userpw)

            }*/
        }

        txt_reset.setOnClickListener {

            val mydialog = ForgotPassworddialogue(this)
            mydialog.show()
        }
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun callLogindata(usernm: String, userpw: String, fcmtoken: String?) =
            if(connectioncheck!!.isNetworkAvailable()) {
                val additem = LoginInput()
                additem.username = usernm
                additem.password = userpw
                additem.device_type = "A"
                additem.device_id = fcmtoken

                val loginsuccessRespeObservable = apiService!!.sendLoginData(additem)
                loginsuccessRespeObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<LoginResponse> {
                            override fun onSubscribe(d: Disposable) {

                            }
                            override fun onNext(loginResponse: LoginResponse)
                            {
                                ResponseMessage = loginResponse.message
                                StatusConstant.SERVER_MSG = ""
                                pd!!.dismiss()
                                if (loginResponse.message_code.equals("1")) {
                                    if (loginResponse.data.forcechangepassword.equals("1")) {
                                        Commonfunctions.set_login_data(this@Login, loginResponse)
                                        startActivity(Intent(this@Login, DashboardNavigation::class.java))
                                        finish()
                                    }else{
                                        Commonfunctions.set_login_data(this@Login, loginResponse)
                                        startActivity(Intent(this@Login, ForceChngPassword::class.java))
                                        finish()
                                    }
                                } else if(loginResponse.message_code.equals("2")){
                                    val note: String = loginResponse.note
                                    StatusConstant.SERVER_MSG = note
                                    startActivity(Intent(this@Login, DeactivationActivity()::class.java))
                                    //Commonfunctions.commonDialogue(this@Login, StatusConstant.ALERT, note).show()
                                } else {
                                    Commonfunctions.commonDialogue(this@Login, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                    edt_id.getText().clear()
                                    edt_password.getText().clear()
                               }
                            }
                            override fun onError(e: Throwable) {
                                pd!!.dismiss()
                                edt_id.getText().clear()
                                edt_password.getText().clear()
                                Commonfunctions.commonDialogue(this@Login, StatusConstant.ALERT, StatusConstant.USER_LOGIN_ERROR_MSG).show()
                                e.printStackTrace()
                            }
                            override fun onComplete() {

                            }
                        })
            }else{
                connectioncheck!!.getNetworkActiveAlert().show()
            }

    private fun getRegIdInPref(): String? {
        val pref = applicationContext.getSharedPreferences(StatusConstant.SHARED_PREF, 0)
        return pref!!.getString(StatusConstant.FCM_TOKEN,"")
    }


}