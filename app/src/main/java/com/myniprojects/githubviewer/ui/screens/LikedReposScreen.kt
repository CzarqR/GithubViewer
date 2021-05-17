package com.myniprojects.githubviewer.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.ui.composes.EmptySearchScreen
import com.myniprojects.githubviewer.ui.composes.GithubRepoItem
import com.myniprojects.githubviewer.ui.composes.LoadingItem
import com.myniprojects.githubviewer.vm.ItemType
import com.myniprojects.githubviewer.vm.LikedReposViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun LikedReposScreen(
    viewModel: LikedReposViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
)
{
    val coroutineScope = rememberCoroutineScope()

    val (isHelpOpened, setIsDialogOpened) = remember { mutableStateOf(false) }
    val itemType = viewModel.selectedItemType.value

    val savedReposState = viewModel.savedRepos.collectAsState(initial = null)
    val savedRepos = savedReposState.value

    val savedUserState = viewModel.savedUsers.collectAsState(initial = null)
    val savedUsers = savedUserState.value

    val context = ContextAmbient.current

    if (isHelpOpened)
    {
        AlertDialog(
            onDismissRequest = { setIsDialogOpened(false) },
            text = {
                Text(
                    text = stringResource(id = R.string.info),
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                TextButton(onClick = { setIsDialogOpened(false) }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        )
    }

    Column(
        modifier = modifier
    ) {

        TopAppBar(
            title = {
                Text(
                    text = stringResource(
                        id = if (itemType == ItemType.REPOS) R.string.liked_repos else R.string.liked_users
                    )
                )
            },
            actions = {
                IconButton(onClick = {
                    viewModel.nextItemType()
                }) {
                    Icon(
                        asset = if (itemType == ItemType.REPOS) Icons.Outlined.PeopleOutline else Icons.Outlined.Book,
                        tint = AmbientContentColor.current.copy(alpha = 1f)
                    )
                }
                IconButton(onClick = {
                    setIsDialogOpened(true)
                }) {
                    Icon(
                        asset = Icons.Outlined.Info,
                        tint = AmbientContentColor.current.copy(alpha = 1f)
                    )
                }
            }
        )

        if (itemType == ItemType.REPOS) // show repositories
        {
            if (savedRepos != null)
            {
                if (savedRepos.isEmpty())
                {
                    EmptySearchScreen(text = stringResource(id = R.string.no_saved_repos))
                }
                else
                {
                    LazyColumnFor(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            top = dimensionResource(id = R.dimen.default_tiny_padding),
                            bottom = dimensionResource(id = R.dimen.default_tiny_padding)
                        ),
                        items = savedRepos
                    ) { githubRepo ->
                        GithubRepoItem(
                            repo = githubRepo,
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
                            onDoubleCLick = {
                                /**
                                 * in this point is a bug
                                 * item is deleted but image is not refreshed
                                 * so user which is below deleted user will have
                                 * avatar of the deleted user
                                 */
                                viewModel.deleteRepo(it)

                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.deleted_repo_format,
                                            it.fullName
                                        ),
                                        actionLabel = context.getString(R.string.ok),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }
            else
            {
                LoadingItem(modifier = Modifier.fillMaxHeight())
            }
        }
        else // show users
        {
            if (savedUsers != null)
            {
                if (savedUsers.isEmpty())
                {
                    EmptySearchScreen(text = stringResource(id = R.string.no_saved_user))
                }
                else
                {
                    LazyColumnFor(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            top = dimensionResource(id = R.dimen.default_tiny_padding),
                            bottom = dimensionResource(id = R.dimen.default_tiny_padding)
                        ),
                        items = savedUsers
                    ) { githubUser ->
                        User(user = githubUser) {
                            viewModel.deleteUser(it)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(
                                        R.string.deleted_user_format,
                                        it.login
                                    ),
                                    actionLabel = context.getString(R.string.ok),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }
            }
            else
            {
                LoadingItem(modifier = Modifier.fillMaxHeight())
            }
        }
    }
}