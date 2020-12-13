package com.myniprojects.githubviewer.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.ui.composes.EmptyItem
import com.myniprojects.githubviewer.ui.composes.RepoList
import com.myniprojects.githubviewer.vm.UserReposViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun UserReposScreen(
    viewModel: UserReposViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
)
{

    val (query, setQuery) = remember { mutableStateOf(viewModel.currentQuery ?: "") }
    val (repos, setRepos) = remember {
        mutableStateOf(
            viewModel.currentRepoResult ?: viewModel.searchUserRepo(username = "")
        )
    }

    val (user, setUser) = remember {
        mutableStateOf(
            viewModel.currentUserResult ?: viewModel.searchUser("CzarqR")
        )
    }

    val x = user?.collectAsState(initial = null)

    if (x?.value != null)
    {
        Timber.d(x.value.toString())
    }


    val coroutineScope = rememberCoroutineScope()

    val emptyQueryMessage = stringResource(id = R.string.empty_query)
    val ok = stringResource(id = R.string.ok)

    Column(
        modifier = modifier
    ) {
        Surface(
            elevation = 4.dp,
            color = MaterialTheme.colors.surface
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = setQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.default_small_padding)),
                label = {
                    Text(text = stringResource(id = R.string.search_user_label))
                },
                maxLines = 1,
                isErrorValue = query.isBlank(),
                textStyle = AmbientTextStyle.current.copy(color = MaterialTheme.colors.onSurface),
                activeColor = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                onImeActionPerformed = { imeAction: ImeAction, softwareKeyboardController: SoftwareKeyboardController? ->
                    if (imeAction == ImeAction.Search)
                    {
                        setQuery(query.trim())
                        Timber.d("Search query: $query")

                        if (query.isNotBlank())
                        {
                            setRepos(viewModel.searchUserRepo(username = query))
                            softwareKeyboardController?.hideSoftwareKeyboard()
                        }
                        else
                        {
                            /*
                            it would be better if soft keyboard didn't hide everytime
                            user enter blank query. Snacbar should appear over keyboard
                             */
                            softwareKeyboardController?.hideSoftwareKeyboard()

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = emptyQueryMessage,
                                    actionLabel = ok,
                                    duration = SnackbarDuration.Long
                                )
                            }
                        }
                    }
                },
                trailingIcon = {
                    Icon(asset = vectorResource(id = R.drawable.ic_outline_group_24))
                }
            )
        }
        if (repos == null)
        {
            EmptySearchScreen(stringResource(id = R.string.search_info_user))
        }
        else
        {
            RepoList(
                repos = repos,
                error404 = {
                    EmptyItem(message = stringResource(id = R.string.user_not_found))
                }
            )
        }
    }
}