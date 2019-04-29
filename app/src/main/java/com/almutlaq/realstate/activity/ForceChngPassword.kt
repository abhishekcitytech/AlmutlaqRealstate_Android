package com.almutlaq.realstate.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.almutlaq.realstate.R
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.ChangePasswordInput
import com.almutlaq.realstate.dto.ChangePasswordResponse
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.force_change_password.*

@Suppress("IMPLICIT_CAST_TO_ANY", "UNUSED_VALUE")
class ForceChngPassword : AppCompatActivity() {
    private var pd: Dialog? = null
   // private var _pref: Pref? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null
    var login_response: LoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.force_change_password)
        init()
        performclick()
    }

    private fun init() {
      //  _pref = Pref(this)
        connectioncheck =  NetworkConnetionCheck(this)
        pd = Commonfunctions.custom_loader(this, StatusConstant.wait_msg)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        StatusConstant.SERVER_MSG =""
        login_response = Commonfunctions.get_login_data(this)
    }

    private fun performclick() {

        btn_force_cp!!.setOnClickListener {
            val new_pwd : String = edtxt_fcp_newpwd.text.toString().trim()
            val re_pwd : String = edtxt_fcp_compwd.text.toString().trim()
            if (new_pwd.length == 0)
            //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.NEW_PASSWORD_ERROR_MSG).show()
                Commonfunctions.custom_dialog_ok(this, StatusConstant.NEW_PASSWORD_ERROR_MSG)
            else if (re_pwd.length == 0)
            //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.RE_PASSWORD_ERROR_MSG).show()
                Commonfunctions.custom_dialog_ok(this, StatusConstant.RE_PASSWORD_ERROR_MSG)
            else if (!new_pwd.equals(re_pwd)) {
                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.MISMATCH_PASSWORD_ERROR_MSG).show()
                Commonfunctions.custom_dialog_ok(this, StatusConstant.MISMATCH_PASSWORD_ERROR_MSG)
                edtxt_fcp_newpwd.getText().clear()
                edtxt_fcp_compwd.getText().clear()
            }
            else{

                callforceChangePwdApi(new_pwd,re_pwd)
            }
        }


    }

    private fun callforceChangePwdApi(new_pwd: String, re_pwd: String) {
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()
            val additem = ChangePasswordInput()
            additem.password = new_pwd
            additem.confirm_password = re_pwd

            val changepasswordsuccessRespeObservable = apiService!!.sendforcechngPwdData( "Bearer"+  login_response!!.data.token,additem)

            changepasswordsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ChangePasswordResponse> {

                        override fun onNext(changePasswordResponse: ChangePasswordResponse) {

                            ResponseMessage = changePasswordResponse.message
                            pd!!.dismiss()
                            if (changePasswordResponse.message_code == 1) {

                                val new_pwd = changePasswordResponse.data!!.new_password
                                startActivity(Intent(this@ForceChngPassword, DashboardNavigation::class.java))
                                finish()
                              //  Commonfunctions.custom_dialog_ok(this@ForceChngPassword, StatusConstant.CHANGE_PASSWORD_SUCCESS_MSG)
                                edtxt_fcp_newpwd.getText().clear()
                                edtxt_fcp_compwd.getText().clear()

                            }else {
                                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, ResponseMessage.toString()).show()
                                Commonfunctions.custom_dialog_ok(this@ForceChngPassword,  ResponseMessage.toString())
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