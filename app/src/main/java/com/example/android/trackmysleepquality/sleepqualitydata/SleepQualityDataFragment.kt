package com.example.android.trackmysleepquality.sleepqualitydata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityDataBinding
import com.example.android.trackmysleepquality.sleepquality.SleepQualityFragmentArgs
import com.example.android.trackmysleepquality.sleepquality.SleepQualityViewModel
import com.example.android.trackmysleepquality.sleepquality.SleepQualityViewModelFactory

class SleepQualityDataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding : FragmentSleepQualityDataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_quality_data, container, false)

        binding.lifecycleOwner = this
        val arguments = SleepQualityDataFragmentArgs.fromBundle(arguments!!)
        val application = requireNotNull(this.activity).application
        val datasource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepQualityDataViewModelFactory(arguments.sleepNightKey, datasource)
        val sleepQualityDataViewModel = ViewModelProvider(this, viewModelFactory).get(SleepQualityDataViewModel::class.java)

        sleepQualityDataViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(SleepQualityDataFragmentDirections.actionSleepQualityDataFragmentToSleepTrackerFragment())
                sleepQualityDataViewModel.navigationComplete()
            }
        })

        binding.sleepQualityDataViewModel = sleepQualityDataViewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}


