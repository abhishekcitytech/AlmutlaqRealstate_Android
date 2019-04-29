package com.almutlaq.realstate.base

import com.almutlaq.realstate.dto.NotifyListdata
import com.almutlaq.realstate.dto.TServiceListdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationListResponse (

        @SerializedName("message")
        @Expose
        val message: String ="",

        @SerializedName("message_code")
        @Expose
        val message_code: String = "",

        @SerializedName("data")
        var data: ArrayList<NotifyListdata> = ArrayList()

)