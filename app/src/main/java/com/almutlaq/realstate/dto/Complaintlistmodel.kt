package com.almutlaq.realstate.dto

class Complaintlistmodel {
    constructor(complainttitle: String?, complaintquote: String?) {
        this.complainttitle = complainttitle
        this.complaintquote = complaintquote
    }

    constructor()

    var complainttitle: String? = null
    var complaintquote: String? = null
    var complaintstatus: String? = null
    var complaintclosednote: String? = null
}