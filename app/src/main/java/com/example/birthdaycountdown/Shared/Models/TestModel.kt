package com.example.birthdaycountdown.Shared.Models

import com.squareup.moshi.Json
import java.util.*

data class TestModel (
    @Json(name = "firstName")
    var firstName: String,
    @Json(name = "lastName")
    var lastName: String,
    @Json(name = "age")
    var age: Int
)
data class TestResponse(@Json(name = "data") val data: List<TestModel>)