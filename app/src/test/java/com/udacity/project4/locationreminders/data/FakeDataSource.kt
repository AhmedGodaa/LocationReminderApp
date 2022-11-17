package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result


class FakeDataSource : ReminderDataSource {

    private var shouldReturnError = false

    var reminderList = mutableListOf<ReminderDTO>()






    override suspend fun deleteAllReminders() {
        reminderList.clear()
    }



    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderList.add(reminder)
    }


    override suspend fun getReminders(): Result<List<ReminderDTO>> =
        try {
            if (shouldReturnError) {
                throw Exception("Location reminders were unable to be retrieved due to exception occurs")
            }
            Result.Success(reminderList)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }



    override suspend fun getReminder(id: String): Result<ReminderDTO> =
        try {
            val firstOrNull = reminderList.firstOrNull { it.id == id }
            if (shouldReturnError) {
                throw Exception("Location reminder with $id was unable to be retrieved due to exception occurs")
            } else if (firstOrNull == null) {
                Result.Error("Reminder not found!")
            } else {
                Result.Success(firstOrNull)
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }




    fun setError(shouldReturnError: Boolean) {
        this.shouldReturnError = shouldReturnError
    }


}