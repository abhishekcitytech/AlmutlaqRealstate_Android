package com.almutlaq.realstate.utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class Pref {

        private lateinit var _pref: SharedPreferences
        private val PREF_FILE = "almutlaq.in"
        private lateinit var _editorPref: SharedPreferences.Editor


        @SuppressLint("CommitPrefEdits")
        constructor(context: Context){
            _pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
            _editorPref = _pref.edit()
        }

    fun saveUserName(user_name: String)  {
            _editorPref.putString("user_name", user_name)
            _editorPref.commit()
        }

        fun getUsername(): String {
            return _pref.getString("user_name", "")
        }

    fun saveUserPw(user_pw: String)  {
        _editorPref.putString("user_pw", user_pw)
        _editorPref.commit()
    }

    fun getUserPw(): String {
        return _pref.getString("user_pw", "")
    }

    fun saveUserType(user_type: String)  {
        _editorPref.putString("user_type", user_type)
        _editorPref.commit()
    }

    fun getUserType(): String {
        return _pref.getString("user_type", "")
    }

    fun saveUserEmail(user_mail: String)  {
        _editorPref.putString("user_mail", user_mail)
        _editorPref.commit()
    }

    fun getUserEmail(): String {
        return _pref.getString("user_mail", "")
    }
    fun saveUserImg(user_img: String)  {
        _editorPref.putString("user_img", user_img)
        _editorPref.commit()
    }

    fun getUserImg(): String {
        return _pref.getString("user_img", "")
    }

    fun saveCustomername(cname: String)  {
        _editorPref.putString("cname", cname)
        _editorPref.commit()
    }

    fun getCustomername(): String {
        return _pref.getString("cname", "")
    }

    fun saveCustomerPhone(c_phone: String)  {
        _editorPref.putString("c_phone", c_phone)
        _editorPref.commit()
    }

    fun getCustomerPhone(): String {
        return _pref.getString("c_phone", "")
    }

    fun saveStreetaddress(streetaddr: String)  {
        _editorPref.putString("streetaddr", streetaddr)
        _editorPref.commit()
    }

    fun getStreetaddress(): String {
        return _pref.getString("streetaddr", "")
    }

    fun saveBuildingno(buildingno: String)  {
        _editorPref.putString("buildingno", buildingno)
        _editorPref.commit()
    }

    fun getBuildingno(): String {
        return _pref.getString("buildingno", "")
    }

    fun saveFlatno(flatno: String)  {
        _editorPref.putString("flatno", flatno)
        _editorPref.commit()
    }

    fun getFlatno(): String {
        return _pref.getString("flatno", "")
    }

    fun saveAccountStatus(ac_status: String)  {
        _editorPref.putString("ac_status", ac_status)
        _editorPref.commit()
    }

    fun getAccountStatus(): String {
        return _pref.getString("ac_status", "")
    }

    fun saveExpirydate(expiry_date: String)  {
        _editorPref.putString("expiry_date", expiry_date)
        _editorPref.commit()
    }

    fun getExpirydate(): String {
        return _pref.getString("expiry_date", "")
    }

    fun saveAccesstoken(access_token: String)  {
        _editorPref.putString("access_token", access_token)
        _editorPref.commit()
    }

    fun getAccesstoken(): String {
        return _pref.getString("access_token", "")
    }

}
