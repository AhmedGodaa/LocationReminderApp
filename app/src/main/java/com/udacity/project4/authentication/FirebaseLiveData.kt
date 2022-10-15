package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseLiveData : LiveData<FirebaseUser>() {

    private val auth = FirebaseAuth.getInstance()

    private val stateListener = FirebaseAuth.AuthStateListener { auth ->
        value = auth.currentUser
    }




    override fun onActive() {
        auth.addAuthStateListener(stateListener)
    }

    override fun onInactive() {
        auth.removeAuthStateListener(stateListener)
    }
}