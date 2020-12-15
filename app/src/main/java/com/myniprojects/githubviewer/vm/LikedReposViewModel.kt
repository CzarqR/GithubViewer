package com.myniprojects.githubviewer.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.repository.GithubRepository
import kotlinx.coroutines.launch

class LikedReposViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel()
{
//    val savedRepos = githubRepository.getSavedRepos()

    fun saveRepo(githubRepo: GithubRepo) = viewModelScope.launch {
        githubRepository.insertRepo(githubRepo)
    }

    fun deleteRepo(githubRepo: GithubRepo) = viewModelScope.launch {
        githubRepository.deleteRepo(githubRepo)
    }
}