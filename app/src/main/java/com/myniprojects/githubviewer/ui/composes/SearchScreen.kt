package com.myniprojects.githubviewer.ui.composes

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.myniprojects.githubviewer.model.RepoListModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RepoList(repos: Flow<PagingData<RepoListModel>>)
{
    val lazyRepoItems: LazyPagingItems<RepoListModel> = repos.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyRepoItems) { repoListModel: RepoListModel? ->
            repoListModel?.let {
                when (it)
                {
                    is RepoListModel.RepoItem ->
                    {
                        Text(
                            text = it.githubRepo.stars.toString()
                        )
                    }
                    is RepoListModel.RepoSeparatorItem ->
                    {
                        Text(
                            text = "${it.before} - ${it.after}",
                            fontSize = 30.sp
                        )
                    }
                }
            }
        }

        lazyRepoItems.apply {
            when
            {
                loadState.refresh is LoadState.Loading ->
                {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading ->
                {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error ->
                {
                    val e = lazyRepoItems.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error ->
                {
                    val e = lazyRepoItems.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}