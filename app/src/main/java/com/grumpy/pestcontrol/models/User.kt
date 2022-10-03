package com.grumpy.pestcontrol.models

import com.google.firebase.firestore.Exclude

data class User(
    var uid : String,
    val name: String,
    val email: String,
    @Exclude
    var isAuthenticated: Boolean,
    @Exclude
    val isNew: Boolean,
    @Exclude
    val isCreated : Boolean
)