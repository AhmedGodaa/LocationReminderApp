package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationStates.AUTHENTICATED
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.utils.Constants.KEY_CODE_SIGNIN
import com.udacity.project4.utils.openActivity


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        checkLoginState()


    }

    // check login state if logged send to remainders activity if not stay and perform login
    private fun checkLoginState() {
        viewModel.stateOfAuthentication.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AUTHENTICATED -> {
                    openActivity(RemindersActivity::class.java)
                }
                else -> {}
            }
        })

    }


    fun signin(view: View) {
        val loginProviders = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(loginProviders)
                .build(), KEY_CODE_SIGNIN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KEY_CODE_SIGNIN) {
            if (resultCode == Activity.RESULT_OK) {
                openActivity(RemindersActivity::class.java)
            }
        }
    }
}

