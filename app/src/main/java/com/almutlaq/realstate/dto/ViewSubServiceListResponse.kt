package com.almutlaq.realstate.base

class ViewSubServiceListResponse {
    var message_code: Int? = null
    var message: String? = null
    var data: List<ViewSubServiceListResponse.databean>? = null

    public class databean{
        var subtype_id: Int? = null
        var sub_typename: String? = null
    }

}