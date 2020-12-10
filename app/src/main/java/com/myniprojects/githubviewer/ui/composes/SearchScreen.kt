package com.myniprojects.githubviewer.ui.composes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.ui.theme.ThemedPreview
import com.myniprojects.githubviewer.vm.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber


@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
)
{
    val (query, setQuery) = remember { mutableStateOf("") }
    val (repos, setRepos) = remember { mutableStateOf(viewModel.searchRepo(query = "")) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val emptyQueryMessage = stringResource(id = R.string.empty_query)
    val ok = stringResource(id = R.string.ok)

    Scaffold(
        modifier = modifier,
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
    ) {
        Column {

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
                        Text(text = stringResource(id = R.string.search_label))
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
                                setRepos(viewModel.searchRepo(query = query))
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
                        Icon(asset = vectorResource(id = R.drawable.ic_git_repo))
                    }
                )
            }
            if (repos == null)
            {
                EmptySearchScreen()
            }
            else
            {
                RepoList(
                    repos = repos
                )
            }

        }
    }
}


@Composable
fun EmptySearchScreen(
    modifier: Modifier = Modifier
)
{
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.default_small_padding))
    ) {
        Text(
            text = stringResource(id = R.string.search_info),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
        Icon(
            asset = vectorResource(id = R.drawable.ic_git),
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_medium_padding))
        )
    }
}

@Preview
@Composable
fun EmptySearchScreenPrev()
{
    ThemedPreview {
        EmptySearchScreen()
    }
}