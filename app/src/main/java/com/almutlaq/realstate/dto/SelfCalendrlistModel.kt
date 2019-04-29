package com.almutlaq.realstate.dto

class SelfCalendrlistModel {

    constructor(calstatustitle: String?, calstatusdate: String?, calstatusaddress: String?) {
        this.calstatustitle = calstatustitle
        this.calstatusdate = calstatusdate
        this.calstatusaddress = calstatusaddress
    }
    constructor()

    var calstatustitle: String? = null
    var calstatusdate: String? = null
    var calstatusaddress: String? = null
    var booking_acknowledge: String? = null
    var service_boooking_id: Int? = null
    var booking_status: Int? = null

}