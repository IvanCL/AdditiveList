package com.icl.additivelist

import androidx.test.espresso.IdlingRegistry
import com.icl.additivelist.testing.EspressoIdlingResource
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule that registers and unregisters an IdlingResource before and after a test runs.
 */
class IdlingResourceRule : TestWatcher() {

    override fun starting(description: Description) {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        super.starting(description)
    }

    override fun finished(description: Description) {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        super.finished(description)
    }
}