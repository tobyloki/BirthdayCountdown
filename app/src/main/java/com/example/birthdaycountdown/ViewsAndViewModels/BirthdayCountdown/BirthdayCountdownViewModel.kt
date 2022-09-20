package com.example.birthdaycountdown.ViewsAndViewModels.BirthdayCountdown

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.birthdaycountdown.Shared.Models.CurrentStatesModel
import java.time.LocalDate
import java.time.Period
import java.util.*

class BirthdayCountdownViewModel : ViewModel(), DefaultLifecycleObserver {
    private val TAG = BirthdayCountdownViewModel::class.java.simpleName

    val months = MutableLiveData<Int>()
    val days = MutableLiveData<Int>()
    val hours = MutableLiveData<Int>()
    val minutes = MutableLiveData<Int>()
    val seconds = MutableLiveData<Int>()
    val nameTxt = MutableLiveData<String>()
    val birthDateTxt = MutableLiveData<String>()
    val navigateToNextPage = MutableLiveData<Boolean>().apply { value = false }

    private val endDate = MutableLiveData<Date>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val birthday = CurrentStatesModel.selectedBirthday!!

        nameTxt.value = birthday.name

        // Set the end date to the current date + 10 seconds
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.SECOND, 3)
//        endDate.value = birthday.date //calendar.time

        val calendar = Calendar.getInstance()
        calendar.time = birthday.date
        birthDateTxt.value = String.format("%02d/%02d/%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR))

        // if the birthday is in the past, ignoring hour, minute, and second, add a year to
        val currentCalendar = Calendar.getInstance()
        val checkCalendar = calendar.clone() as Calendar
        checkCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR))

        Log.i(TAG, "onCreate: currentCalendar = ${currentCalendar.time}")
        Log.i(TAG, "onCreate: checkCalendar = ${checkCalendar.time}")

        if (calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
            calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)) {
            // NOTE: Set the year to this year
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR))
        } else if (checkCalendar.timeInMillis < currentCalendar.timeInMillis) {
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) + 1)
        } else {
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR))
        }

        Log.i(TAG, "onCreate: calendar = ${calendar.time}")
        endDate.value = calendar.time

        // Start the countdown
        startCountdown()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCountdown() {
        // Create a new thread to run the countdown
        Thread {
            // While the current date is less than the end date
            while (Date() < endDate.value!!) {
                // Get the difference between the current date and the end date
                val difference = endDate.value!!.time - Date().time

                val startCalendar = Calendar.getInstance()
                startCalendar.time = Date()
                val endCalendar = Calendar.getInstance()
                endCalendar.time = endDate.value!!

                val start: LocalDate = LocalDate.now()
                val end: LocalDate = LocalDate.of(endDate.value!!.year, endDate.value!!.month, endDate.value!!.date)

                // get number of days in current month
                val daysInMonth = start.lengthOfMonth()

                // get the months, days, hours, minutes, and seconds from the difference accurately
                val period: Period = Period.between(start, end)
                val years = (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR))
                val months = years * 12 + period.months
                val days = daysInMonth + period.days
                val hours = (difference / (1000 * 60 * 60)).toInt() % 24
                val minutes = (difference / (1000 * 60)).toInt() % 60
                val seconds = (difference / 1000).toInt() % 60

//                val years = (start.get(Calendar.YEAR) - end.get(Calendar.YEAR))
//                val months =  years * 12 + start.get(Calendar.MONTH) - end.get(Calendar.MONTH)
//                val days = start.get(Calendar.DAY_OF_MONTH) - end.get(Calendar.DAY_OF_MONTH)
//                val hours = start.get(Calendar.HOUR_OF_DAY) - end.get(Calendar.HOUR_OF_DAY)
//                val minutes = start.get(Calendar.MINUTE) - end.get(Calendar.MINUTE)
//                val seconds = start.get(Calendar.SECOND) - end.get(Calendar.SECOND)

//                val days = TimeUnit.MILLISECONDS.toDays(difference)
//                val months = (difference / (1000L * 60 * 60 * 24 * 30)).toInt()
//                val days = (difference / (1000 * 60 * 60 * 24)).toInt() % 30
//                val hours = (difference / (1000 * 60 * 60)).toInt() % 24
//                val minutes = (difference / (1000 * 60)).toInt() % 60
//                val seconds = (difference / 1000).toInt() % 60

                // Set the values of the LiveData objects
                this.months.postValue(months)
                this.days.postValue(days)
                this.hours.postValue(hours)
                this.minutes.postValue(minutes)
                this.seconds.postValue(seconds)
            }

            // when countdown finishes, update navigateToNextPage to true
            navigateToNextPage.postValue(true)
        }.start()
    }
}