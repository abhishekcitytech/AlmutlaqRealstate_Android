package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data  class ServiceListData (

    @SerializedName("service_id")
    @Expose
    var service_id: Int = 0,

    @SerializedName("service_name")
    @Expose
    var service_name: String = "",

    @SerializedName("service_image")
    @Expose
    var service_image: String = ""
)