package com.myniprojects.githubviewer.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.githubviewer.network.GithubService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val githubService: GithubService
) : ViewModel()
{
    fun test()
    {
        viewModelScope.launch(IO) {
            val x = githubService.searchRepos("Android", 1, 10)
            Timber.d(x.toString())
        }
    }
}