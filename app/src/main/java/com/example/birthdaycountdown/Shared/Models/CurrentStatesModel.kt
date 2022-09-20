package com.example.birthdaycountdown.Shared.Models

class CurrentStatesModel {
    companion object {
        var birthdays = mutableListOf<BirthdayModel>()
        var selectedBirthday: BirthdayModel? = null
    }
}