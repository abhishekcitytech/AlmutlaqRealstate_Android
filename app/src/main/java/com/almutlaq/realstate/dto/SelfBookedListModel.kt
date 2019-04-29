package com.almutlaq.realstate.dto

class SelfBookedListModel {

    constructor(statustitle: String?, statusrequest: String?, statusdate: String?, statusaddress: String?) {
        this.statustitle = statustitle
        this.statusrequest = statusrequest
        this.statusdate = statusdate
        this.statusaddress = statusaddress
    }

    var statustitle: String? = null
    var statusrequest: String? = null
    var statusdate: String? = null
    var statusaddress: String? = null
}