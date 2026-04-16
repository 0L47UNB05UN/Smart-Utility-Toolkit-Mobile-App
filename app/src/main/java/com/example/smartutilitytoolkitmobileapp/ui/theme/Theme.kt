package com.example.smartutilitytoolkitmobileapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    // Primary - Maple Red
    primary = MapleRedDark,
    onPrimary = Color.White,
    primaryContainer = MapleRedDarkVariant,
    onPrimaryContainer = Color.White,

    // Secondary - Maple Gold
    secondary = MapleGoldDark,
    onSecondary = Color.Black,
    secondaryContainer = MapleGoldDarkVariant,
    onSecondaryContainer = Color.Black,

    // Tertiary - Forest Green
    tertiary = ForestGreenDark,
    onTertiary = Color.White,
    tertiaryContainer = ForestGreenDarkVariant,
    onTertiaryContainer = Color.White,

    // Surface and Background
    background = Color(0xFF1A1A1A),
    onBackground = MapleNeutralDark,
    surface = Color(0xFF242424),
    onSurface = MapleNeutralDark,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = MapleNeutralVariantDark,

    // Additional surfaces with theme tints
    surfaceContainerLowest = Color(0xFF1A1A1A),
    surfaceContainerLow = Color(0xFF242424),
    surfaceContainer = Color(0xFF2C2C2C),
    surfaceContainerHigh = Color(0xFF333333),
    surfaceContainerHighest = Color(0xFF3A3A3A),

    // Error
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color.White,

    // Outline
    outline = Color(0xFF8A8A8A),
    outlineVariant = Color(0xFF4A4A4A),

    // Inverse
    inverseSurface = MapleNeutralDark,
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = MapleRedLight
)

private val LightColorScheme = lightColorScheme(
    // Primary - Maple Red
    primary = MapleRedLight,
    onPrimary = Color.White,
    primaryContainer = MapleRedLightVariant,
    onPrimaryContainer = Color.White,

    // Secondary - Maple Gold
    secondary = MapleGoldLight,
    onSecondary = Color.Black,
    secondaryContainer = MapleGoldLightVariant,
    onSecondaryContainer = Color.Black,

    // Tertiary - Forest Green
    tertiary = ForestGreenLight,
    onTertiary = Color.White,
    tertiaryContainer = ForestGreenLightVariant,
    onTertiaryContainer = Color.White,

    // Surface and Background
    background = Color(0xFFFAFAFA),
    onBackground = MapleNeutralLight,
    surface = Color(0xFFFDFDFD),
    onSurface = MapleNeutralLight,
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = MapleNeutralVariantLight,

    // Additional surfaces with theme tints
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFAFAFA),
    surfaceContainer = Color(0xFFF5F5F5),
    surfaceContainerHigh = Color(0xFFF0F0F0),
    surfaceContainerHighest = Color(0xFFEBEBEB),

    // Error
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Outline
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFFCACACA),

    // Inverse
    inverseSurface = MapleNeutralLight,
    inverseOnSurface = Color.White,
    inversePrimary = MapleRedDark
)

// BMI Category Colors
val ColorScheme.BMI_CATEGORY_UNDERWEIGHT
    @Composable
    get() = if (!isSystemInDarkTheme()) BMIUnderweightLight else BMIUnderweightDark

val ColorScheme.BMI_CATEGORY_NORMAL
    @Composable
    get() = if (!isSystemInDarkTheme()) BMINormalLight else BMINormalDark

val ColorScheme.BMI_CATEGORY_OVERWEIGHT
    @Composable
    get() = if (!isSystemInDarkTheme()) BMIOverweightLight else BMIOverweightDark

val ColorScheme.BMI_CATEGORY_OBESE
    @Composable
    get() = if (!isSystemInDarkTheme()) BMIObeseLight else BMIObeseDark

@Composable
fun SmartUtilityToolkitMobileAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to use our custom theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}