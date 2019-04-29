package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LogOutResponse (

        @SerializedName("message")
        @Expose
        var message: String ="",

        @SerializedName("message_code")
        @Expose
        var message_code: String = ""


)