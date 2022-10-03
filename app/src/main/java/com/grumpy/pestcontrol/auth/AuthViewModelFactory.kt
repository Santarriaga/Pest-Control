package com.grumpy.pestcontrol.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AuthenticationRepository::class.java).newInstance(AuthenticationRepository())
    }
}