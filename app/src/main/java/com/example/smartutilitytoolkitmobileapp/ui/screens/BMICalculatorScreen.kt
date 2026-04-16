package com.example.smartutilitytoolkitmobileapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartutilitytoolkitmobileapp.data.preferences.PreferencesManager
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialOverlay
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialPosition
import com.example.smartutilitytoolkitmobileapp.ui.components.TutorialStep
import com.example.smartutilitytoolkitmobileapp.ui.theme.BMI_CATEGORY_OBESE
import com.example.smartutilitytoolkitmobileapp.ui.theme.BMI_CATEGORY_OVERWEIGHT
import com.example.smartutilitytoolkitmobileapp.ui.theme.BMI_CATEGORY_NORMAL
import com.example.smartutilitytoolkitmobileapp.ui.theme.BMI_CATEGORY_UNDERWEIGHT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BMICalculatorScreen(
    viewModel: BMICalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showResultDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Tutorial state
    val preferencesManager = remember { PreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()
    var showTutorial by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableStateOf(0) }
    var ageInputPosition by remember { mutableStateOf(Offset.Zero) }
    var ageInputSize by remember { mutableStateOf(Size.Zero) }
    var genderSelectorPosition by remember { mutableStateOf(Offset.Zero) }
    var genderSelectorSize by remember { mutableStateOf(Size.Zero) }
    var heightInputPosition by remember { mutableStateOf(Offset.Zero) }
    var heightInputSize by remember { mutableStateOf(Size.Zero) }
    var weightInputPosition by remember { mutableStateOf(Offset.Zero) }
    var weightInputSize by remember { mutableStateOf(Size.Zero) }
    var calculateButtonPosition by remember { mutableStateOf(Offset.Zero) }
    var calculateButtonSize by remember { mutableStateOf(Size.Zero) }

    // Check if tutorial should be shown
    LaunchedEffect(Unit) {
        preferencesManager.hasSeenBMITutorial().collect { hasSeen ->
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
            title = "BMI Calculator",
            description = "Calculate your Body Mass Index and understand your weight category. Let's see how it works!",
            position = TutorialPosition.Center
        ),
        TutorialStep(
            key = "age",
            title = "Enter Your Age",
            description = "Start by entering your age. BMI calculations are interpreted differently based on age groups.",
            highlightOffset = ageInputPosition,
            highlightSize = ageInputSize,
            position = TutorialPosition.Bottom
        ),
        TutorialStep(
            key = "gender",
            title = "Select Gender",
            description = "Choose your gender. BMI ranges vary slightly between males and females.",
            highlightOffset = genderSelectorPosition,
            highlightSize = genderSelectorSize,
            position = TutorialPosition.Top
        ),
        TutorialStep(
            key = "height",
            title = "Enter Height",
            description = "Input your height in centimeters. This is used to calculate your BMI along with your weight.",
            highlightOffset = heightInputPosition,
            highlightSize = heightInputSize,
            position = TutorialPosition.Top
        ),
        TutorialStep(
            key = "weight",
            title = "Enter Weight",
            description = "Input your weight in kilograms. Your BMI will be calculated using weight / (height in meters)².",
            highlightOffset = weightInputPosition,
            highlightSize = weightInputSize,
            position = TutorialPosition.Top
        ),
        TutorialStep(
            key = "calculate",
            title = "Calculate BMI",
            description = "Once all fields are filled, tap Calculate to see your BMI result and detailed analysis.",
            highlightOffset = calculateButtonPosition,
            highlightSize = calculateButtonSize,
            position = TutorialPosition.Top
        ),
        TutorialStep(
            key = "result",
            title = "Understanding Results",
            description = "Your result shows your BMI value, category, and suggested weight range. Categories are color-coded: Blue (Underweight), Green (Normal), Yellow (Overweight), Red (Obese).",
            position = TutorialPosition.Center
        ),
        TutorialStep(
            key = "ready",
            title = "You're Ready!",
            description = "Track your BMI regularly to maintain a healthy lifestyle. Stay healthy!",
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
                // Header
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "BMI",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Text(
                        text = "Body mass index calculator for health assessment.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.widthIn(max = 448.dp)
                    )
                }

                // Input Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Age Input
                        InputField(
                            label = "Age",
                            value = uiState.age,
                            onValueChange = { viewModel.onEvent(BMIEvent.UpdateAge(it)) },
                            placeholder = "Enter age (2-120)",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                ageInputPosition = coordinates.positionInRoot()
                                ageInputSize = Size(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            }
                        )

                        // Gender Selection
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                genderSelectorPosition = coordinates.positionInRoot()
                                genderSelectorSize = Size(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            }
                        ) {
                            Text(
                                text = "Gender",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                GenderChip(
                                    text = "Male",
                                    isSelected = uiState.gender == "Male",
                                    onClick = { viewModel.onEvent(BMIEvent.UpdateGender("Male")) },
                                    modifier = Modifier.weight(1f)
                                )
                                GenderChip(
                                    text = "Female",
                                    isSelected = uiState.gender == "Female",
                                    onClick = { viewModel.onEvent(BMIEvent.UpdateGender("Female")) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Height Input
                        InputField(
                            label = "Height (cm)",
                            value = uiState.height,
                            onValueChange = { viewModel.onEvent(BMIEvent.UpdateHeight(it)) },
                            placeholder = "Enter height (50-300)",
                            keyboardType = KeyboardType.Decimal,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                heightInputPosition = coordinates.positionInRoot()
                                heightInputSize = Size(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            }
                        )

                        // Weight Input
                        InputField(
                            label = "Weight (kg)",
                            value = uiState.weight,
                            onValueChange = { viewModel.onEvent(BMIEvent.UpdateWeight(it)) },
                            placeholder = "Enter weight (10-500)",
                            keyboardType = KeyboardType.Decimal,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                weightInputPosition = coordinates.positionInRoot()
                                weightInputSize = Size(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            }
                        )

                        // About BMI Section
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "About BMI",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Body mass index (BMI) is a person's weight in kilograms divided by the square of height in metres. BMI is an easy screening method for weight category.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Calculate Button
                        Button(
                            onClick = {
                                viewModel.onEvent(BMIEvent.Calculate)
                                showResultDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .onGloballyPositioned { coordinates ->
                                    calculateButtonPosition = coordinates.positionInRoot()
                                    calculateButtonSize = Size(
                                        coordinates.size.width.toFloat(),
                                        coordinates.size.height.toFloat()
                                    )
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = uiState.isFormValid
                        ) {
                            Text(
                                "Calculate BMI",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }

                        if (!uiState.isFormValid && (uiState.age.isNotBlank() || uiState.height.isNotBlank() || uiState.weight.isNotBlank())) {
                            Text(
                                text = "Please fill all fields with valid values",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Result Dialog
        if (showResultDialog && uiState.bmiResult != null) {
            BMIResultDialog(
                result = uiState.bmiResult!!,
                onDismiss = { showResultDialog = false }
            )
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
                            preferencesManager.setBMITutorialShown()
                        }
                    }
                },
                onSkip = {
                    showTutorial = false
                    coroutineScope.launch {
                        preferencesManager.setBMITutorialShown()
                    }
                },
                onDismiss = {
                    showTutorial = false
                    coroutineScope.launch {
                        preferencesManager.setBMITutorialShown()
                    }
                }
            )
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            )
        )
    }
}

@Composable
fun GenderChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainerLowest
            )
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BMIResultDialog(
    result: BMIResult,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Title
                Text(
                    text = "Your current BMI",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // BMI Value
                Text(
                    text = String.format("%.1f", result.bmiValue),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = when (result.category) {
                            BMICategory.UNDERWEIGHT -> MaterialTheme.colorScheme.BMI_CATEGORY_UNDERWEIGHT
                            BMICategory.NORMAL -> MaterialTheme.colorScheme.BMI_CATEGORY_NORMAL
                            BMICategory.OVERWEIGHT -> MaterialTheme.colorScheme.BMI_CATEGORY_OVERWEIGHT
                            BMICategory.OBESE -> MaterialTheme.colorScheme.BMI_CATEGORY_OBESE
                        }
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Body mass index",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Category
                Text(
                    text = result.category.displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = when (result.category) {
                            BMICategory.UNDERWEIGHT -> MaterialTheme.colorScheme.BMI_CATEGORY_UNDERWEIGHT
                            BMICategory.NORMAL -> MaterialTheme.colorScheme.BMI_CATEGORY_NORMAL
                            BMICategory.OVERWEIGHT -> MaterialTheme.colorScheme.BMI_CATEGORY_OVERWEIGHT
                            BMICategory.OBESE -> MaterialTheme.colorScheme.BMI_CATEGORY_OBESE
                        }
                    )
                )

                // Category Scale
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.BMI_CATEGORY_UNDERWEIGHT)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.BMI_CATEGORY_NORMAL)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.BMI_CATEGORY_OVERWEIGHT)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.BMI_CATEGORY_OBESE)
                    )
                }

                // Category Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Under",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.BMI_CATEGORY_UNDERWEIGHT
                    )
                    Text(
                        "Normal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.BMI_CATEGORY_NORMAL
                    )
                    Text(
                        "Over",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.BMI_CATEGORY_OVERWEIGHT
                    )
                    Text(
                        "Obese",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.BMI_CATEGORY_OBESE
                    )
                }

                // Indicator arrow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                ) {
                    val position = when (result.category) {
                        BMICategory.UNDERWEIGHT -> 0.125f
                        BMICategory.NORMAL -> 0.375f
                        BMICategory.OVERWEIGHT -> 0.625f
                        BMICategory.OBESE -> 0.875f
                    }

                    Box(
                        modifier = Modifier
                            .offset(
                                x = (position * 300).dp.coerceIn(0.dp, 300.dp)
                            )
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(
                                when (result.category) {
                                    BMICategory.UNDERWEIGHT -> MaterialTheme.colorScheme.BMI_CATEGORY_UNDERWEIGHT
                                    BMICategory.NORMAL -> MaterialTheme.colorScheme.BMI_CATEGORY_NORMAL
                                    BMICategory.OVERWEIGHT -> MaterialTheme.colorScheme.BMI_CATEGORY_OVERWEIGHT
                                    BMICategory.OBESE -> MaterialTheme.colorScheme.BMI_CATEGORY_OBESE
                                }
                            )
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Analysis
                Text(
                    text = "Analysis",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Height (cm)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.1f", result.height),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Suggested weight (kg)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.1f ~ %.1f", result.suggestedMinWeight, result.suggestedMaxWeight),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Health Tip
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                ) {
                    Text(
                        text = getHealthTip(result.category),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // OK Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "OK",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

fun getHealthTip(category: BMICategory): String {
    return when (category) {
        BMICategory.UNDERWEIGHT -> "💡 Consider consulting a nutritionist to help you reach a healthy weight through balanced nutrition."
        BMICategory.NORMAL -> "💡 Great job! Maintain your healthy weight with regular exercise and a balanced diet."
        BMICategory.OVERWEIGHT -> "💡 Small lifestyle changes can make a big difference. Try incorporating more physical activity into your daily routine."
        BMICategory.OBESE -> "💡 Consider speaking with a healthcare provider about developing a personalized weight management plan."
    }
}

@Preview(showBackground = true)
@Composable
fun BMICalculatorScreenPreview() {
    MaterialTheme {
        BMICalculatorScreen()
    }
}