package com.myniprojects.githubviewer.ui.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.myniprojects.githubviewer.vm.LikedReposViewModel


@ExperimentalMaterialApi
@Composable
fun LikedReposScreen(
    viewModel: LikedReposViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
)
{
    Text(text = "LikedReposScreen")
}