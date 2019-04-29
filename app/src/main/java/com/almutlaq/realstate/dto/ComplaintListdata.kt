package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComplaintListdata (

        @SerializedName("complain_id")
        @Expose
        var complain_id: Int = 0,

        @SerializedName("user_id")
        @Expose
        var user_id: Int = 0,

        @SerializedName("complain_subject")
        @Expose
        var complain_subject: String = "",

        @SerializedName("complain_note")
        @Expose
        var complain_note: String = "",

        @SerializedName("complain_type")
        @Expose
        var complain_type: String = "",

        @SerializedName("complain_status")
        @Expose
        var complain_status: String = "",

        @SerializedName("complain_close_note")
        @Expose
        var complain_close_note: String = "",

        @SerializedName("complain_close_date")
        @Expose
        var complain_close_date: String = "",

        @SerializedName("created_at")
        @Expose
        var created_at: String = "",

        @SerializedName("updated_at")
        @Expose
        var updated_at: String = ""
)
