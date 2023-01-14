package com.grumpy.pestcontrol.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    private val userData = repository.getFirebaseUserData()
    private val loggedStatus = repository.getUserLogged()

    //TODO:add necessary functions inside coroutine
    fun getUserData() : MutableLiveData<FirebaseUser>  {
        return userData
    }

    fun getLoggedStatus() : MutableLiveData<Boolean>{
        return loggedStatus
    }

    fun register(email :String, password :String){
        repository.register(email, password)
    }

    fun signIn(email :String, password :String){
        repository.login(email, password)
    }

    fun signOut(){
        repository.signOut()
    }


}