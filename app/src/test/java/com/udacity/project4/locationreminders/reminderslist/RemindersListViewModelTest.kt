package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.rule.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
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
    fun loadingState() = runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        saveReminderFakeData()
        viewModelReminder.loadReminders()
        MatcherAssert.assertThat(viewModelReminder.showLoading.value, CoreMatchers.`is`(true))
        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(viewModelReminder.showLoading.value, CoreMatchers.`is`(false))
    }

    @Test
    fun testErrorChecker () = runBlockingTest  {
        reminderDataSource.setErrorChecker(true)
        saveReminderFakeData()
        viewModelReminder.loadReminders()
        MatcherAssert.assertThat(viewModelReminder.showSnackBar.value, CoreMatchers.`is`("Reminders can't be found"))
    }



    private suspend fun saveReminderFakeData() {
        reminderDataSource.saveReminder(ReminderDTO("test", "test", "test", 88.00, 88.00))
    }

    @Before
    fun prepareViewModel() {
        reminderDataSource = FakeDataSource()
        viewModelReminder = RemindersListViewModel(ApplicationProvider.getApplicationContext(), reminderDataSource)
    }

}