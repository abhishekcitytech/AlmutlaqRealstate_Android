package com.almutlaq.realstate.base

import com.almutlaq.realstate.dto.CSEnginnerListdata
import com.almutlaq.realstate.dto.CSackdatetimeListdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CSengineernameResponse (

        @SerializedName("message")
        @Expose
        val message: String ="",

        @SerializedName("message_code")
        @Expose
        val message_code: String = "",

        @SerializedName("data")
        var data: ArrayList<CSEnginnerListdata> = ArrayList()

)