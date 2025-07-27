package com.todolist.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.todolist.app.data.Task
import com.todolist.app.data.TaskDatabase
import com.todolist.app.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    val allTasks: LiveData<List<Task>>
    val pendingTasks: LiveData<List<Task>>
    val completedTasks: LiveData<List<Task>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.getAllTasks()
        pendingTasks = repository.getPendingTasks()
        completedTasks = repository.getCompletedTasks()
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        repository.toggleTaskCompletion(task)
    }

    fun deleteCompletedTasks() = viewModelScope.launch {
        repository.deleteCompletedTasks()
    }

    suspend fun getTaskById(id: Long): Task? {
        return repository.getTaskById(id)
    }
}