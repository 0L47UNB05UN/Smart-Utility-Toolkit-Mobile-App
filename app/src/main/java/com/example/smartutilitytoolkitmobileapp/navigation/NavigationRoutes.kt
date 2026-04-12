package com.example.smartutilitytoolkitmobileapp.navigation

sealed class Screen(val route: String) {
    object Tasks : Screen("tasks")
    object Converter : Screen("converter")
    object BMI : Screen("bmi")
}