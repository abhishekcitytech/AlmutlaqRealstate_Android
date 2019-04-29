package com.almutlaq.realstate.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegComplaintResponse (
    @SerializedName("message")
    @Expose
    val message: String ="",

    @SerializedName("message_code")
    @Expose
    val message_code: String = ""

)