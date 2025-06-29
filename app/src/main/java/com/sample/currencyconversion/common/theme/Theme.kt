package com.sample.currencyconversion.common.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// Dark theme
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple40,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Background40,
    onBackground = PurpleGrey80,
    surface = PurpleGrey80,
    onSurface = Black0
)

// Light theme
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = White100,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = White100,
    onBackground = Black0,
    surface = PurpleGrey80,
    onSurface = Black0

)

@Composable
fun CurrencyConversionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MyTheme.typography,
        content = content
    )
}

object MyTheme {
    val typography = Typography(
        // Title styles
        titleLarge = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        ),
        titleMedium = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        ),
        titleSmall = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        ),

        // Body styles
        bodyLarge = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        ),
        bodyMedium = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        bodySmall = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        ),

        // Optional label styles
        labelLarge = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 0.5.sp
        )
    )
}

