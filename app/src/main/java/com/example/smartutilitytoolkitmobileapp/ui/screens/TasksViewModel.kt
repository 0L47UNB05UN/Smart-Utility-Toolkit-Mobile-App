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
    val newTaskTitle: String = "",
    val isDeleteMode: Boolean = false,
    val selectedTasks: Set<Long> = emptySet(),
    val showDeleteConfirmation: Boolean = false
)

sealed class TasksEvent {
    data class UpdateNewTaskTitle(val title: String) : TasksEvent()
    object ShowAddTaskInput : TasksEvent()
    object HideAddTaskInput : TasksEvent()
    object SaveNewTask : TasksEvent()
    data class ToggleTaskCompletion(val task: TaskEntity) : TasksEvent()
    data class EnterDeleteMode(val initialTaskId: Long) : TasksEvent()
    object ExitDeleteMode : TasksEvent()
    data class ToggleTaskSelection(val taskId: Long) : TasksEvent()
    object ShowDeleteConfirmation : TasksEvent()
    object HideDeleteConfirmation : TasksEvent()
    object ConfirmDelete : TasksEvent()
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
                if (!_uiState.value.isDeleteMode) {
                    toggleTaskCompletion(event.task)
                }
            }
            is TasksEvent.EnterDeleteMode -> {
                _uiState.value = _uiState.value.copy(
                    isDeleteMode = true,
                    selectedTasks = setOf(event.initialTaskId)
                )
            }
            is TasksEvent.ExitDeleteMode -> {
                _uiState.value = _uiState.value.copy(
                    isDeleteMode = false,
                    selectedTasks = emptySet()
                )
            }
            is TasksEvent.ToggleTaskSelection -> {
                toggleTaskSelection(event.taskId)
            }
            is TasksEvent.ShowDeleteConfirmation -> {
                if (_uiState.value.selectedTasks.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(showDeleteConfirmation = true)
                }
            }
            is TasksEvent.HideDeleteConfirmation -> {
                _uiState.value = _uiState.value.copy(showDeleteConfirmation = false)
            }
            is TasksEvent.ConfirmDelete -> {
                deleteSelectedTasks()
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

    private fun toggleTaskSelection(taskId: Long) {
        val currentSelected = _uiState.value.selectedTasks
        val newSelected = if (currentSelected.contains(taskId)) {
            currentSelected - taskId
        } else {
            currentSelected + taskId
        }

        _uiState.value = _uiState.value.copy(selectedTasks = newSelected)

        // Exit delete mode if no tasks selected
        if (newSelected.isEmpty()) {
            _uiState.value = _uiState.value.copy(isDeleteMode = false)
        }
    }

    private fun deleteSelectedTasks() {
        viewModelScope.launch {
            val tasksToDelete = _uiState.value.tasks.filter {
                _uiState.value.selectedTasks.contains(it.id)
            }

            tasksToDelete.forEach { task ->
                repository.deleteTask(task)
            }

            _uiState.value = _uiState.value.copy(
                isDeleteMode = false,
                selectedTasks = emptySet(),
                showDeleteConfirmation = false
            )
        }
    }
}