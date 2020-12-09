package com.myniprojects.githubviewer.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent
import com.myniprojects.githubviewer.ui.composes.RepoList
import com.myniprojects.githubviewer.ui.composes.formatWithSpaces
import com.myniprojects.githubviewer.ui.theme.GithubViewerTheme
import com.myniprojects.githubviewer.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{

    private val viewModel: MainViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        println((1).formatWithSpaces())
        println((12).formatWithSpaces())
        println((123).formatWithSpaces())
        println((1234).formatWithSpaces())
        println((12345).formatWithSpaces())
        println((123456).formatWithSpaces())
        println((1234567).formatWithSpaces())
        println((12345678).formatWithSpaces())
        println((123456789).formatWithSpaces() + "X")

        // change light mode to test
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //delegate.applyDayNight()

        setContent {
            GithubViewerTheme {
                Surface(color = MaterialTheme.colors.background) {

                    RepoList(repos = viewModel.searchRepo("Android"))

                }
            }
        }
    }
}

