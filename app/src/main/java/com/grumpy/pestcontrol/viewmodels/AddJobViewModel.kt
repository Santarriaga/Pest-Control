package com.grumpy.pestcontrol.viewmodels

import androidx.lifecycle.ViewModel
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.repositories.JobsRepoImplementation

class AddJobViewModel(private val repository: JobsRepoImplementation) : ViewModel() {

    fun addJob(job: Job) = repository.addJob(job)
}