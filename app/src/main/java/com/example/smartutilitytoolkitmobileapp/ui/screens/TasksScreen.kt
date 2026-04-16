package com.example.smartutilitytoolkitmobileapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartutilitytoolkitmobileapp.data.local.TaskEntity
import com.example.smartutilitytoolkitmobileapp.data.preferences.PreferencesManager
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialOverlay
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialPosition
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    // Tutorial state
    val preferencesManager = remember { PreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()
    var showTutorial by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableStateOf(0) }
    var fabPosition by remember { mutableStateOf(Offset.Zero) }
    var fabSize by remember { mutableStateOf(Size.Zero) }
    var firstTaskPosition by remember { mutableStateOf(Offset.Zero) }
    var firstTaskSize by remember { mutableStateOf(Size.Zero) }
    var taskListPosition by remember { mutableStateOf(Offset.Zero) }
    var taskListSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(Unit) {
        preferencesManager.hasSeenTasksTutorial().collect { hasSeen ->
            if (!hasSeen && uiState.tasks.isEmpty()) {
                delay(500)
                showTutorial = true
            }
        }
    }

    LaunchedEffect(uiState.isAddingTask) {
        if (uiState.isAddingTask) {
            delay(100)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val tutorialSteps = listOf(
        TutorialStep(
            key = "welcome",
            title = "Welcome to Tasks",
            description = "Keep track of everything you need to do. Let's take a quick tour to see how it works!",
            position = TutorialPosition.Center
        ),
        TutorialStep(
            key = "add_task",
            title = "Add a New Task",
            description = "Tap the + button to create a new task. You can add as many tasks as you need to stay organized.",
            highlightOffset = fabPosition,
            highlightSize = fabSize,
            position = TutorialPosition.Top
        ),
        TutorialStep(
            key = "complete_task",
            title = "Complete Tasks",
            description = "Tap on any task to mark it as complete. Completed tasks will move to the bottom with a strikethrough so you can focus on what's left.",
            highlightOffset = firstTaskPosition,
            highlightSize = firstTaskSize,
            position = TutorialPosition.Bottom
        ),
        TutorialStep(
            key = "delete_mode",
            title = "Delete Tasks",
            description = "Long press on any task to enter delete mode. Select multiple tasks and tap the trash icon to delete them all at once. Tap Cancel to exit delete mode.",
            highlightOffset = taskListPosition,
            highlightSize = taskListSize,
            position = TutorialPosition.Center
        ),
        TutorialStep(
            key = "ready",
            title = "You're All Set!",
            description = "Start adding tasks and stay productive. Enjoy using Tasks!",
            position = TutorialPosition.Center
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (!showTutorial) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (uiState.isAddingTask) {
                                    viewModel.onEvent(TasksEvent.HideAddTaskInput)
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 672.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isDeleteMode) "Select Tasks" else "Tasks",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )

                        if (uiState.isDeleteMode) {
                            TextButton(
                                onClick = { viewModel.onEvent(TasksEvent.ExitDeleteMode) }
                            ) {
                                Text(
                                    "Cancel",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Tasks List Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .onGloballyPositioned { coordinates ->
                                taskListPosition = coordinates.positionInRoot()
                                taskListSize = Size(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {
                            if (uiState.tasks.isEmpty() && !uiState.isAddingTask) {
                                // Empty State
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(
                                            text = "📋",
                                            style = MaterialTheme.typography.displayLarge
                                        )
                                        Text(
                                            text = "No tasks here yet",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Tap + to add your first task",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            } else {
                                val incompleteTasks = uiState.tasks.filter { !it.isCompleted }
                                val completedTasks = uiState.tasks.filter { it.isCompleted }

                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Incomplete tasks
                                    items(incompleteTasks) { task ->
                                        TaskItem(
                                            task = task,
                                            isDeleteMode = uiState.isDeleteMode,
                                            isSelected = uiState.selectedTasks.contains(task.id),
                                            onToggle = {
                                                if (uiState.isDeleteMode) {
                                                    viewModel.onEvent(TasksEvent.ToggleTaskSelection(task.id))
                                                } else {
                                                    viewModel.onEvent(TasksEvent.ToggleTaskCompletion(task))
                                                }
                                            },
                                            onLongPress = {
                                                if (!uiState.isDeleteMode) {
                                                    viewModel.onEvent(TasksEvent.EnterDeleteMode(task.id))
                                                }
                                            },
                                            modifier = if (incompleteTasks.indexOf(task) == 0) {
                                                Modifier.onGloballyPositioned { coordinates ->
                                                    firstTaskPosition = coordinates.positionInRoot()
                                                    firstTaskSize = Size(
                                                        coordinates.size.width.toFloat(),
                                                        coordinates.size.height.toFloat()
                                                    )
                                                }
                                            } else {
                                                Modifier
                                            }
                                        )
                                    }

                                    // Completed header
                                    if (completedTasks.isNotEmpty() && !uiState.isDeleteMode) {
                                        item {
                                            Text(
                                                text = "✓ Completed",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                            )
                                        }
                                    }

                                    // Completed tasks
                                    items(completedTasks) { task ->
                                        TaskItem(
                                            task = task,
                                            isDeleteMode = uiState.isDeleteMode,
                                            isSelected = uiState.selectedTasks.contains(task.id),
                                            onToggle = {
                                                if (uiState.isDeleteMode) {
                                                    viewModel.onEvent(TasksEvent.ToggleTaskSelection(task.id))
                                                } else {
                                                    viewModel.onEvent(TasksEvent.ToggleTaskCompletion(task))
                                                }
                                            },
                                            onLongPress = {
                                                if (!uiState.isDeleteMode) {
                                                    viewModel.onEvent(TasksEvent.EnterDeleteMode(task.id))
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Delete Bar (shown in delete mode)
                    if (uiState.isDeleteMode) {
                        DeleteBar(
                            selectedCount = uiState.selectedTasks.size,
                            onDelete = {
                                viewModel.onEvent(TasksEvent.ShowDeleteConfirmation)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // New Task Dialog
            if (uiState.isAddingTask) {
                NewTaskDialog(
                    value = uiState.newTaskTitle,
                    onValueChange = {
                        viewModel.onEvent(TasksEvent.UpdateNewTaskTitle(it))
                    },
                    onDone = {
                        viewModel.onEvent(TasksEvent.SaveNewTask)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    onDismiss = {
                        viewModel.onEvent(TasksEvent.HideAddTaskInput)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    focusRequester = focusRequester
                )
            }

            // Delete Confirmation Dialog
            if (uiState.showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.onEvent(TasksEvent.HideDeleteConfirmation)
                    },
                    title = {
                        Text(
                            "Delete Tasks",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Text(
                            "Are you sure you want to delete ${uiState.selectedTasks.size} task${if (uiState.selectedTasks.size > 1) "s" else ""}? This action cannot be undone.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(TasksEvent.ConfirmDelete)
                            }
                        ) {
                            Text(
                                "Delete",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(TasksEvent.HideDeleteConfirmation)
                            }
                        ) {
                            Text("Cancel")
                        }
                    },
                    shape = RoundedCornerShape(20.dp)
                )
            }

            // Floating Action Button (hidden in delete mode and when adding task)
            if (!uiState.isAddingTask && !uiState.isDeleteMode) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(TasksEvent.ShowAddTaskInput)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp)
                        .size(56.dp)
                        .onGloballyPositioned { coordinates ->
                            fabPosition = coordinates.positionInRoot()
                            fabSize = Size(
                                coordinates.size.width.toFloat(),
                                coordinates.size.height.toFloat()
                            )
                        },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }

            // Tutorial Overlay
            if (showTutorial) {
                TutorialOverlay(
                    steps = tutorialSteps,
                    currentStep = currentTutorialStep,
                    onNext = {
                        if (currentTutorialStep < tutorialSteps.size - 1) {
                            currentTutorialStep++
                        } else {
                            showTutorial = false
                            coroutineScope.launch {
                                preferencesManager.setTasksTutorialShown()
                            }
                        }
                    },
                    onSkip = {
                        showTutorial = false
                        coroutineScope.launch {
                            preferencesManager.setTasksTutorialShown()
                        }
                    },
                    onDismiss = {
                        showTutorial = false
                        coroutineScope.launch {
                            preferencesManager.setTasksTutorialShown()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteBar(
    selectedCount: Int,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$selectedCount task${if (selectedCount > 1) "s" else ""} selected",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Medium
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete selected tasks",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    onDismiss: () -> Unit,
    focusRequester: FocusRequester
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onDismiss() }
                    )
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .imePadding(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "New Task",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = {
                            Text(
                                "What needs to be done?",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp, max = 200.dp)
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(16.dp),
                        minLines = 5,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        )
                    )

                    Button(
                        onClick = onDone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = value.isNotBlank()
                    ) {
                        Text(
                            "Add Task",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    isDeleteMode: Boolean,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    isDeleteMode -> MaterialTheme.colorScheme.surfaceContainerLowest
                    else -> MaterialTheme.colorScheme.surfaceContainerLowest
                }
            )
            .combinedClickable(
                onClick = { onToggle() },
                onLongClick = { onLongPress() }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        task.isCompleted && !isDeleteMode -> MaterialTheme.colorScheme.primary
                        else -> Color.Transparent
                    }
                )
                .border(
                    2.dp,
                    when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        task.isCompleted && !isDeleteMode -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outlineVariant
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected || (task.isCompleted && !isDeleteMode)) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = if (isDeleteMode) "Selected" else "Completed",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Task title
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (task.isCompleted && !isDeleteMode) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            ),
            color = if (task.isCompleted && !isDeleteMode) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun TasksScreenPreview() {
    MaterialTheme {
        TasksScreen()
    }
}