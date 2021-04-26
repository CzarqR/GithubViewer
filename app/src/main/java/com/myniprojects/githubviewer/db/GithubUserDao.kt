package com.myniprojects.githubviewer.db

import androidx.room.*
import com.myniprojects.githubviewer.model.GithubUser
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(githubUser: GithubUser)

    @Delete
    suspend fun deleteUser(githubUser: GithubUser)

    @Query("SELECT * FROM github_users ORDER BY name ASC")
    fun getUsers(): Flow<List<GithubUser>>

}