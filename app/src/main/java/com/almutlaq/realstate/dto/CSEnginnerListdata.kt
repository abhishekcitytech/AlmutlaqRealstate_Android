package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CSEnginnerListdata (

        @SerializedName("engineer_id")
        @Expose
        var engineer_id: Int = -1,

        @SerializedName("engineer_name")
        @Expose
        var engineer_name: String = "",

        @SerializedName("engineer_email")
        @Expose
        var engineer_email: String = "",

        @SerializedName("engineer_contactno")
        @Expose
        var engineer_contactno: String = "",

        @SerializedName("created_at")
        @Expose
        var created_at: String = "",

        @SerializedName("updated_at")
        @Expose
        var updated_at: String = ""

)
