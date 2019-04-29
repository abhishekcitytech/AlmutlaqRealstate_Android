package com.almutlaq.realstate.utility

import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.widget.Button
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.dto.WorkHourModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Commonfunctions {

    private var sharedpreferences: SharedPreferences? = null
    /****************Check Network avialable*************************/
    fun checkConnectivity(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var ni = cm.activeNetworkInfo
        return ni != null && ni.isAvailable && ni.isConnected
    }
    /*******************************************************************/

    fun commonDialogue(context: Context, title: String, msg: String): AlertDialog {
        val context: Context = context
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    // context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    dialogInterface.cancel()

                })
        /*.setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialogInterface, i ->
            //dialogInterface.cancel();
            dialogInterface.dismiss()
        })*/
        return builder.create()
    }

    /********************Common dialog *****************/
    fun custom_dialog_ok(act: Activity, msg: String) {

        try {
            val dialog = Dialog(act, R.style.FullHeightDialog)
            dialog.setContentView(R.layout.custom_dialog_ok)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val txt = dialog.findViewById(R.id.heading) as TextView
            val ok = dialog.findViewById(R.id.btn_yes_one) as Button
            txt.text = msg
            ok.setOnClickListener { dialog.dismiss() }
            dialog.show()
            //            dialog_top(act,dialog,"");
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    @SuppressLint("CommitPrefEdits")
    fun set_intro_data(activity: Activity,flag_page : Boolean){
        sharedpreferences = activity.getSharedPreferences(StatusConstant.shared_prefference, Context.MODE_PRIVATE)
        val editor = sharedpreferences!!.edit()
        editor.putBoolean(StatusConstant.INTRO,flag_page)
        editor.apply()
    }

    fun get_intro_data(activity: Activity) : Boolean {
        sharedpreferences = activity.getSharedPreferences(StatusConstant.shared_prefference, Context.MODE_PRIVATE)
        val flag_page = sharedpreferences!!.getBoolean(StatusConstant.INTRO,false)
        return flag_page
    }


    @SuppressLint("CommitPrefEdits")
    fun set_login_data(activity: Activity, loginResponse: LoginResponse){
        sharedpreferences = activity.getSharedPreferences(StatusConstant.shared_prefference, Context.MODE_PRIVATE)
        val editor = sharedpreferences!!.edit()

        editor.putString(StatusConstant.TOKEN, loginResponse.data.token)
        editor.putString(StatusConstant.USER_TYPE, loginResponse.data.user_type)
        editor.putString(StatusConstant.CUSTOMER_ID, loginResponse.data.customer_id.toString())
        editor.putString(StatusConstant.CUSTOMER_NAME,loginResponse.data.customer_name)
        editor.putString(StatusConstant.CUSTOMER_PROFILE_PIC,loginResponse.data.customer_profilePic)
        editor.putString(StatusConstant.CUSTOMER_EMAIL,loginResponse.data.customer_email)
        editor.putString(StatusConstant.CUSTOMER_PHONE,loginResponse.data.customer_phone)
        editor.putString(StatusConstant.CUSTOMER_STREET_ADDRESS,loginResponse.data.customer_street_address)
        editor.putString(StatusConstant.CUSTOMER_BUILDING_NO,loginResponse.data.customer_building_no)
        editor.putString(StatusConstant.CUSTOMER_FLAT_NO,loginResponse.data.customer_flat_no)
        editor.putString(StatusConstant.CUSTOMER_ACCOUNT_STATUS,loginResponse.data.customer_account_status)
        editor.putString(StatusConstant.CUSTOMER_EXPIRY_DATE,loginResponse.data.customer_Expiry_Date)
        editor.putString(StatusConstant.OFFICE_SOS, loginResponse.data.Office_SOS)
        editor.putString(StatusConstant.PRIVATE_SOS, loginResponse.data.Private_SOS)
        editor.putString(StatusConstant.FORCE_CHANGE_PASSWORD, loginResponse.data.forcechangepassword)
        val gson =Gson()
        val json = gson.toJson(loginResponse.data.working_hour)
        editor.putString(StatusConstant.WORKING_HOURS,json.toString())
        editor.apply()

    }

    fun get_login_data(activity: Activity): LoginResponse {
        sharedpreferences = activity.getSharedPreferences(StatusConstant.shared_prefference, Context.MODE_PRIVATE)
//        val loginResponse =LoginResponse
        val loginResponse = LoginResponse()
        try
        {
            loginResponse.data.token = sharedpreferences!!.getString(StatusConstant.TOKEN, "")
            loginResponse.data.user_type = sharedpreferences!!.getString(StatusConstant.USER_TYPE, "")
            loginResponse.data.customer_id = sharedpreferences!!.getString(StatusConstant.CUSTOMER_ID, "")
            loginResponse.data.customer_name = sharedpreferences!!.getString(StatusConstant.CUSTOMER_NAME, "")
            loginResponse.data.customer_profilePic = sharedpreferences!!.getString(StatusConstant.CUSTOMER_PROFILE_PIC, "")
            loginResponse.data.customer_email = sharedpreferences!!.getString(StatusConstant.CUSTOMER_EMAIL, "")
            loginResponse.data.customer_phone = sharedpreferences!!.getString(StatusConstant.CUSTOMER_PHONE, "")
            loginResponse.data.customer_street_address = sharedpreferences!!.getString(StatusConstant.CUSTOMER_STREET_ADDRESS, "")
            loginResponse.data.customer_building_no = sharedpreferences!!.getString(StatusConstant.CUSTOMER_BUILDING_NO, "")
            loginResponse.data.customer_flat_no = sharedpreferences!!.getString(StatusConstant.CUSTOMER_FLAT_NO, "")
            loginResponse.data.customer_account_status = sharedpreferences!!.getString(StatusConstant.CUSTOMER_ACCOUNT_STATUS, "")
            loginResponse.data.customer_Expiry_Date = sharedpreferences!!.getString(StatusConstant.CUSTOMER_EXPIRY_DATE, "")
            loginResponse.data.Office_SOS = sharedpreferences!!.getString(StatusConstant.OFFICE_SOS, "")
            loginResponse.data.Private_SOS = sharedpreferences!!.getString(StatusConstant.PRIVATE_SOS, "")
            loginResponse.data.forcechangepassword = sharedpreferences!!.getString(StatusConstant.FORCE_CHANGE_PASSWORD, "")

            val gson = Gson()
            val json = sharedpreferences!!.getString(StatusConstant.WORKING_HOURS, "")
            val type = object : TypeToken<ArrayList<WorkHourModel>>() {}.type
            loginResponse.data.working_hour = gson.fromJson(json, type)
        }catch(e: Exception){
            println("Error"+e.toString())
        }

        return loginResponse

    }

    /********************loader */
    fun custom_loader(act: Activity, msg: String): Dialog {
        val dialog = Dialog(act, R.style.FullHeightDialog)
        dialog.setContentView(R.layout.custom_loader)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val txt = dialog.findViewById(R.id.txt_msg) as TextView
        txt.text = msg
        return dialog
    }

    /////for calendar date////
    fun setdate(date: Int): String {
        return String.format("%02d", date)
    }


    fun checkappinBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val appProcessInfo = ActivityManager.RunningAppProcessInfo()
            ActivityManager.getMyMemoryState(appProcessInfo)
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                isInBackground = false
            } else {
                val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                // App is foreground, but screen is locked, so show notification
                isInBackground = km.inKeyguardRestrictedInputMode()
            }
        }

        return isInBackground
    }



}