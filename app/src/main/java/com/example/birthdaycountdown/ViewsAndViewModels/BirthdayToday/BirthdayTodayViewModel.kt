package com.example.birthdaycountdown.ViewsAndViewModels.BirthdayToday

import androidx.lifecycle.*
import com.example.birthdaycountdown.Shared.Models.CurrentStatesModel
import java.util.*

class BirthdayTodayViewModel : ViewModel(), DefaultLifecycleObserver {
    private val TAG = BirthdayTodayViewModel::class.java.simpleName

    val nameTxt = MutableLiveData<String>()
    val ageTxt = MutableLiveData<Int>()
    val birthDateTxt = MutableLiveData<String>()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val birthday = CurrentStatesModel.selectedBirthday!!

        nameTxt.value = birthday.name

        // get years from birthday.date
        val birthDate = Calendar.getInstance()
        birthDate.time = birthday.date
        val birthYear = birthDate.get(Calendar.YEAR)

        // get years from today
        val today = Calendar.getInstance()
        val todayYear = today.get(Calendar.YEAR)

        // calculate age
        ageTxt.value = todayYear - birthYear

        val endDate = birthday.date

        val calendar = Calendar.getInstance()
        calendar.time = endDate
        birthDateTxt.value = String.format("%02d/%02d/%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(
            Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR))
    }
}