package com.almutlaq.realstate.base

import com.almutlaq.realstate.dto.CsCalendardatesdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CsCalendardatesResponse (

        @SerializedName("message")
        @Expose
        val message: String ="",

        @SerializedName("message_code")
        @Expose
        val message_code: String = "",

        @SerializedName("data")
        var data: ArrayList<CsCalendardatesdata> = ArrayList()

)