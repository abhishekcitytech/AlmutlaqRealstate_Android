package com.almutlaq.realstate.dto

import java.io.Serializable

class StatusListModel : Serializable {
  //  constructor()
    var servicetype: String? = null
    var serviceid : Int? = null
    var subservice_id: Int? = null
    var service_boooking_id : Int? = null
    var statustitle: String? = null
    var statusrequest: String? = null
    var statusdate: String? = null
    var statusflatno: String? = null
    var statusbuildigno: String? = null
    var statusaddress: String? = null

    var bookingAcknowledge: String? = null
    var booking_accepted: String? = null
    var acknowledge_slot_date: String? = null
    var acknowledge_requested_date: String? = null
    var bookingImage: String? = null
    var tenant_slot_time: String? = null
    var tenant_slot_date: String? = null
    var csk_slot_time: String? = null
    var booking_acknowledge_note: String? = null
    var tenant_rating : Float? = null
    var tenant_rating_note : String? = null
    var tenant_rating_date : String? = null
    var booking_completion_percentage : String? = null
    var subservice: String? = null
    var booking_update_note: ArrayList<Historymodel> = ArrayList()

}