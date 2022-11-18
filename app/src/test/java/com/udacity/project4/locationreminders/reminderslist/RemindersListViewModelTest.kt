package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.CoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.P])
class RemindersListViewModelTest : AutoCloseKoinTest() {

    lateinit var viewModelReminder: RemindersListViewModel
    lateinit var reminderDataSource: FakeDataSource


    @get:Rule
    var mainCoroutineRule = CoroutineRule()

    @get:Rule
    var executorRule = InstantTaskExecutorRule()




    @Test
    fun check_loading() = mainCoroutineRule.runBlockingTest {

        val reminder = ReminderDTO(
            title = "test",
            description = "test",
            location = "test",
            latitude = 88.00,
            longitude = 88.00
        )
        reminderDataSource.saveReminder(reminder)

        mainCoroutineRule.pauseDispatcher()
        viewModelReminder.loadReminders()
        reminderDataSource.deleteAllReminders()



        assertThat(viewModelReminder.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModelReminder.showLoading.getOrAwaitValue(), `is`(false))


        assertThat(viewModelReminder.showNoData.getOrAwaitValue(), `is`(true))




    }


    @Test
    fun returnError() = mainCoroutineRule.runBlockingTest {

        reminderDataSource.saveReminder(
            ReminderDTO(
                "test",
                "test",
                "test",
                88.00,
                88.00
            )
        )
        reminderDataSource.setError(true)
        viewModelReminder.loadReminders()
        assertThat(viewModelReminder.showSnackBar.getOrAwaitValue(), `is`("Location reminders were unable to be retrieved due to exception occurs"))

    }




    @Before
    fun prepareViewModel() {
        reminderDataSource = FakeDataSource()
        viewModelReminder =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), reminderDataSource)
    }

}