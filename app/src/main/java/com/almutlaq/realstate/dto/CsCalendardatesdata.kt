package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CsCalendardatesdata (

        @SerializedName("tenant_slot_date")
        @Expose
        val tenant_slot_date: String = ""

)
