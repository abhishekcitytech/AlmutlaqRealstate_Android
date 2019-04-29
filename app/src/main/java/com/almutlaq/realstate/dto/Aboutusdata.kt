package com.almutlaq.realstate.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Aboutusdata (

        @SerializedName("company_name")
        @Expose
        val company_name: String = "",

        @SerializedName("company_website_url")
        @Expose
        val company_website_url: String = "",

        @SerializedName("company_description")
        @Expose
        val company_description: String = "",

        @SerializedName("company_video_link")
        @Expose
        val company_video_link: String = "",

        @SerializedName("company_logo")
        @Expose
        val company_logo: String = "",

        @SerializedName("company_address")
        @Expose
        val company_address: String = "",

        @SerializedName("company_image")
        @Expose
        val company_image: String = ""

)
