package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {


    val stateOfAuthentication = FirebaseLiveData().map { currentUser ->
        if (currentUser != null) {
            AuthenticationStates.AUTHENTICATED
        } else {
            AuthenticationStates.UNAUTHENTICATED
        }
    }

}