package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginResponse (

        @SerializedName("message")
        @Expose
        var message: String ="",

        @SerializedName("note")
        @Expose
        var note: String ="",

        @SerializedName("message_code")
        @Expose
        var message_code: String = "",

        @SerializedName("data")
        @Expose
        val data: Databean = Databean()

)