package com.myniprojects.githubviewer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myniprojects.githubviewer.model.GithubRepo

@Database(
    entities = [
        GithubRepo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract val githubRepoDao: GithubRepoDao
}