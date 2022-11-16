package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result


class FakeDataSource : ReminderDataSource {

    private var shouldReturnError = false

    var reminderList = mutableListOf<ReminderDTO>()

    override suspend fun deleteAllReminders() {
        reminderList.clear()
    }

    fun setErrorChecker(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderList.add(reminder)
    }


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {
            if (shouldReturnError)
                throw Exception("Reminder not found!")
            else
                reminderList.let { return@let Result.Success(ArrayList(reminderList)) }
        } catch (ex: Exception) {
            return Result.Error(ex.localizedMessage)
        }
    }


    override suspend fun getReminder(id: String): Result<ReminderDTO> {

        return try {
            val reminder = reminderList.find { it.id == id }
            if (shouldReturnError || reminder == null) {
                throw Exception("Reminder not found!")
            } else {
                Result.Success(reminder)
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }


}