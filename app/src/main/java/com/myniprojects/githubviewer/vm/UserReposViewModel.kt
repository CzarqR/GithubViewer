package com.myniprojects.githubviewer.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.repository.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class UserReposViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel()
{
    var currentQuery: String? = null
        private set

    var currentResult: Flow<PagingData<GithubRepo>>? = null
        private set


    @ExperimentalCoroutinesApi
    fun searchUserRepo(query: String): Flow<PagingData<GithubRepo>>?
    {
        if (query.isBlank())
        {
            return null
        }

        if (currentQuery == query)
        {
            return currentResult
        }

        currentQuery = query

        currentResult = githubRepository.getUserReposSearchStream(query)
            .cachedIn(viewModelScope)

        return currentResult
    }
}