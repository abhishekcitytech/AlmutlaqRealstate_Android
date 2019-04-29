package com.almutlaq.realstate.base

import com.almutlaq.realstate.dto.CustomerServiceListdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomerServiceListResponse (

        @SerializedName("message")
        @Expose
        val message: String ="",

        @SerializedName("message_code")
        @Expose
        val message_code: String = "",

        @SerializedName("data")
        var data: ArrayList<CustomerServiceListdata> = ArrayList()

)