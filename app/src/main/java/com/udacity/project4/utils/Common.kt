package com.udacity.project4.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment



fun Fragment.openActivity(activity: Class<*>?) {
    val intent = Intent(requireContext(), activity)
    this.startActivity(intent)
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.openActivity(activity: Class<*>?) {
    val intent = Intent(this, activity)
    this.startActivity(intent)
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
