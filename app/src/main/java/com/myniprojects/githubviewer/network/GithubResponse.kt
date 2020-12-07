package com.myniprojects.githubviewer.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubResponse(
    @Json(name = "total_count") val total: Int = 0,
    val items: List<ResponseItem> = emptyList(),
)