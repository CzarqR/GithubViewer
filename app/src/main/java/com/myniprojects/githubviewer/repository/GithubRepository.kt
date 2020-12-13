package com.myniprojects.githubviewer.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.network.ResponseState
import com.myniprojects.githubviewer.repository.mapper.NetworkToDomainReposMapper
import com.myniprojects.githubviewer.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubDataSource: GithubDataSource,
    private val mapper: NetworkToDomainReposMapper
)
{
    @ExperimentalCoroutinesApi
    fun getPublicReposSearchStream(query: String): Flow<PagingData<GithubRepo>> =
            Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = NETWORK_PAGE_SIZE
                ),
                pagingSourceFactory = { githubDataSource.getGithubPagingSourcePublicRepos(query) }
            ).flow.map { pagingData ->
                pagingData.map { responseItem ->
                    mapper.mapToNewModel(responseItem)
                }
            }


    @ExperimentalCoroutinesApi
    fun getUserReposSearchStream(username: String): Flow<PagingData<GithubRepo>> =
            Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = NETWORK_PAGE_SIZE
                ),
                pagingSourceFactory = { githubDataSource.getGithubPagingSourceUserRepos(username) }
            ).flow.map { pagingData ->
                pagingData.map { responseItem ->
                    mapper.mapToNewModel(responseItem)
                }
            }


    fun getUser(username: String): Flow<ResponseState<GithubUser>> =
            githubDataSource.getUser(username)
}
