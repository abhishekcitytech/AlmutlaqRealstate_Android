package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CSrescheduletimedata (

        @SerializedName("start")
        @Expose
        var start: String = "",

        @SerializedName("end")
        @Expose
        var end: String = ""

)
