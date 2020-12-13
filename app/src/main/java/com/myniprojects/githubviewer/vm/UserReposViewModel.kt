package com.myniprojects.githubviewer.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.network.ResponseState
import com.myniprojects.githubviewer.repository.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class UserReposViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel()
{
    var currentQuery: String? = null
        private set

    var currentRepoResult: Flow<PagingData<GithubRepo>>? = null
        private set


    @ExperimentalCoroutinesApi
    fun searchUserRepo(username: String): Flow<PagingData<GithubRepo>>?
    {
        if (username.isBlank())
        {
            return null
        }

        if (currentQuery == username)
        {
            return currentRepoResult
        }

        currentQuery = username

        currentRepoResult = githubRepository.getUserReposSearchStream(username)
            .cachedIn(viewModelScope)

        return currentRepoResult
    }

    var currentUserResult: Flow<ResponseState<GithubUser>>? = null
        private set

    fun searchUser(username: String): Flow<ResponseState<GithubUser>>?
    {
        if (username.isBlank())
        {
            return null
        }

        if (currentQuery == username)
        {
            return currentUserResult
        }

        currentQuery = username

        currentUserResult = githubRepository.getUser(username)

        return currentUserResult
    }

}