package com.myniprojects.githubviewer.network

import com.squareup.moshi.Json

data class ResponseItem(
    val id: Long,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val description: String?,
    @Json(name = "html_url") val url: String,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "forks_count") val forks: Int,
    val language: String?
)