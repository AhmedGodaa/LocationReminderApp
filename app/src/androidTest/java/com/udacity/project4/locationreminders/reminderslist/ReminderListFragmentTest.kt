package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

    private lateinit var repo: ReminderDataSource
    private lateinit var applicationContext: Application
    private val dataBindingResource = DataBindingIdlingResource()


    @Before
    fun registerResources() { IdlingRegistry.getInstance().register(dataBindingResource) }


    @After
    fun unregisterResources() { IdlingRegistry.getInstance().unregister(dataBindingResource) }

    @Test
    fun testNoDataIsDisplayed() {
        val fragmentScenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        dataBindingResource.monitorFragment(fragmentScenario)
        Espresso.onView(ViewMatchers.withText(R.string.no_data))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }




    @Test
    fun testNavFragments() {

        val fragmentScenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        dataBindingResource.monitorFragment(fragmentScenario)
        val navController = Mockito.mock(NavController::class.java)
        fragmentScenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        Espresso.onView(withId(R.id.addReminderFAB)).perform(click())
        Mockito.verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }

    @Before
    fun initializeTests() {
        stopKoin()
        applicationContext = getApplicationContext()
        val usedModule = module {
            viewModel { RemindersListViewModel(applicationContext, get() as ReminderDataSource) }
            single { SaveReminderViewModel(applicationContext, get() as ReminderDataSource) }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(applicationContext) }
        }
        startKoin { modules(listOf(usedModule)) }
        repo = get()


        runBlocking { repo.deleteAllReminders() }
    }


}