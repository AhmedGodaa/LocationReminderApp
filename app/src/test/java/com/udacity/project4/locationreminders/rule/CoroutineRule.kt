package com.udacity.project4.locationreminders.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineRule(
    private val coroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(coroutineDispatcher) {


    override fun finished(d: Description) {
        super.finished(d)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
    override fun starting(d: Description) {
        super.starting(d)
        Dispatchers.setMain(coroutineDispatcher)
    }

}