package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.rule.CoroutineRule

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])

class SaveReminderViewModelTest {

    private lateinit var viewModelInstance: SaveReminderViewModel
    private lateinit var dataSourceInstance: FakeDataSource


    @get:Rule
    var coroutineRule = CoroutineRule()

    @get:Rule
    var executorRule = InstantTaskExecutorRule()


    @After
    fun after() {
        stopKoin()
    }


    @Before
    fun prepareViewModel() {
        dataSourceInstance = FakeDataSource()
        viewModelInstance =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSourceInstance)
    }

    private fun createIncompleteReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            "",
            "abc",
            "abc",
            77.00,
            77.00
        )
    }


    @Test
    fun errorChecker() = runBlockingTest {
        val result = viewModelInstance.validateEnteredData(createIncompleteReminderDataItem())
        MatcherAssert.assertThat(result, CoreMatchers.`is`(false))
    }


    @Test
    fun loadingState() = runBlockingTest {
        coroutineRule.pauseDispatcher()
        viewModelInstance.saveReminder(testReminderItem())
        MatcherAssert.assertThat(viewModelInstance.showLoading.value, CoreMatchers.`is`(true))
        coroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(viewModelInstance.showLoading.value, CoreMatchers.`is`(false))
    }

    private fun testReminderItem(): ReminderDataItem {
        return ReminderDataItem(
            "test",
            "test",
            "test",
            88.00,
            88.00
        )
    }


}