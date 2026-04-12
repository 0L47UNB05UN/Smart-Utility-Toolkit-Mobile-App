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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConverterScreen(
    viewModel: ConverterScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 48.dp),
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
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Converter.",
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
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
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
                                .padding(6.dp),
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
                                    listOf("USD", "EUR", "GBP", "JPY")
                                },
                                modifier = Modifier.fillMaxWidth()
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
                                    listOf("EUR", "USD", "GBP", "JPY")
                                },
                                isLoading = uiState.isLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Footer with update button only
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { /* Can be used for future features */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    "Update Values",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
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
    modifier: Modifier = Modifier
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
        Box {
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
            modifier = Modifier.fillMaxWidth(),
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