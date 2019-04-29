package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotifyListdata (

        @SerializedName("posted_date")
        @Expose
        var posted_date: String = "",

        @SerializedName("posted_time")
        @Expose
        val posted_time: String = "",

        @SerializedName("message_text")
        @Expose
        val message_text: String = "",

        @SerializedName("message_subject")
        @Expose
        val message_subject: String = ""
)
