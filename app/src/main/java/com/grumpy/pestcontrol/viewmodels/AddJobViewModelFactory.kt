package com.grumpy.pestcontrol.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grumpy.pestcontrol.repositories.JobsRepoImplementation

class AddJobViewModelFactory : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(JobsRepoImplementation::class.java).newInstance(
            JobsRepoImplementation()
        )
    }
}