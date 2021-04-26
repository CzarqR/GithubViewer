package com.myniprojects.githubviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_users")
data class GithubUser(
    @PrimaryKey val login: String,
    val avatarUrl: String,
    val url: String,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val twitter: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val createdAt: String
)