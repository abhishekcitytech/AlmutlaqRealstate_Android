package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class historyListdata (

        @SerializedName("labour_name")
        @Expose
        val labour_name: String = "",

        @SerializedName("note")
        @Expose
        val note: String = "",

        @SerializedName("booking_update_date")
        @Expose
        val booking_update_date: String = "",

        @SerializedName("job_completion")
        @Expose
        val job_completion: String = ""

)
