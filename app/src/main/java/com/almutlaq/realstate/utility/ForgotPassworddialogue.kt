package com.almutlaq.realstate.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import com.almutlaq.realstate.R
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.dto.ForgetPasswordResponse
import com.almutlaq.realstate.dto.ForgotPasswordInput
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.forgot_password_dialogue.*

class ForgotPassworddialogue (context: Context) : Dialog(context){

    var contxt = context
    private var pd: Dialog? = null
    private var _pref: Pref? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null
    var ResponseMessage : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.setTheme(R.style.Custom_dialogue_theme)
        setContentView(R.layout.forgot_password_dialogue)
        init()
        performclick()
    }

    private fun init() {
        _pref = Pref(context)
        pd = Commonfunctions.custom_loader(contxt as Activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(context)
        apiService = ApiClient!!.getClient().create(ApiInterface::class.java)
    }

    private fun performclick() {

        btn_forgotp_submit.setOnClickListener {
            val emailid : String = edtxt_forgotpwd.text.toString().trim()

            if (emailid.isEmpty()) {
                Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.EMAIL_ERROR_MSG)
            } else if (!isValidEmail(emailid)){
                Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.EMAIL_VALID_ERROR_MSG)
            } else{
                callForgotPwdApi(emailid)
            }

        }
        btn_fp_Cancel.setOnClickListener { dismiss() }
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun callForgotPwdApi(emailid: String) {
        if(connectioncheck!!.isNetworkAvailable()) {

            pd!!.show()
            val additem = ForgotPasswordInput()
            additem.email = emailid

            val forgotpasswordsuccessRespeObservable = apiService!!.sendForgotPwdData(additem)

            forgotpasswordsuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ForgetPasswordResponse> {

                        override fun onNext(forgetPasswordResponse: ForgetPasswordResponse) {

                            ResponseMessage = forgetPasswordResponse.message
                            pd!!.dismiss()
                            if (forgetPasswordResponse.message_code == 1) {

                                Commonfunctions.custom_dialog_ok(contxt as Activity, ResponseMessage.toString())
                               // Commonfunctions.custom_dialog_ok(contxt as Activity, StatusConstant.CHANGE_PASSWORD_SUCCESS_MSG)
                                edtxt_forgotpwd.getText().clear()
                                dismiss()
                               // context.startActivity(Intent(context, ThankyouActivity::class.java))
                            }else {
                                Commonfunctions.custom_dialog_ok(contxt as Activity,  StatusConstant.FORGOT_PASSWORD_FAILURE_MSG)
                               // Commonfunctions.custom_dialog_ok(contxt as Activity,  ResponseMessage.toString())
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