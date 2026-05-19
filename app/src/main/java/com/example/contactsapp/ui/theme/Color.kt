package com.example.contactsapp.ui.theme

import androidx.compose.ui.graphics.Color

// Базовые цвета из шаблона
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Цвета для экрана контактов
val BluePrimary = Color(0xFF1E5BF6)
val BlueLight = Color(0xFF4A7CFF)
val BlueDark = Color(0xFF0033CC)
val BlueLightBackground = Color(0xFFE8F0FF)
val OrangeBackground = Color(0xFFFFF6E5)
val OrangePrimary = Color(0xFFFF9500)
val GreenBackground = Color(0xFFE5FFF0)
val GreenPrimary = Color(0xFF00B34A)
val PurpleBackground = Color(0xFFF3EBFF)
val PurplePrimary = Color(0xFF8A2BE2)

// Нейтральные цвета
val BackgroundGray = Color(0xFFF5F5F5)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF999999)
val TextLightBlue = Color(0xFFB8CCFF)
val White = Color.White

// Material 3 Color Scheme
val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueLightBackground,
    onPrimaryContainer = BluePrimary,
    secondary = OrangePrimary,
    onSecondary = White,
    tertiary = PurplePrimary,
    background = BackgroundGray,
    onBackground = TextPrimary,
    surface = White,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundGray,
    onSurfaceVariant = TextSecondary,
    error = Color(0xFFB3261E),
    onError = White
)

val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = BlueLight,
    onPrimary = Color(0xFF001A4D),
    primaryContainer = Color(0xFF002B80),
    onPrimaryContainer = Color(0xFFDDE4FF),
    secondary = OrangePrimary,
    onSecondary = Color(0xFF331A00),
    tertiary = PurplePrimary,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0)
)