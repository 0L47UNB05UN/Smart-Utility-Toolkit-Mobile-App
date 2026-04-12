package com.example.smartutilitytoolkitmobileapp.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ConverterScreenUiState(
    val inputValue: String = "1.00",
    val outputValue: String = "0.92",
    val fromUnit: String = "Kilograms (kg)",
    val toUnit: String = "Pounds (lb)",
    val conversionRate: Double = 2.20462,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUnitConversion: Boolean = true
)

sealed class ConverterEvent {
    data class UpdateInputValue(val value: String) : ConverterEvent()
    data class UpdateFromUnit(val unit: String) : ConverterEvent()
    data class UpdateToUnit(val unit: String) : ConverterEvent()
    data class SwapUnits(val fromUnit: String, val toUnit: String) : ConverterEvent()
    data class ToggleMode(val isUnitConversion: Boolean) : ConverterEvent()
    object ClearError : ConverterEvent()
}

class ConverterScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ConverterScreenUiState())
    val uiState: StateFlow<ConverterScreenUiState> = _uiState.asStateFlow()

    // Unit conversions
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
        ),
        "Grams (g)" to mapOf(
            "Kilograms (kg)" to 0.001,
            "Pounds (lb)" to 0.00220462,
            "Ounces (oz)" to 0.035274
        ),
        "Ounces (oz)" to mapOf(
            "Kilograms (kg)" to 0.0283495,
            "Pounds (lb)" to 0.0625,
            "Grams (g)" to 28.3495
        )
    )

    // Currency rates (base: USD = 1.0)
    private val currencyRates = mapOf(
        "USD" to mapOf(
            "EUR" to 0.92,
            "GBP" to 0.79,
            "JPY" to 150.0,
            "CAD" to 1.36,
            "AUD" to 1.52
        ),
        "EUR" to mapOf(
            "USD" to 1.09,
            "GBP" to 0.86,
            "JPY" to 163.0,
            "CAD" to 1.48,
            "AUD" to 1.65
        ),
        "GBP" to mapOf(
            "USD" to 1.27,
            "EUR" to 1.16,
            "JPY" to 190.0,
            "CAD" to 1.72,
            "AUD" to 1.92
        ),
        "JPY" to mapOf(
            "USD" to 0.0067,
            "EUR" to 0.0061,
            "GBP" to 0.0053,
            "CAD" to 0.0091,
            "AUD" to 0.010
        ),
        "CAD" to mapOf(
            "USD" to 0.74,
            "EUR" to 0.68,
            "GBP" to 0.58,
            "JPY" to 110.0,
            "AUD" to 1.12
        ),
        "AUD" to mapOf(
            "USD" to 0.66,
            "EUR" to 0.61,
            "GBP" to 0.52,
            "JPY" to 99.0,
            "CAD" to 0.89
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
            outputValue = String.format("%.4f", result)
        )
    }
}