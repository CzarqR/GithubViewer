package com.myniprojects.githubviewer.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.RepoListModel
import com.myniprojects.githubviewer.repository.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

//import com.myniprojects.githubviewer.repository.GithubRepository

class MainViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel()
{
    @ExperimentalCoroutinesApi
    fun searchRepo(query: String): Flow<PagingData<RepoListModel>>
    {
        return githubRepository.getSearchStream(query)
            .map { pagingData ->
                pagingData.map { githubRepo ->
                    RepoListModel.RepoItem(githubRepo)
                }
            }
            .map { pagingData ->
                pagingData.insertSeparators { before, after ->

                    // end of the list
                    if (after == null)
                    {
                        return@insertSeparators null
                    }

                    //beginning of the list
                    if (before == null)
                    {
                        return@insertSeparators RepoListModel.RepoSeparatorItem(
                            after.githubRepo.stars,
                        )
                    }

                    if (checkInsert(before.githubRepo, after.githubRepo))
                    {
                        RepoListModel.RepoSeparatorItem(
                            after.githubRepo.stars
                        )
                    }
                    else
                    {
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
    }
}

private fun checkInsert(before: GithubRepo, after: GithubRepo): Boolean
{
    var i = 100_000
    while (i > 1)
    {
        if (after.stars > i)
        {
            return before.stars / i != after.stars / i
        }
        i /= 10
    }

    return false
}