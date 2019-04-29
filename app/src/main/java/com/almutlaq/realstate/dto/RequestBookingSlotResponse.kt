package com.almutlaq.realstate.base

class RequestBookingSlotResponse {
    var message_code: Int? = null
    var message: String? = null
    var data: List<RequestBookingSlotResponse.databean>? = null

    public class databean{
        var start: String? = null
        var end: String? = null
    }

}