package com.myniprojects.githubviewer.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
    private val _selectedItemType: MutableState<ItemType> = mutableStateOf(ItemType.REPOS)
    val selectedItemType: State<ItemType> = _selectedItemType

    val savedRepos = githubRepository.savedRepos

    fun deleteRepo(githubRepo: GithubRepo) = viewModelScope.launch {
        githubRepository.deleteRepo(githubRepo)
    }

    val savedUsers = githubRepository.savedUsers

    fun deleteUser(githubUser: GithubUser) = viewModelScope.launch {
        githubRepository.deleteUser(githubUser)
    }

    fun nextItemType()
    {
        _selectedItemType.value = if (_selectedItemType.value == ItemType.REPOS) ItemType.USERS else ItemType.REPOS
    }
}

enum class ItemType
{
    REPOS,
    USERS
}