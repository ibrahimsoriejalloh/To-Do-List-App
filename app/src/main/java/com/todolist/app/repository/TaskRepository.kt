package com.todolist.app.repository

import androidx.lifecycle.LiveData
import com.todolist.app.data.Task
import com.todolist.app.data.TaskDao
import java.util.Date

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun getPendingTasks(): LiveData<List<Task>> = taskDao.getPendingTasks()

    fun getCompletedTasks(): LiveData<List<Task>> = taskDao.getCompletedTasks()

    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) {
        val updatedTask = task.copy(updatedAt = Date())
        taskDao.updateTask(updatedTask)
    }

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = Date()
        )
        taskDao.updateTask(updatedTask)
    }

    suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()
}