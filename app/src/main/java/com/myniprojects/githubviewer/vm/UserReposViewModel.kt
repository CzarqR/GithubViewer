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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber

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
        Timber.d("searchUserRepo")

        if (username.isBlank())
        {
            return null
        }

        if (currentQuery == username && currentRepoResult != null)
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

    var successUser: Boolean = false

    fun searchUser(
        username: String
    ): Flow<ResponseState<GithubUser>>?
    {
        Timber.d("searchUser")

        if (username.isBlank())
        {
            return null
        }

        if (successUser && currentQuery == username && currentUserResult != null)
        {
            Timber.d("return current")
            return currentUserResult
        }

        currentQuery = username

        currentUserResult = githubRepository.getUser(username)
            .shareIn(viewModelScope, SharingStarted.Lazily, 1)

        Timber.d(currentUserResult.toString())

        return currentUserResult
    }


    fun saveRepo(githubRepo: GithubRepo) = viewModelScope.launch {
        githubRepository.insertRepo(githubRepo)
    }
}