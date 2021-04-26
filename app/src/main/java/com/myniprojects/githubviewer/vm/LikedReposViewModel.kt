package com.myniprojects.githubviewer.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.repository.GithubRepository
import kotlinx.coroutines.launch

class LikedReposViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel()
{
    val savedRepos = githubRepository.savedRepos

    fun deleteRepo(githubRepo: GithubRepo) = viewModelScope.launch {
        githubRepository.deleteRepo(githubRepo)
    }

    val savedUsers = githubRepository.savedUsers

    fun deleteUser(githubUser: GithubUser) = viewModelScope.launch {
        githubRepository.deleteUser(githubUser)
    }
}