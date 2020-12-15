package com.myniprojects.githubviewer.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val shapes = Shapes(
    small = RoundedCornerShape(topRight = 4.dp, topLeft = 8.dp, bottomLeft = 4.dp),
    medium = RoundedCornerShape(topLeft = 16.dp, bottomRight = 8.dp)
)