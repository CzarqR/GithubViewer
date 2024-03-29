package com.myniprojects.githubviewer.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.myniprojects.githubviewer.db.GithubRepoDao
import com.myniprojects.githubviewer.db.GithubUserDao
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.network.ResponseState
import com.myniprojects.githubviewer.repository.mapper.NetworkToDomainReposMapper
import com.myniprojects.githubviewer.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubDataSource: GithubDataSource,
    private val mapper: NetworkToDomainReposMapper,
    private val githubRepoDao: GithubRepoDao,
    private val githubUserDao: GithubUserDao
)
{
    // region network

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

    // endregion

    // region local

    val savedRepos: Flow<List<GithubRepo>> = githubRepoDao.getRepos()

    suspend fun insertRepo(githubRepo: GithubRepo) =
            withContext(Dispatchers.IO) {
                githubRepoDao.insertRepo(githubRepo)
            }

    suspend fun deleteRepo(githubRepo: GithubRepo) =
            withContext(Dispatchers.IO) {
                githubRepoDao.deleteRepo(githubRepo)
            }

    val savedUsers: Flow<List<GithubUser>> = githubUserDao.getUsers()

    suspend fun insertUser(githubUser: GithubUser) =
            withContext(Dispatchers.IO) {
                githubUserDao.insertUser(githubUser)
            }

    suspend fun deleteUser(githubUser: GithubUser) =
            withContext(Dispatchers.IO) {
                githubUserDao.deleteUser(githubUser)
            }

    // endregion
}
