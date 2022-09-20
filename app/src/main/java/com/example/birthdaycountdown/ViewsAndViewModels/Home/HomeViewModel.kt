package com.example.birthdaycountdown.ViewsAndViewModels.Home

import android.util.Log
import androidx.lifecycle.*
import com.example.birthdaycountdown.Shared.Models.BirthdayModel
import com.example.birthdaycountdown.Shared.Models.CurrentStatesModel
import com.example.birthdaycountdown.Shared.Utility.HttpApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*


class HomeViewModel : ViewModel(), DefaultLifecycleObserver {
    private val TAG = HomeViewModel::class.java.simpleName

    class ListItem {
        var name: String = ""
        var date: Date = Date()

        constructor(name: String, date: Date) {
            this.name = name
            this.date = date
        }

        constructor()
    }

    val birthdays = MutableLiveData<List<ListItem>>().apply {
        value = listOf()
    }
    val loading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val showError = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i(TAG, "onCreate")

        loading.value = true
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.i(TAG, "onResume")

        showError.value = false

        coroutineScope.launch {
            try {
                val response = HttpApi.retrofitService.getBirthdaysAsync().await()
                Log.i(TAG, "getBirthdaysAsync success: $response")

                // convert response JSONArray to List<BirthdayModel>
                val data = JSONObject(response).getJSONArray("data")
                // loop through data
                val itemList = mutableListOf<BirthdayModel>()
                for (i in 0 until data.length()) {
                    try {
                        val item = data.getJSONObject(i)
                        val id = item.getString("id")
                        val name = item.getString("name")
                        val date = Date(item.getString("date"))
                        val listItem = BirthdayModel(id, name, date)
                        itemList.add(listItem)
                    } catch (e: Exception) {
                        Log.e(TAG, "parseBirthdays error: $e")
                    }
                }

                CurrentStatesModel.birthdays = itemList

                val birthdayList = CurrentStatesModel.birthdays
                // sort by date whoever's date is closest to today, and then alphabetically by name
                birthdayList.sortWith(Comparator { o1, o2 ->
                    return@Comparator compareBirthdays(o1.date, o2.date, o1.name, o2.name)
                })
                CurrentStatesModel.birthdays = birthdayList

                // populate list
                val list = mutableListOf<ListItem>()
                birthdayList.forEach {
                    val item = ListItem(it.name, it.date)
                    list.add(item)
                }
                // sort by date whoever's date is closest to today, and then alphabetically by name
                list.sortWith(Comparator { o1, o2 ->
                    return@Comparator compareBirthdays(o1.date, o2.date, o1.name, o2.name)
                })
                birthdays.postValue(list)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "getBirthdaysAsync fail: ${e.localizedMessage}")

                showError.postValue(true)
            }

            loading.postValue(false)
        }
        // make get call to HttpApi
//        HttpApi.retrofitService.getBirthdaysAsync().enqueue(object: Callback<TestResponse> {
//            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//
//            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {
//                if(response.isSuccessful) {
//                    Log.i(TAG, "onResponse success [${response.code()}]: ${response.body()}")
//                } else {
//                    Log.e(TAG, "onResponse fail [${response.code()}]: ${response.errorBody()}")
//                }
//            }
//        })
    }

    private fun compareBirthdays(date1: Date, date2: Date, name1: String, name2: String): Int {
        // if birthdays are 09/11/2000, 03/08/2013, 09/09/2022, 10/10/2022 and today is 09/09/22, order it like this:
        // 09/09/2022, 09/11/2000, 10/10/2022, 03/08/2013
        // get which birthday is upcoming. if both are upcoming, then compare by name

        // sort birth dates by whichever one is coming up, starting with today
        // order by the absolute value of the difference between the two dates and today's date
//        val today = Date()
//        val diff1 = Math.abs(date1.time - today.time)
//        val diff2 = Math.abs(date2.time - today.time)
//        if (diff1 < diff2) {
//            return -1
//        } else if (diff1 > diff2) {
//            return 1
//        } else {
            // if both are upcoming, then compare by name
            return name1.compareTo(name2)
//        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.i(TAG, "onPause")
    }
}