package com.grumpy.pestcontrol.viewmodels

import androidx.lifecycle.ViewModel
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.repositories.JobsRepoImplementation

class UpdateJobViewModel(private val repository: JobsRepoImplementation) : ViewModel() {

    fun updateEntry(job: Job) = repository.updateJob(job)

}