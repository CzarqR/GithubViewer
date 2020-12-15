package com.myniprojects.githubviewer.db

import androidx.paging.PagingSource
import androidx.room.*
import com.myniprojects.githubviewer.model.GithubRepo

@Dao
interface GithubRepoDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(githubRepo: GithubRepo)

    @Delete
    suspend fun deleteRepo(githubRepo: GithubRepo)

//    @Query("SELECT * FROM github_repos ORDER BY stars DESC")
//    fun getReposPaged(): PagingSource<Int, GithubRepo>


}