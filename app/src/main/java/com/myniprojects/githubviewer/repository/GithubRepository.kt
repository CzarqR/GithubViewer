package com.myniprojects.githubviewer.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubPaging: GithubPaging,
    private val mapper: NetworkToDomainMapper
)
{
    @ExperimentalCoroutinesApi
    fun getSearchStream(query: String): Flow<PagingData<GithubRepo>>
    {
        Timber.d("New query submitted $query")
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { githubPaging.getGithubPagingSource(query) }
        ).flow.map { pagingData ->
            pagingData.map { responseItem ->
                mapper.mapToNewModel(responseItem)
            }
        }
    }
}
