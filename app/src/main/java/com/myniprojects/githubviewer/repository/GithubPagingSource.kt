package com.myniprojects.githubviewer.repository

import androidx.paging.PagingSource
import com.myniprojects.githubviewer.network.GithubService
import com.myniprojects.githubviewer.network.ResponseItem
import com.myniprojects.githubviewer.utils.Constants.GITHUB_STARTING_PAGE_INDEX
import com.myniprojects.githubviewer.utils.Constants.IN_QUALIFIER
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubPaging @Inject constructor(
    private val githubService: GithubService,
)
{

    fun getGithubPagingSource(query: String): PagingSource<Int, ResponseItem>
    {
        return object : PagingSource<Int, ResponseItem>()
        {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseItem>
            {
                val apiQuery = query + IN_QUALIFIER
                val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
                return try
                {
                    val response = githubService.searchRepos(apiQuery, position, params.loadSize)
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
}