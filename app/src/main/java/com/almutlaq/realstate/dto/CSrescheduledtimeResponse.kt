package com.almutlaq.realstate.base

import com.almutlaq.realstate.dto.CSackdatetimeListdata
import com.almutlaq.realstate.dto.CSrescheduletimedata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CSrescheduledtimeResponse (

        @SerializedName("message")
        @Expose
        val message: String ="",

        @SerializedName("message_code")
        @Expose
        val message_code: String = "",

        @SerializedName("data")
        var data: ArrayList<CSrescheduletimedata> = ArrayList()

)