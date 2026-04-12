package com.example.smartutilitytoolkitmobileapp.data.repository

import com.example.smartutilitytoolkitmobileapp.data.local.TaskDao
import com.example.smartutilitytoolkitmobileapp.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getCompletedCount(): Flow<Int> = taskDao.getCompletedCount()

    suspend fun insertTask(task: TaskEntity): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}