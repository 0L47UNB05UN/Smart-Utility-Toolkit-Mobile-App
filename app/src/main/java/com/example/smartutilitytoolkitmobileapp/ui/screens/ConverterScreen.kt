package com.example.smartutilitytoolkitmobileapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartutilitytoolkitmobileapp.data.preferences.PreferencesManager
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialOverlay
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialPosition
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    viewModel: ConverterScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Tutorial state
    val preferencesManager = remember { PreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()
    var showTutorial by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableStateOf(0) }
    var modeTogglePosition by remember { mutableStateOf(Offset.Zero) }
    var modeToggleSize by remember { mutableStateOf(Size.Zero) }
    var fromInputPosition by remember { mutableStateOf(Offset.Zero) }
    var fromInputSize by remember { mutableStateOf(Size.Zero) }
    var swapButtonPosition by remember { mutableStateOf(Offset.Zero) }
    var swapButtonSize by remember { mutableStateOf(Size.Zero) }
    var fromUnitPosition by remember { mutableStateOf(Offset.Zero) }
    var fromUnitSize by remember { mutableStateOf(Size.Zero) }
    var toUnitPosition by remember { mutableStateOf(Offset.Zero) }
    var toUnitSize by remember { mutableStateOf(Size.Zero) }

    // Check if tutorial should be shown
    LaunchedEffect(Unit) {
        preferencesManager.hasSeenConverterTutorial().collect { hasSeen ->
            if (!hasSeen) {
                delay(500)
                showTutorial = true
            }
        }
    }

    // Tutorial steps
    val tutorialSteps = listOf(
        TutorialStep(
            key = "welcome",
            title = "Smart Converter",
            description = "Convert between units and currencies with precision. Let's see how to use it!",
            position = TutorialPosition.Center
        ),
        TutorialStep(
            key = "mode_toggle",
            title = "Switch Conversion Mode",
            description = "Toggle between Unit conversion (kg, lb, etc.) and Currency conversion (USD, EUR, etc.).",
            highlightOffset = modeTogglePosition,
            highlightSize = modeToggleSize,
            position = TutorialPosition.Bottom
        ),
        TutorialStep(
            key = "from_input",
            title = "Enter Value to Convert",
            description = "Type the value you want to convert in the FROM field. The conversion happens automatically as you type.",
            highlightOffset = fromInputPosition,
            highlightSize = fromInputSize,
            position = TutorialPosition.Top
        ),
        TutorialStep(
            key = "from_unit",
            title = "Select Source Unit",
            description = "Tap the dropdown to choose the unit or currency you want to convert from.",
            highlightOffset = fromUnitPosition,
            highlightSize = fromUnitSize,
            position = TutorialPosition.Bottom
        ),
        TutorialStep(
            key = "swap",
            title = "Quick Swap",
            description = "Tap the swap button to instantly reverse the conversion direction. No need to reselect units!",
            highlightOffset = swapButtonPosition,
            highlightSize = swapButtonSize,
            position = TutorialPosition.Center
        ),
        TutorialStep(
            key = "to_unit",
            title = "Select Target Unit",
            description = "Tap the dropdown to choose the unit or currency you want to convert to. The result appears automatically.",
            highlightOffset = toUnitPosition,
            highlightSize = toUnitSize,
            position = TutorialPosition.Bottom
        ),
        TutorialStep(
            key = "ready",
            title = "You're Ready!",
            description = "Start converting values for your projects, travel, or daily needs. Happy converting!",
            position = TutorialPosition.Center
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 48.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 672.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Header Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Converter",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Text(
                        text = "Precision-engineered conversion tools for global workflows.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.widthIn(max = 448.dp)
                    )
                }

                // Converter Module Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        // Mode Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(6.dp)
                                .onGloballyPositioned { coordinates ->
                                    modeTogglePosition = coordinates.positionInRoot()
                                    modeToggleSize = Size(
                                        coordinates.size.width.toFloat(),
                                        coordinates.size.height.toFloat()
                                    )
                                },
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ModeToggleChip(
                                text = "Unit",
                                isSelected = uiState.isUnitConversion,
                                onClick = {
                                    viewModel.onEvent(ConverterEvent.ToggleMode(true))
                                },
                                modifier = Modifier.weight(1f)
                            )
                            ModeToggleChip(
                                text = "Currency",
                                isSelected = !uiState.isUnitConversion,
                                onClick = {
                                    viewModel.onEvent(ConverterEvent.ToggleMode(false))
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Converter Main Grid
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // FROM Section
                            ConversionBlock(
                                label = "FROM",
                                unit = uiState.fromUnit,
                                value = uiState.inputValue,
                                onValueChange = {
                                    viewModel.onEvent(ConverterEvent.UpdateInputValue(it))
                                },
                                onUnitSelected = { selectedUnit ->
                                    viewModel.onEvent(ConverterEvent.UpdateFromUnit(selectedUnit))
                                },
                                isEditable = true,
                                units = if (uiState.isUnitConversion) {
                                    listOf("Kilograms (kg)", "Pounds (lb)", "Grams (g)", "Ounces (oz)")
                                } else {
                                    listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                onInputPositioned = { position, size ->
                                    fromInputPosition = position
                                    fromInputSize = size
                                },
                                onUnitPositioned = { position, size ->
                                    fromUnitPosition = position
                                    fromUnitSize = size
                                }
                            )

                            // Swap Button
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(
                                        ConverterEvent.SwapUnits(
                                            uiState.fromUnit,
                                            uiState.toUnit
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .shadow(8.dp, CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceContainerLowest,
                                        CircleShape
                                    )
                                    .onGloballyPositioned { coordinates ->
                                        swapButtonPosition = coordinates.positionInRoot()
                                        swapButtonSize = Size(
                                            coordinates.size.width.toFloat(),
                                            coordinates.size.height.toFloat()
                                        )
                                    }
                            ) {
                                Icon(
                                    Icons.Default.SwapHoriz,
                                    contentDescription = "Swap Units",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // TO Section
                            ConversionBlock(
                                label = "TO",
                                unit = uiState.toUnit,
                                value = uiState.outputValue,
                                onValueChange = {},
                                onUnitSelected = { selectedUnit ->
                                    viewModel.onEvent(ConverterEvent.UpdateToUnit(selectedUnit))
                                },
                                isEditable = false,
                                units = if (uiState.isUnitConversion) {
                                    listOf("Pounds (lb)", "Kilograms (kg)", "Grams (g)", "Ounces (oz)")
                                } else {
                                    listOf("EUR", "USD", "GBP", "JPY", "CAD", "AUD")
                                },
                                isLoading = uiState.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                                onUnitPositioned = { position, size ->
                                    toUnitPosition = position
                                    toUnitSize = size
                                }
                            )
                        }

                        // Footer with info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Rates update automatically",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
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
                            preferencesManager.setConverterTutorialShown()
                        }
                    }
                },
                onSkip = {
                    showTutorial = false
                    coroutineScope.launch {
                        preferencesManager.setConverterTutorialShown()
                    }
                },
                onDismiss = {
                    showTutorial = false
                    coroutineScope.launch {
                        preferencesManager.setConverterTutorialShown()
                    }
                }
            )
        }
    }
}

@Composable
fun ModeToggleChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ConversionBlock(
    label: String,
    unit: String,
    value: String,
    onValueChange: (String) -> Unit,
    onUnitSelected: (String) -> Unit,
    isEditable: Boolean,
    units: List<String>,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onInputPositioned: ((Offset, Size) -> Unit)? = null,
    onUnitPositioned: ((Offset, Size) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Unit Selector
        Box(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                onUnitPositioned?.invoke(
                    coordinates.positionInRoot(),
                    Size(
                        coordinates.size.width.toFloat(),
                        coordinates.size.height.toFloat()
                    )
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Select Unit",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                units.forEach { unitOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                unitOption,
                                color = if (unitOption == unit) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        },
                        onClick = {
                            onUnitSelected(unitOption)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Value Display/Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isEditable && onInputPositioned != null) {
                        Modifier.onGloballyPositioned { coordinates ->
                            onInputPositioned(
                                coordinates.positionInRoot(),
                                Size(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            )
                        }
                    } else {
                        Modifier
                    }
                ),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (isEditable) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (value.isEmpty()) {
                                    Text(
                                        "0.00",
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                } else {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConverterScreenPreview() {
    MaterialTheme {
        ConverterScreen()
    }
}