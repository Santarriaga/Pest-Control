package com.grumpy.pestcontrol.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.repositories.JobsRepoImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddJobViewModel(private val repository: JobsRepoImplementation) : ViewModel() {

    fun addJob(job: Job) = repository.addJob(job)
}