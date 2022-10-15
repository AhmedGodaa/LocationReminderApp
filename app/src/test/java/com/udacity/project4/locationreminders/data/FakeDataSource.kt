package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result


class FakeDataSource : ReminderDataSource {

    private var errorChecker = false

    var reminderList = mutableListOf<ReminderDTO>()

    override suspend fun deleteAllReminders() { reminderList.clear() }

    fun setErrorChecker(value: Boolean) { errorChecker = value }
    override suspend fun saveReminder(reminder: ReminderDTO) { reminderList.add(reminder) }


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {
            if (errorChecker) { throw Exception("Reminders can't be found") }
            Result.Success(ArrayList(reminderList)) } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }


    override suspend fun getReminder(id: String): Result<ReminderDTO> {

        return try {
            val reminder = reminderList.find { it.id == id }
            if (errorChecker || reminder == null) { throw Exception("Not found $id") }
            else { Result.Success(reminder) }
        }
        catch (ex: Exception) { Result.Error(ex.localizedMessage) }
    }




}