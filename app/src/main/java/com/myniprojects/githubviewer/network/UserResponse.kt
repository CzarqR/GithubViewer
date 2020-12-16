package com.myniprojects.githubviewer.network

import com.squareup.moshi.Json

data class UserResponse(
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "html_url") val url: String,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    @Json(name = "twitter_username") val twitter: String?,
    @Json(name = "public_repos") val publicRepos: Int,
    val followers: Int,
    val following: Int,
    @Json(name = "created_at") val createdAt: String
)
