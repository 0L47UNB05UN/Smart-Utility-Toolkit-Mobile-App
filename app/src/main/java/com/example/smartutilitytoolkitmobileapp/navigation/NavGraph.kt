package com.example.smartutilitytoolkitmobileapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartutilitytoolkitmobileapp.ui.screens.BMICalculatorScreen
import com.example.smartutilitytoolkitmobileapp.ui.screens.ConverterScreen
import com.example.smartutilitytoolkitmobileapp.ui.screens.TasksScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Tasks.route
    ) {
        composable(Screen.Tasks.route) {
            TasksScreen()
        }

        composable(Screen.Converter.route) {
            ConverterScreen()
        }

        composable(Screen.BMI.route) {
            BMICalculatorScreen()
        }
    }
}