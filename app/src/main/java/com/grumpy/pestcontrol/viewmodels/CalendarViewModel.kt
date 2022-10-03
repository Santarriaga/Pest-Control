package com.grumpy.pestcontrol.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.repositories.JobsRepoImplementation
import com.grumpy.pestcontrol.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class CalendarViewModel(private val repository: JobsRepoImplementation) : ViewModel() {


    // calls repository
    fun loadJobs() = repository.getJobs()





}