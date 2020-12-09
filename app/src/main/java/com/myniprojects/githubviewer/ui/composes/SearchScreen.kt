package com.myniprojects.githubviewer.ui.composes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.ui.tooling.preview.Preview
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.model.RepoListModel
import com.myniprojects.githubviewer.ui.theme.ThemedPreview
import com.myniprojects.githubviewer.ui.theme.matteBlue
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
                        GithubRepoItem(repo = it.githubRepo)
                    }
                    is RepoListModel.RepoSeparatorItem ->
                    {
                        SeparatorGithubItem(separator = it)
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

@Composable
fun GithubRepoItem(
    repo: GithubRepo,
    modifier: Modifier = Modifier
)
{
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.default_small_padding),
                end = dimensionResource(id = R.dimen.default_small_padding),
                top = dimensionResource(id = R.dimen.default_small_padding) / 2,
                bottom = dimensionResource(id = R.dimen.default_small_padding) / 2
            ),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_small_padding))
        ) {
            Text(
                text = repo.fullName,
                style = MaterialTheme.typography.h6,
                color = matteBlue
            )
            Text(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.default_small_padding),
                        bottom = dimensionResource(id = R.dimen.default_small_padding)
                    ),
                text = repo.description ?: "",
                maxLines = 3,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )

            Providers(AmbientContentAlpha provides ContentAlpha.medium) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = repo.language ?: "",
                        modifier = Modifier
                            .weight(1f),
                        style = MaterialTheme.typography.body2
                    )

                    Icon(asset = vectorResource(id = R.drawable.ic_git_star))
                    Text(
                        text = repo.stars.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = R.dimen.default_tiny_padding),
                                end = dimensionResource(id = R.dimen.default_small_padding)
                            )
                    )

                    Icon(asset = vectorResource(id = R.drawable.ic_git_branch))
                    Text(
                        text = repo.forks.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = R.dimen.default_tiny_padding),
                            )
                    )


                }
            }
        }
    }
}

@Preview
@Composable
fun GithubRepoItemPrev()
{
    ThemedPreview {
        GithubRepoItem(
            repo = GithubRepo(
                id = 0,
                name = "android-architecture",
                fullName = "google/android-architecture",
                description = "A collection of samples to discuss and showcase different architectural tools and patterns for Android apps.",
                url = "https://github.com/",
                stars = 38139,
                forks = 1391,
                language = "Kotlin"
            )
        )
    }
}


@Composable
fun SeparatorGithubItem(
    separator: RepoListModel.RepoSeparatorItem,
    modifier: Modifier = Modifier
)
{
    Row(
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.default_tiny_padding),
                bottom = dimensionResource(id = R.dimen.default_tiny_padding)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SepDivider()

        Text(
            text = separator.after.getRounded().formatWithSpaces(),
            style = MaterialTheme.typography.overline.copy(
                fontSize = 14.sp
            )
        )

        SepDivider()
    }
}

@Composable
fun RowScope.SepDivider()
{
    Divider(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = dimensionResource(id = R.dimen.default_small_padding),
                end = dimensionResource(id = R.dimen.default_small_padding),
            ),
        color = MaterialTheme.colors.secondary,
        thickness = 0.5.dp
    )
}

@Preview
@Composable
fun SeparatorGithubItemPrev()
{
    ThemedPreview {
        SeparatorGithubItem(separator = RepoListModel.RepoSeparatorItem(19_319))
    }
}

private fun Int.getRounded(): Int
{
    var i = 100_000
    while (i > 1)
    {
        if (this >= i)
        {
            return (this / i) * i
        }
        i /= 10
    }
    throw IllegalArgumentException("Wrong value $this. Separator should be bigger or equal 10")
}

fun Int.formatWithSpaces(): String
{
    val sb = StringBuilder().append(this)

    for (i in sb.length - 3 downTo 1 step 3)
    {
        sb.insert(i, ' ')
    }

    return sb.toString()
}

