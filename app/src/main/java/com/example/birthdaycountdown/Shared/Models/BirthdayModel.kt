package com.example.birthdaycountdown.Shared.Models

import com.squareup.moshi.Json
import java.util.*

class BirthdayModel {
    // Properties
//    @Json(name = "id")
    var id: String = ""
//    @Json(name = "name")
    var name: String = ""
//    @Json(name = "date")
    var date: Date = Date()

    // Constructor
    constructor(id: String, name: String, date: Date) {
        // create random uuid
        this.id = id
        this.name = name
        this.date = date
    }

    constructor(name: String, date: Date) {
        // create random uuid
        this.id = UUID.randomUUID().toString()
        this.name = name
        this.date = date
    }

    // Empty constructor
    constructor()
}
//data class BirthdayListResponse(@Json(name = "data") val data: List<BirthdayModel>)