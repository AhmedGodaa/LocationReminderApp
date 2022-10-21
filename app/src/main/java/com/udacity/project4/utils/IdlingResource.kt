package com.udacity.project4.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private const val RESOURCE_NAME = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE_NAME)

    fun increase() {
        countingIdlingResource.increment()
    }

    fun decrease() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapIdlingResource(function: () -> T): T {
    EspressoIdlingResource.increase()
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrease()
    }
}