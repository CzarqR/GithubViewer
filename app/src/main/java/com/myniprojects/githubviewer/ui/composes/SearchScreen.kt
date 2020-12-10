package com.myniprojects.githubviewer.ui.composes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.model.RepoListModel
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchScreen(
    repos: Flow<PagingData<RepoListModel>>,
    modifier: Modifier = Modifier
)
{
    val (query, setQuery) = remember { mutableStateOf("Android") }

    Scaffold(
        modifier = modifier
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
                    activeColor = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant

                )
            }

            RepoList(
                repos = repos
            )
        }

    }
}