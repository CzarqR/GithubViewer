package com.myniprojects.githubviewer.db

import androidx.room.*
import com.myniprojects.githubviewer.model.GithubRepo
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubRepoDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(githubRepo: GithubRepo)

    @Delete
    suspend fun deleteRepo(githubRepo: GithubRepo)

    @Query("SELECT * FROM github_repos ORDER BY stars DESC")
    fun getRepos(): Flow<List<GithubRepo>>

}