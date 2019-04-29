package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WorkHourModel(

        @SerializedName("dailytime_day")
        @Expose
        val dailytime_day: String,

        @SerializedName("dailytime_status")
        @Expose
        val dailytime_status: String,

        @SerializedName("dailytime_opening")
        @Expose
        val dailytime_opening: String,

        @SerializedName("dailytime_closing")
        @Expose
        val dailytime_closing: String,

        @SerializedName("dailytime_opening_2")
        @Expose
        val dailytime_opening_2: String,

       @SerializedName("dailytime_closing_2")
       @Expose
       val dailytime_closing_2: String

)
