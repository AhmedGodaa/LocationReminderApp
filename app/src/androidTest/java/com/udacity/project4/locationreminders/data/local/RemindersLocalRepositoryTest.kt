package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class RemindersLocalRepositoryTest {



    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    lateinit var repo: RemindersLocalRepository

    lateinit var db: RemindersDatabase


    @Before
    fun setup() {

        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
        repo = RemindersLocalRepository(db.reminderDao())
    }

    @After
    fun cleanUpDataBase() = db.close()


    @Test
    fun testInsertionAndRetrieveData() = runBlocking {

        val fakeData = ReminderDTO(
            "test",
            "test",
            "test",
            88.00,
            88.00
        )

        repo.saveReminder(fakeData)

        val result = repo.getReminder(fakeData.id)

        result as Result.Success
        MatcherAssert.assertThat(true, CoreMatchers.`is`(true))

        val insertedData = result.data
        MatcherAssert.assertThat(insertedData.id, CoreMatchers.`is`(fakeData.id))
        MatcherAssert.assertThat(insertedData.title, CoreMatchers.`is`(fakeData.title))
        MatcherAssert.assertThat(insertedData.description, CoreMatchers.`is`(fakeData.description))
        MatcherAssert.assertThat(insertedData.location, CoreMatchers.`is`(fakeData.location))
        MatcherAssert.assertThat(insertedData.latitude, CoreMatchers.`is`(fakeData.latitude))
        MatcherAssert.assertThat(insertedData.longitude, CoreMatchers.`is`(fakeData.longitude))
    }


    @Test
    fun testNoDataAndReturnError() = runBlocking {
        val result = repo.getReminder("888") as Result.Error
        MatcherAssert.assertThat(result.message, Matchers.notNullValue())
        MatcherAssert.assertThat(result.message, CoreMatchers.`is`("Reminder not found!"))
    }

}