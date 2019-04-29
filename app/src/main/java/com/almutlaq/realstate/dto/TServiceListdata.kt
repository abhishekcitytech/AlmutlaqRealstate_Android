package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TServiceListdata (

        @SerializedName("service_boooking_id")
        @Expose
        var service_boooking_id: Int = 0,

        @SerializedName("user_id")
        @Expose
        var user_id: Int = 0,

        @SerializedName("service_id")
        @Expose
        var service_id: Int = 0,

        @SerializedName("subservice_id")
        @Expose
        var subservice_id: Int = 0,

        @SerializedName("booking_status")
        @Expose
        var booking_status: String = "",

        @SerializedName("booking_notes")
        @Expose
        val booking_notes: String = "",

        @SerializedName("tenant_slot_time")
        @Expose
        val tenant_slot_time: String = "",

        @SerializedName("tenant_slot_date")
        @Expose
        val tenant_slot_date: String = "",

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

        @SerializedName("booking_accepted")
        @Expose
        val booking_accepted: String = "",

        @SerializedName("acknowledge_slot_date")
        @Expose
        val acknowledge_slot_date: String = "",

        @SerializedName("acknowledge_requested_date")
        @Expose
        val acknowledge_requested_date: String = "",

        @SerializedName("booking_acknowledge_date")
        @Expose
        val booking_acknowledge_date: String = "",

        @SerializedName("booking_acknowledge_time")
        @Expose
        val booking_acknowledge_time: String = "",

       @SerializedName("csk_slot_time")
        @Expose
        val csk_slot_time: String = "",

        @SerializedName("booking_acknowledge_note")
        @Expose
        val booking_acknowledge_note: String = "",

        @SerializedName("booking_completion_percentage")
        @Expose
        val booking_completion_percentage: String = "",

        @SerializedName("tenant_rating")
        @Expose
        val tenant_rating: String = "",

        @SerializedName("tenant_rating_note")
        @Expose
        val tenant_rating_note: String = "",

        @SerializedName("tenant_rating_date")
        @Expose
        val tenant_rating_date: String = "",

        @SerializedName("created_at")
        @Expose
        val created_at: String = "",

        @SerializedName("subservice_name")
        @Expose
        val subservice_name: String = "",

        @SerializedName("service_name")
        @Expose
        val service_name: String = "",

        @SerializedName("customer_service_name")
        @Expose
        val customer_service_name: String = "",

        @SerializedName("building_address")
        @Expose
        val building_address: String = "",

        @SerializedName("building_no")
        @Expose
        val building_no: String = "",

        @SerializedName("flat_no")
        @Expose
        val flat_no: String = "",

        @SerializedName("booking_image")
        @Expose
        val booking_image: String = "",

        @SerializedName("booking_update_note")
        var booking_update_note: ArrayList<historyListdata> = ArrayList()

)
