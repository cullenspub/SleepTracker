package com.example.android.trackmysleepquality.sleepqualitydata

import android.util.Log
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SleepQualityDataViewModel(private val sleepNightKey: Long, val dataSource: SleepDatabaseDao) : ViewModel() {

    // Use Mediator instead of Coroutine
    val night = MediatorLiveData<SleepNight>()

    init {
        night.addSource(dataSource.get(sleepNightKey), Observer {
            Log.i("SleepQualityDataViewMod", "observed change with key ${it.nightId}")
            night.value = it
        })
    }

    private var _navigateToSleepTracker = MutableLiveData<Boolean>()
    val navigateToSleepTracker: LiveData<Boolean>
        get() = _navigateToSleepTracker

    fun onRequestNavigation() {
        _navigateToSleepTracker.value = true
    }

    fun navigationComplete() {
        _navigateToSleepTracker.value = null
    }
}




























