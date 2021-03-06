package com.myniprojects.githubviewer.ui.composes

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.model.GithubRepo
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Composable
fun RepoList(
    repos: Flow<PagingData<GithubRepo>>,
    onDoubleCLick: (GithubRepo) -> Unit,
    modifier: Modifier = Modifier,
    error404: (@Composable () -> Unit)? = null
)
{
    val lazyRepoItems: LazyPagingItems<GithubRepo> = repos.collectAsLazyPagingItems()
    val context = ContextAmbient.current

    LazyColumn(
        contentPadding = PaddingValues(
            top = dimensionResource(id = R.dimen.default_tiny_padding),
            bottom = dimensionResource(id = R.dimen.default_tiny_padding)
        ),
        modifier = modifier
    ) {
        items(lazyRepoItems) { githubRepo ->
            githubRepo?.let {
                GithubRepoItem(
                    repo = it,
                    onCLick = { url ->
                        Timber.d("Repo clicked with URL $url")
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try
                        {
                            context.startActivity(intent)
                        }
                        catch (e: ActivityNotFoundException)
                        {
                            Timber.d("Cannot open web")
                        }
                    },
                    onDoubleCLick = onDoubleCLick
                )
            }
        }

        applyLoadState(
            lazyRepoItems = lazyRepoItems,
            R.string.user_np_repos,
            error404 = error404

        )

    }
}

fun <T> LazyListScope.applyLoadState(
    lazyRepoItems: LazyPagingItems<T>,
    @StringRes emptyResult: Int,
    error404: (@Composable () -> Unit)? = null
) where T : Any
{
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

                if (error404 != null && (e.error as retrofit2.HttpException).code() == 404)
                {
                    item {
                        error404()
                    }
                }
                else
                {
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
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
            loadState.prepend is LoadState.NotLoading && loadState.prepend.endOfPaginationReached && itemCount == 0 ->
            {
                item {
                    EmptyItem(
                        message = stringResource(id = emptyResult),
                    )
                }
            }
        }
    }
}

