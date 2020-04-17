package com.example.android.trackmysleepquality.sleepqualitydata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import java.lang.IllegalArgumentException

/**
 * Boiler plate code for a ViewModel Factory.
 *
 * Provides the key for the night and the SleepDatabaseDao to the ViewModel.
 */
class SleepQualityDataViewModelFactory(val sleepNightKey: Long, val dataSource: SleepDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityDataViewModel::class.java)) {
            return SleepQualityDataViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Invalid ViewModel class")
    }
}