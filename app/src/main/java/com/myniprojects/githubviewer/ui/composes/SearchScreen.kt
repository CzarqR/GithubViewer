package com.myniprojects.githubviewer.ui.composes

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.myniprojects.githubviewer.model.GithubRepo
import kotlinx.coroutines.flow.Flow

@Composable
fun RepoList(repos: Flow<PagingData<GithubRepo>>)
{
    val lazyRepoItems: LazyPagingItems<GithubRepo> = repos.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyRepoItems) { githubRepo: GithubRepo? ->
            githubRepo?.let {
                Text(text = it.name)
            }
        }
    }
}