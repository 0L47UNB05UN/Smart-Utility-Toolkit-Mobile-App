package com.example.smartutilitytoolkitmobileapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartutilitytoolkitmobileapp.data.local.TaskEntity
import com.example.smartutilitytoolkitmobileapp.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val isAddingTask: Boolean = false,
    val newTaskTitle: String = ""
)

sealed class TasksEvent {
    data class UpdateNewTaskTitle(val title: String) : TasksEvent()
    object ShowAddTaskInput : TasksEvent()
    object HideAddTaskInput : TasksEvent()
    object SaveNewTask : TasksEvent()
    data class ToggleTaskCompletion(val task: TaskEntity) : TasksEvent()
    data class DeleteTask(val task: TaskEntity) : TasksEvent()
}

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect { tasks ->
                _uiState.value = _uiState.value.copy(tasks = tasks)
            }
        }
    }

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.UpdateNewTaskTitle -> {
                _uiState.value = _uiState.value.copy(newTaskTitle = event.title)
            }
            is TasksEvent.ShowAddTaskInput -> {
                _uiState.value = _uiState.value.copy(isAddingTask = true)
            }
            is TasksEvent.HideAddTaskInput -> {
                _uiState.value = _uiState.value.copy(
                    isAddingTask = false,
                    newTaskTitle = ""
                )
            }
            is TasksEvent.SaveNewTask -> {
                saveTask()
            }
            is TasksEvent.ToggleTaskCompletion -> {
                toggleTaskCompletion(event.task)
            }
            is TasksEvent.DeleteTask -> {
                deleteTask(event.task)
            }
        }
    }

    private fun saveTask() {
        val title = _uiState.value.newTaskTitle.trim()
        if (title.isNotEmpty()) {
            viewModelScope.launch {
                val task = TaskEntity(
                    title = title,
                    isCompleted = false
                )
                repository.insertTask(task)
                _uiState.value = _uiState.value.copy(
                    isAddingTask = false,
                    newTaskTitle = ""
                )
            }
        }
    }

    private fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            repository.updateTask(updatedTask)
        }
    }

    private fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}