package com.grumpy.pestcontrol.auth

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationRepository {

    private val firebaseAuth : FirebaseAuth by lazy {FirebaseAuth.getInstance() }
    private val firebaseUser = MutableLiveData<FirebaseUser>()
    private val userLogged = MutableLiveData<Boolean>()

    init {
        if (firebaseAuth.currentUser != null){
            firebaseUser.postValue(firebaseAuth.currentUser)
        }
    }


    fun getFirebaseUserData() : MutableLiveData<FirebaseUser>{
        return firebaseUser
    }

    fun getUserLogged() : MutableLiveData<Boolean>{
        return userLogged
    }

    fun register(email : String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(){ task ->
            if (task.isSuccessful){
                firebaseUser.postValue(firebaseAuth.currentUser)
            }else{
                Log.d("Error", task.exception?.message.toString())
            }

        }
    }

    fun login(email: String,password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){ task ->
            if(task.isSuccessful){
                firebaseUser.postValue(firebaseAuth.currentUser)
            }else{
                Log.d("Error", task.exception?.message.toString())
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        userLogged.postValue(true)
    }



}