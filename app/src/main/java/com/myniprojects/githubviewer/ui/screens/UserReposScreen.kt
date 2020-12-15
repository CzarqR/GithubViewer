package com.myniprojects.githubviewer.ui.screens

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.network.ResponseState
import com.myniprojects.githubviewer.ui.composes.*
import com.myniprojects.githubviewer.ui.theme.ThemedPreview
import com.myniprojects.githubviewer.vm.UserReposViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
            viewModel.currentRepoResult
        )
    }

    val (user, setUser) = remember {
        mutableStateOf(
            viewModel.currentUserResult
        )
    }


    val coroutineScope = rememberCoroutineScope()

    val emptyQueryMessage = stringResource(id = R.string.empty_query)
    val ok = stringResource(id = R.string.ok)


    fun initSearch(username: String)
    {
        setUser(viewModel.searchUser(username = username))
    }

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
                            Timber.d("Not blank: $query")
                            initSearch(username = query)
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
        if (repos == null && user == null)
        {
            EmptySearchScreen(stringResource(id = R.string.search_info_user))
        }
        else
        {
            Column {

                if (user != null)
                {
                    UserState(
                        userFlow = user,
                        success = {
                            setRepos(viewModel.searchUserRepo(username = it))
                        })
                }

                if (repos != null)
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
    }
}

@Composable
fun UserState(
    userFlow: Flow<ResponseState<GithubUser>>,
    success: (String) -> Unit
)
{
    val user = userFlow.collectAsState(initial = ResponseState.Sleep)

    Timber.d(user.value.toString())

    when (val x = user.value)
    {
        is ResponseState.Success ->
        {
            User(user = x.data)
            success(x.data.login)
        }
        is ResponseState.Error ->
        {
            ErrorItem(
                message = x.exception.localizedMessage
                        ?: stringResource(id = R.string.cannot_load_data),
                onClickRetry = {})
        }
        ResponseState.Loading ->
        {
            LoadingItem()
        }
        ResponseState.Sleep ->
        {
            // collapsed
        }
    }
}

@Composable
fun User(
    user: GithubUser,
    modifier: Modifier = Modifier
)
{

    Surface(
        modifier = modifier.padding(
            top = dimensionResource(id = R.dimen.default_small_padding)
        ),
        color = MaterialTheme.colors.surface,
        elevation = dimensionResource(id = R.dimen.card_elevation) / 2
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_small_padding))
        ) {

            GlideImage(
                imageModel = user.avatarUrl,
                modifier = Modifier
                    .preferredSize(dimensionResource(id = R.dimen.avatar_size))
                    .clip(RoundedCornerShape(50)),
                circularRevealedEnabled = true,
                circularRevealedDuration = 1000,
            )

            ScrollableColumn(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.avatar_size))
                    .weight(1f)
                    .padding(start = dimensionResource(id = R.dimen.default_medium_padding)),
            ) {

                with(user) {

                    Text(
                        text = name ?: login,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )


                    bio?.let {
                        StyledTextUserBody(text = it)
                    }

                    StyledTextUserBody(
                        text = stringResource(
                            id = R.string.joined_format,
                            createdAt.substring(0..9)
                        )
                    )

                    company?.let {
                        if (it.isNotBlank())
                        {
                            StyledTextUserBody(
                                text = stringResource(id = R.string.company_format, it)
                            )
                        }
                    }

                    location?.let {
                        if (it.isNotBlank())
                        {
                            StyledTextUserBody(
                                text = stringResource(id = R.string.location_format, it)
                            )
                        }
                    }
                    email?.let {
                        if (it.isNotBlank())
                        {
                            StyledTextUserBody(
                                text = stringResource(id = R.string.email_format, it)
                            )
                        }
                    }
                    blog?.let {
                        if (it.isNotBlank())
                        {
                            StyledTextUserBody(
                                text = stringResource(id = R.string.blog_format, it)
                            )
                        }
                    }

                    twitter?.let {
                        if (it.isNotBlank())
                        {
                            StyledTextUserBody(
                                text = stringResource(id = R.string.twitter_format, it)
                            )
                        }
                    }

                    StyledTextUserBody(
                        text = stringResource(
                            id = R.string.followers_format,
                            followers
                        )
                    )

                    StyledTextUserBody(
                        text = stringResource(
                            id = R.string.following_format,
                            following
                        )
                    )

                    StyledTextUserBody(
                        text = stringResource(
                            id = R.string.public_repos_format,
                            publicRepos
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StyledTextUserBody(text: String)
{
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.default_tiny_padding))
    )
}


@Preview(showBackground = true)
@Composable
fun UserPrev()
{
    ThemedPreview {
        User(
            GithubUser(
                login = "florina-muntenescu",
                avatarUrl = "https://avatars1.githubusercontent.com/u/2998890?v=4",
                url = "https://api.github.com/users/florina-muntenescu",
                name = "Florina Muntenescu",
                company = "Google",
                blog = "https://medium.com/@florina.muntenescu",
                location = "London, United Kingdom",
                email = "FMuntenescu@gmail.com",
                bio = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. ",
                twitter = "FMuntenescu",
                publicRepos = 15,
                followers = 3289,
                following = 0,
                createdAt = "2012-12-09T01:27:35Z",
            )
        )
    }
}