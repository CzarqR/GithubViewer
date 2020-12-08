package com.myniprojects.githubviewer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = purple200,
    primaryVariant = purple700,
    secondary = teal200
)

private val LightColorPalette = lightColors(
    primary = purple500,
    primaryVariant = purple700,
    secondary = teal200
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