package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Databean (

        @SerializedName("token")
        @Expose
        var token: String = "",

        @SerializedName("user_type")
        @Expose
        var user_type: String = "",

        @SerializedName("customer_id")
        @Expose
        var customer_id: String = "",

        @SerializedName("customer_name")
        @Expose
        var customer_name: String =  "",

        @SerializedName("customer_profilePic")
        @Expose
        var customer_profilePic: String =  "",

        @SerializedName("customer_email")
        @Expose
        var customer_email: String =  "",

        @SerializedName("customer_phone")
        @Expose
        var customer_phone: String =  "",

        @SerializedName("customer_street_address")
        @Expose
        var customer_street_address: String =  "",

        @SerializedName("customer_building_no")
        @Expose
        var customer_building_no: String =  "",

        @SerializedName("customer_flat_no")
        @Expose
        var customer_flat_no: String =  "",

        @SerializedName("forcechangepassword")
        @Expose
        var forcechangepassword: String =  "",

        @SerializedName("customer_account_status")
        @Expose
        var customer_account_status: String =  "",

        @SerializedName("customer_Expiry_Date")
        @Expose
        var customer_Expiry_Date: String =  "",

        @SerializedName("Office_SOS")
        @Expose
        var Office_SOS: String =  "",

        @SerializedName("Private_SOS")
        @Expose
        var Private_SOS: String =  "",

        @SerializedName("working_hours")
        var working_hour: ArrayList<WorkHourModel> = ArrayList()
)