package com.example.smartutilitytoolkitmobileapp.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Tasks : BottomNavItem(
        route = Screen.Tasks.route,
        title = "Tasks",
        icon = Icons.Default.CheckCircle
    )

    object Converter : BottomNavItem(
        route = Screen.Converter.route,
        title = "Converter",
        icon = Icons.Default.SwapHoriz
    )

    object BMI : BottomNavItem(
        route = Screen.BMI.route,
        title = "BMI",
        icon = Icons.Default.FitnessCenter
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Tasks,
    BottomNavItem.Converter,
    BottomNavItem.BMI
)