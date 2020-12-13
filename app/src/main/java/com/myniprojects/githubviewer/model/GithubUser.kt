package com.myniprojects.githubviewer.model

data class GithubUser(
    val login: String,
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