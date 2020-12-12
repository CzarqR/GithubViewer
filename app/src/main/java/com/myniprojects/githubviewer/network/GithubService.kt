package com.myniprojects.githubviewer.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService
{
    /*
    List public repositories: https://docs.github.com/en/free-pro-team@latest/rest/reference/search#search-repositories
     */
    @GET("search/repositories?sort=stars")
    suspend fun searchPublicRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): GithubResponse

    /*
    List repositories for a user: https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#list-repositories-for-a-user
     */
    @GET("users/{username}/repos?sort=updated")
    suspend fun searchUserRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): List<ResponseItem>
}