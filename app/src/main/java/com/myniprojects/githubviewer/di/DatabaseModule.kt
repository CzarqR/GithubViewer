package com.myniprojects.githubviewer.di

import android.content.Context
import androidx.room.Room
import com.myniprojects.githubviewer.db.AppDatabase
import com.myniprojects.githubviewer.db.GithubRepoDao
import com.myniprojects.githubviewer.db.GithubUserDao
import com.myniprojects.githubviewer.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
{
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideRepoDao(db: AppDatabase): GithubRepoDao = db.githubRepoDao

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): GithubUserDao = db.githubUserDao
}