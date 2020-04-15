/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*


/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    // Coroutine setup
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init {
        initializeTonight()
    }

    /*
     * Night live data
     */
    private var tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAll()

    val nightString = Transformations.map(nights) {
        formatNights(it, application.resources)
    }

    /*
     * Observables
     */
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    val startButtonIsVisible = Transformations.map(tonight) {
        null == it
    }

    val stopButtonIsVisible = Transformations.map(tonight) {
        null != it
    }

    val clearButtonIsVisible = Transformations.map(nights) {
        it.isNotEmpty()
    }

    fun doneNavigation() {
        _navigateToSleepQuality.value = null
    }

    fun doneWithSnack() {
        _showSnackBarEvent.value = false
    }

    /*
     * Overrides
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /*
     * Listener Handlers
     */
    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getLatestFromDatabase()
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val previousNight = tonight.value ?: return@launch
            previousNight.endTimeMilli = System.currentTimeMillis()
            update(previousNight)
            _navigateToSleepQuality.value = previousNight
        }
    }

    fun onClear() {
        uiScope.launch {
            clearDatabase()
            tonight.value = null
            _showSnackBarEvent.value = true

        }
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getLatestFromDatabase()
        }
    }

    private suspend fun getLatestFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getMostRecent()
            if (night?.startTimeMilli != night?.endTimeMilli) {
                night = null
            }
            night
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

}

