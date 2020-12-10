package com.myniprojects.githubviewer.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import com.myniprojects.githubviewer.R

private val Montserrat = fontFamily(
    font(R.font.montserrat_regular),
    font(R.font.montserrat_medium, FontWeight.W500),
    font(R.font.montserrat_semibold, FontWeight.W600)
)

val typography = Typography(
    defaultFontFamily = Montserrat
)