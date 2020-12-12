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
import com.myniprojects.githubviewer.vm.LikedReposViewModel
import com.myniprojects.githubviewer.vm.PublicReposViewModel
import com.myniprojects.githubviewer.vm.UserReposViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private val userReposViewModel: UserReposViewModel by viewModels()
    private val publicReposViewModel: PublicReposViewModel by viewModels()
    private val likedReposViewModel: LikedReposViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContent {
            GithubViewerTheme {
                Surface(color = MaterialTheme.colors.background) {

                    MainHost(
                        userReposViewModel = userReposViewModel,
                        publicReposViewModel = publicReposViewModel,
                        likedReposViewModel = likedReposViewModel
                    )
                }
            }
        }
    }
}

