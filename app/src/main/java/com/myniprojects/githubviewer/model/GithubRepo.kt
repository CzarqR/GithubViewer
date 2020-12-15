package com.myniprojects.githubviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_repos")
data class GithubRepo(
    @PrimaryKey val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stars: Int,
    val forks: Int,
    val language: String?
)