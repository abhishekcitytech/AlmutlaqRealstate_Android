package com.almutlaq.realstate.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.almutlaq.realstate.R
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.ChangePasswordInput
import com.almutlaq.realstate.dto.ChangePasswordResponse
import com.almutlaq.realstate.dto.LoginResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.changepassword_dialogue.*

class ChangePassworddialogue (context: Context) : Dialog(context){

    var contxt = context
    private var pd: Dialog? = null
    private var _pref: Pref? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null
    var login_response: LoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.changepassword_dialogue)
        init()
        performclick()
    }

    private fun init() {
        _pref = Pref(context)
        pd = Commonfunctions.custom_loader(contxt as Activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(context)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
        login_response = Commonfunctions.get_login_data(contxt as Activity)
    }

    private fun performclick() {

        btn_cp_submit.setOnClickListener {
            val new_pwd : String = edtxt_cp_newpwd.text.toString().trim()
            val re_pwd : String = edtxt_cp_compwd.text.toString().trim()
            if (new_pwd.length == 0)
                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.NEW_PASSWORD_ERROR_MSG).show()
                    Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.NEW_PASSWORD_ERROR_MSG)
            else if (re_pwd.length == 0)
                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.RE_PASSWORD_ERROR_MSG).show()
                    Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.RE_PASSWORD_ERROR_MSG)
            else if (!new_pwd.equals(re_pwd)) {
                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.MISMATCH_PASSWORD_ERROR_MSG).show()
                Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.MISMATCH_PASSWORD_ERROR_MSG)
                edtxt_cp_newpwd.getText().clear()
                edtxt_cp_compwd.getText().clear()
            }
            else{
                //Log.d("new_pwd & re_pwd", new_pwd + " and " +re_pwd)
                callChangePwdApi(new_pwd,re_pwd)
            }
            //dismiss()
        }
        btnCancel.setOnClickListener { dismiss() }
    }

    private fun callChangePwdApi(new_pwd: String, re_pwd: String) {
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()
            val additem = ChangePasswordInput()
            additem.password = new_pwd
            additem.confirm_password = re_pwd

            val changepasswordsuccessRespeObservable = apiService!!.sendChangePwdData( "Bearer"+  login_response!!.data.token,additem)

            changepasswordsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ChangePasswordResponse> {

                        override fun onNext(changePasswordResponse: ChangePasswordResponse) {

                            ResponseMessage = changePasswordResponse.message
                            pd!!.dismiss()
                            if (changePasswordResponse.message_code == 1) {
                                val new_pwd = changePasswordResponse.data!!.new_password
                                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, StatusConstant.CHANGE_PASSWORD_SUCCESS_MSG).show()
                                Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.CHANGE_PASSWORD_SUCCESS_MSG)
                                edtxt_cp_newpwd.getText().clear()
                                edtxt_cp_compwd.getText().clear()
                                dismiss()
                            }else {
                                //Commonfunctions.commonDialogue(context, StatusConstant.ALERT, ResponseMessage.toString()).show()
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