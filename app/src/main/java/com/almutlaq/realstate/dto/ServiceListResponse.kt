package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 data class ServiceListResponse (

    @SerializedName("message")
    @Expose
    val message: String ="",

    @SerializedName("message_code")
    @Expose
    val message_code: String = "",

    @SerializedName("customer_is_active")
    @Expose
    val customer_is_active: Boolean = false,

    @SerializedName("data")
    var data: ArrayList<ServiceListData> = ArrayList()

)