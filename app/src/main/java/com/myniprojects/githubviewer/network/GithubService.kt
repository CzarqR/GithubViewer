package com.myniprojects.githubviewer.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService
{

    /*
    API docs: https://docs.github.com/en/free-pro-team@latest/rest/reference/search#search-repositories
     */
    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): GithubResponse
}