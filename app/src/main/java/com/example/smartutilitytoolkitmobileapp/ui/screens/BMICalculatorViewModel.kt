package com.example.smartutilitytoolkitmobileapp.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class BMICategory(val displayName: String) {
    UNDERWEIGHT("Underweight"),
    NORMAL("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese")
}

data class BMIResult(
    val bmiValue: Double,
    val category: BMICategory,
    val height: Double,
    val suggestedMinWeight: Double,
    val suggestedMaxWeight: Double
)

data class BMICalculatorUiState(
    val age: String = "",
    val gender: String = "Male",
    val height: String = "",
    val weight: String = "",
    val isFormValid: Boolean = false,
    val bmiResult: BMIResult? = null
)

sealed class BMIEvent {
    data class UpdateAge(val value: String) : BMIEvent()
    data class UpdateGender(val gender: String) : BMIEvent()
    data class UpdateHeight(val value: String) : BMIEvent()
    data class UpdateWeight(val value: String) : BMIEvent()
    object Calculate : BMIEvent()
}

class BMICalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BMICalculatorUiState())
    val uiState: StateFlow<BMICalculatorUiState> = _uiState.asStateFlow()

    fun onEvent(event: BMIEvent) {
        when (event) {
            is BMIEvent.UpdateAge -> updateAge(event.value)
            is BMIEvent.UpdateGender -> updateGender(event.gender)
            is BMIEvent.UpdateHeight -> updateHeight(event.value)
            is BMIEvent.UpdateWeight -> updateWeight(event.value)
            is BMIEvent.Calculate -> calculateBMI()
        }
    }

    private fun updateAge(value: String) {
        val sanitized = value.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(age = sanitized)
        validateForm()
    }

    private fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    private fun updateHeight(value: String) {
        val sanitized = value.filter { it.isDigit() || it == '.' }
        if (sanitized.count { it == '.' } <= 1) {
            _uiState.value = _uiState.value.copy(height = sanitized)
            validateForm()
        }
    }

    private fun updateWeight(value: String) {
        val sanitized = value.filter { it.isDigit() || it == '.' }
        if (sanitized.count { it == '.' } <= 1) {
            _uiState.value = _uiState.value.copy(weight = sanitized)
            validateForm()
        }
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid = state.age.isNotBlank() &&
                state.height.isNotBlank() &&
                state.weight.isNotBlank() &&
                state.age.toIntOrNull()?.let { it in 2..120 } ?: false &&
                state.height.toDoubleOrNull()?.let { it in 50.0..300.0 } ?: false &&
                state.weight.toDoubleOrNull()?.let { it in 10.0..500.0 } ?: false

        _uiState.value = state.copy(isFormValid = isValid)
    }

    private fun calculateBMI() {
        val state = _uiState.value
        val height = state.height.toDoubleOrNull() ?: return
        val weight = state.weight.toDoubleOrNull() ?: return

        val heightInMeters = height / 100
        val bmi = weight / (heightInMeters * heightInMeters)

        val category = when {
            bmi < 18.5 -> BMICategory.UNDERWEIGHT
            bmi < 25.0 -> BMICategory.NORMAL
            bmi < 30.0 -> BMICategory.OVERWEIGHT
            else -> BMICategory.OBESE
        }

        val suggestedMinWeight = 18.5 * heightInMeters * heightInMeters
        val suggestedMaxWeight = 24.9 * heightInMeters * heightInMeters

        val result = BMIResult(
            bmiValue = bmi,
            category = category,
            height = height,
            suggestedMinWeight = suggestedMinWeight,
            suggestedMaxWeight = suggestedMaxWeight
        )

        _uiState.value = state.copy(bmiResult = result)
    }
}