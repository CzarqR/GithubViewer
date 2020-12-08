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

//import com.myniprojects.githubviewer.repository.GithubRepository

class MainViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel()
{
    @ExperimentalCoroutinesApi
    fun searchRepo(query: String): Flow<PagingData<GithubRepo>>
    {
        return githubRepository.getSearchStream(query)
            .cachedIn(viewModelScope)
    }
}