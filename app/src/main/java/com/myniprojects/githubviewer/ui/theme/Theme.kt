package com.myniprojects.githubviewer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = greenA200,
    primaryVariant = greenA700,
    secondary = yellowA400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
)

private val LightColorPalette = lightColors(
    primary = greenA400,
    primaryVariant = greenA700,
    secondary = yellowA400,
    secondaryVariant = yellowA700,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
)

@Composable
fun GithubViewerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit)
{
    val colors = if (darkTheme)
    {
        DarkColorPalette
    }
    else
    {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

@Composable
fun ThemedPreview(
    darkTheme: Boolean = false,
    children: @Composable () -> Unit
)
{
    GithubViewerTheme(darkTheme = darkTheme) {
        Surface {
            children()
        }
    }
}