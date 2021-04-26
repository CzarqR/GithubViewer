package com.myniprojects.githubviewer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.GithubUser

@Database(
    entities = [
        GithubRepo::class,
        GithubUser::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract val githubRepoDao: GithubRepoDao
    abstract val githubUserDao: GithubUserDao
}