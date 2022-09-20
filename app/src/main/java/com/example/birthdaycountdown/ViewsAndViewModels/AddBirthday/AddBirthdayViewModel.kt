package com.example.birthdaycountdown.ViewsAndViewModels.AddBirthday

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birthdaycountdown.Shared.Models.BirthdayModel
import com.example.birthdaycountdown.Shared.Utility.HttpApi
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

class AddBirthdayViewModel : ViewModel(), DefaultLifecycleObserver {
    private val TAG = AddBirthdayViewModel::class.java.simpleName

    val minMonth = MutableLiveData<Int>().apply { value = 0 }
    val maxMonth = MutableLiveData<Int>().apply { value = 11 }
    val month = MutableLiveData<Int>()
    val minDay = MutableLiveData<Int>().apply { value = 1 }
    val maxDay = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()
    val minYear = MutableLiveData<Int>().apply { value = 1900 }
    val maxYear = MutableLiveData<Int>()
    val year = MutableLiveData<Int>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // region Lifecycle
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate")

        // set max year to current year
        maxYear.value = Calendar.getInstance().get(Calendar.YEAR)

        // set the current month and year
        month.value = getCurrentMonth()
        year.value = getCurrentYear()

        // set the max value for the day number picker depending on the month, and the year
        maxDay.value = getMaxDay(month.value!!, year.value!!)

        // set the current day
        day.value = getCurrentDay()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.i(TAG, "onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.i(TAG, "onPause")
    }
    // endregion

    // region Business logic
    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH)
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getMaxDay(month: Int, year: Int): Int {
        return when (month) {
            0, 2, 4, 6, 7, 9, 11 -> 31
            3, 5, 8, 10 -> 30
            1 -> if (year % 4 == 0) 29 else 28
            else -> 0
        }
    }
    // endregion

    // region Event handlers
    fun onMonthChanged(month: Int) {
        this.month.value = month
        maxDay.value = getMaxDay(month, year.value!!)
    }

    fun onYearChanged(year: Int) {
        this.year.value = year
        maxDay.value = getMaxDay(month.value!!, year)
    }

    fun createBirthday(name: String, month: Int, day: Int, year: Int, completion: (String?) -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val date = calendar.time

        val birthday = BirthdayModel(name.trim(), date)

        // convert birthday to json
        val gson = Gson()
        val json = gson.toJson(birthday)
        val body: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)

        coroutineScope.launch {
            try {
                val response = HttpApi.retrofitService.createBirthdayAsync(body).await()
                Log.i(TAG, "createBirthdayAsync success: $response")
                withContext(Dispatchers.Main) {
                    completion(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "createBirthdayAsync fail: ${e.localizedMessage}")
                withContext(Dispatchers.Main) {
                    completion(e.localizedMessage)
                }
            }
        }

//        HttpApi.retrofitService.createBirthdayAsync(body).enqueue(object: Callback<ResponseBody> {
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if(response.isSuccessful) {
//                    Log.i(TAG, "onResponse success [${response.code()}]: ${response.body()}")
//
//                    val birthdays = CurrentStatesModel.birthdays
//                    birthdays.add(birthday)
//                    CurrentStatesModel.birthdays = birthdays
//                }
//                else {
//                    Log.e(TAG, "onResponse error [${response.code()}]: ${response.errorBody()}")
//                }
//            }
//        })
    }
    // endregion
}