package com.myniprojects.githubviewer.repository

import androidx.paging.PagingSource
import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.network.GithubService
import com.myniprojects.githubviewer.network.ResponseItem
import com.myniprojects.githubviewer.network.ResponseState
import com.myniprojects.githubviewer.repository.mapper.NetworkToDomainUserMapper
import com.myniprojects.githubviewer.utils.Constants.GITHUB_STARTING_PAGE_INDEX
import com.myniprojects.githubviewer.utils.Constants.IN_QUALIFIER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubDataSource @Inject constructor(
    private val githubService: GithubService,
    private val networkToDomainUserMapper: NetworkToDomainUserMapper,
)
{
    fun getGithubPagingSourcePublicRepos(query: String): PagingSource<Int, ResponseItem>
    {
        return object : PagingSource<Int, ResponseItem>()
        {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseItem>
            {
                val apiQuery = query + IN_QUALIFIER
                val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
                return try
                {
                    val response = githubService.searchPublicRepos(
                        apiQuery,
                        position,
                        params.loadSize
                    )
                    val repos = response.items

                    LoadResult.Page(
                        data = repos,
                        prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (repos.isEmpty()) null else position + 1
                    )
                }
                catch (exception: IOException)
                {
                    LoadResult.Error(exception)
                }
                catch (exception: HttpException)
                {
                    LoadResult.Error(exception)
                }
            }
        }
    }

    fun getGithubPagingSourceUserRepos(username: String): PagingSource<Int, ResponseItem>
    {
        return object : PagingSource<Int, ResponseItem>()
        {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseItem>
            {
                val position = params.key ?: GITHUB_STARTING_PAGE_INDEX

                return try
                {
                    val response = githubService.searchUserRepos(
                        username,
                        position,
                        params.loadSize
                    )

                    LoadResult.Page(
                        data = response,
                        prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (response.isEmpty()) null else position + 1
                    )
                }
                catch (exception: IOException)
                {
                    LoadResult.Error(exception)
                }
                catch (exception: HttpException)
                {
                    LoadResult.Error(exception)
                }
            }
        }
    }

    // not sure if it has to be suspend or not, I think not
    fun getUser(username: String): Flow<ResponseState<GithubUser>> =
            flow {
                emit(ResponseState.Loading)
                try
                {
                    val userResponse = githubService.searchUser(username)

                    emit(ResponseState.Success(networkToDomainUserMapper.mapToNewModel(userResponse)))
                }
                catch (e: IOException)
                {
                    emit(ResponseState.Error(e))
                }
                catch (e: HttpException)
                {
                    emit(ResponseState.Error(e))
                }
            }
}