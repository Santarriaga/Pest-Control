package com.grumpy.pestcontrol.repositories

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.grumpy.pestcontrol.utils.Resource
import com.grumpy.pestcontrol.models.Job
import kotlinx.coroutines.flow.Flow


interface JobsRepoInterface {

     fun addJob(job: Job) : Flow<Resource<DocumentReference>>
     fun updateJob(job : Job) : Flow<Resource<Void>>
     fun getJobs() : Flow<Resource<List<Job>>>
     fun getJobByDate(date: String) : Flow<Resource<List<Job>>>
}