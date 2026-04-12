package com.example.smartutilitytoolkitmobileapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ConverterScreenUiState(
    val inputValue: String = "1.00",
    val outputValue: String = "2.204",
    val fromUnit: String = "Kilograms (kg)",
    val toUnit: String = "Pounds (lb)",
    val conversionRate: Double = 2.20462,
    val lastUpdated: String = "Updated just now",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUnitConversion: Boolean = true // true for unit, false for currency
)

sealed class ConverterEvent {
    data class UpdateInputValue(val value: String) : ConverterEvent()
    data class UpdateFromUnit(val unit: String) : ConverterEvent()
    data class UpdateToUnit(val unit: String) : ConverterEvent()
    data class SwapUnits(val fromUnit: String, val toUnit: String) : ConverterEvent()
    data class ToggleMode(val isUnitConversion: Boolean) : ConverterEvent()
    object RefreshRates : ConverterEvent()
    object ClearError : ConverterEvent()
}

class ConverterScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ConverterScreenUiState())
    val uiState: StateFlow<ConverterScreenUiState> = _uiState.asStateFlow()

    // Mock conversion rates database
    private val unitConversions = mapOf(
        "Kilograms (kg)" to mapOf(
            "Pounds (lb)" to 2.20462,
            "Grams (g)" to 1000.0,
            "Ounces (oz)" to 35.274
        ),
        "Pounds (lb)" to mapOf(
            "Kilograms (kg)" to 0.453592,
            "Grams (g)" to 453.592,
            "Ounces (oz)" to 16.0
        )
    )

    private val currencyRates = mapOf(
        "USD" to mapOf(
            "EUR" to 0.92,
            "GBP" to 0.79,
            "JPY" to 150.0
        )
    )

    fun onEvent(event: ConverterEvent) {
        when (event) {
            is ConverterEvent.UpdateInputValue -> {
                updateInputValue(event.value)
            }
            is ConverterEvent.UpdateFromUnit -> {
                updateFromUnit(event.unit)
            }
            is ConverterEvent.UpdateToUnit -> {
                updateToUnit(event.unit)
            }
            is ConverterEvent.SwapUnits -> {
                swapUnits(event.fromUnit, event.toUnit)
            }
            is ConverterEvent.ToggleMode -> {
                toggleMode(event.isUnitConversion)
            }
            is ConverterEvent.RefreshRates -> {
                refreshRates()
            }
            is ConverterEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    private fun updateInputValue(value: String) {
        val sanitizedValue = value.filter { it.isDigit() || it == '.' }
        if (sanitizedValue.count { it == '.' } <= 1) {
            _uiState.value = _uiState.value.copy(inputValue = sanitizedValue)
            calculateConversion()
        }
    }

    private fun updateFromUnit(unit: String) {
        _uiState.value = _uiState.value.copy(fromUnit = unit)
        updateConversionRate()
        calculateConversion()
    }

    private fun updateToUnit(unit: String) {
        _uiState.value = _uiState.value.copy(toUnit = unit)
        updateConversionRate()
        calculateConversion()
    }

    private fun swapUnits(fromUnit: String, toUnit: String) {
        _uiState.value = _uiState.value.copy(
            fromUnit = toUnit,
            toUnit = fromUnit
        )
        updateConversionRate()
        calculateConversion()
    }

    private fun toggleMode(isUnitConversion: Boolean) {
        val defaultUnits = if (isUnitConversion) {
            Pair("Kilograms (kg)", "Pounds (lb)")
        } else {
            Pair("USD", "EUR")
        }

        _uiState.value = _uiState.value.copy(
            isUnitConversion = isUnitConversion,
            fromUnit = defaultUnits.first,
            toUnit = defaultUnits.second,
            inputValue = "1.00"
        )
        updateConversionRate()
        calculateConversion()
    }

    private fun updateConversionRate() {
        val currentState = _uiState.value
        val rate = if (currentState.isUnitConversion) {
            unitConversions[currentState.fromUnit]?.get(currentState.toUnit) ?: 1.0
        } else {
            currencyRates[currentState.fromUnit]?.get(currentState.toUnit) ?: 1.0
        }

        _uiState.value = currentState.copy(conversionRate = rate)
    }

    private fun calculateConversion() {
        val currentState = _uiState.value
        val input = currentState.inputValue.toDoubleOrNull() ?: 0.0
        val result = input * currentState.conversionRate

        _uiState.value = currentState.copy(
            outputValue = String.format("%.3f", result)
        )
    }

    private fun refreshRates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Simulate API call
            delay(1500)

            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = sdf.format(Date())

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                lastUpdated = "Updated $currentTime"
            )

            // In real app, you'd fetch new rates here
            calculateConversion()
        }
    }
}