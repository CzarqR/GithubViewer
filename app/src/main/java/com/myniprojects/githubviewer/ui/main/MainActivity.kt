package com.myniprojects.githubviewer.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent
import com.myniprojects.githubviewer.ui.composes.MainHost
import com.myniprojects.githubviewer.ui.theme.GithubViewerTheme
import com.myniprojects.githubviewer.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{

    private val viewModel: MainViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // change light mode to test
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //delegate.applyDayNight()

        setContent {
            GithubViewerTheme {
                Surface(color = MaterialTheme.colors.background) {

                    MainHost(viewModel = viewModel)

                }
            }
        }
    }
}

