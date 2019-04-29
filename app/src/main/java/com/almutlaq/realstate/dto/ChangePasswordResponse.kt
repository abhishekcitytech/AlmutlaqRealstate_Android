package com.almutlaq.realstate.dto

class ChangePasswordResponse {

    var message_code: Int? = null
    var message: String? = null
    var data: databean? = null

    public class databean{
        var new_password: String? = null

    }


}