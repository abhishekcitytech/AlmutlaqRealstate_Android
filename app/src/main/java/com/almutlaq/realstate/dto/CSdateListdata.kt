package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CSdateListdata (

        @SerializedName("service_boooking_id")
        @Expose
        var service_boooking_id: Int = 0,

        @SerializedName("user_id")
        @Expose
        var user_id: Int = 0,

        @SerializedName("booking_status")
        @Expose
        var booking_status: Int = 0,

        @SerializedName("booking_notes")
        @Expose
        val booking_notes: String = "",

        @SerializedName("labour_name")
        @Expose
        val labour_name: String = "",

        @SerializedName("booking_requested_date")
        @Expose
        val booking_requested_date: String = "",

        @SerializedName("booking_requested_time")
        @Expose
        val booking_requested_time: String = "",


        @SerializedName("booking_acknowledge_by")
        @Expose
        val booking_acknowledge_by: String = "",


        @SerializedName("booking_acknowledge")
        @Expose
        val booking_acknowledge: String = "",


        @SerializedName("booking_acknowledge_date")
        @Expose
        val booking_acknowledge_date: String = "",

        @SerializedName("booking_acknowledge_time")
        @Expose
        val booking_acknowledge_time: String = "",

        @SerializedName("booking_acknowledge_note")
        @Expose
        val booking_acknowledge_note: String = "",

        @SerializedName("tenant_name")
        @Expose
        val tenant_name: String = "",

        @SerializedName("building_address")
        @Expose
        val building_address: String = "",

        @SerializedName("building_no")
        @Expose
        val building_no: String = "",

        @SerializedName("flat_no")
        @Expose
        val flat_no: String = "",

        @SerializedName("service_name")
        @Expose
        val service_name: String = "",

        @SerializedName("booking_image")
        @Expose
        val booking_image: String = "",

        @SerializedName("booking_completion_percentage")
        @Expose
        val booking_completion_percentage: String = ""



)
