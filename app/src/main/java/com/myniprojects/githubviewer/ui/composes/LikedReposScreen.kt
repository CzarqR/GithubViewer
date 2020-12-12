package com.myniprojects.githubviewer.ui.composes

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.myniprojects.githubviewer.vm.LikedReposViewModel

@ExperimentalMaterialApi
@Composable
fun LikedReposScreen(
    viewModel: LikedReposViewModel,
    snackbarHostState: SnackbarHostState
)
{
    Text(text = "LikedReposScreen")
}