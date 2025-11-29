package com.icl.additivelist.testing

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * A simple singleton that manages a CountingIdlingResource for UI tests.
 * This allows tests to wait for long-running operations like network calls to complete.
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}