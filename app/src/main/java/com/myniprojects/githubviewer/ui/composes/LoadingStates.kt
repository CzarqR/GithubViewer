package com.myniprojects.githubviewer.ui.composes


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.ui.theme.ThemedPreview

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingViewPrev()
{
    ThemedPreview {
        LoadingView()
    }
}

@Composable
fun LoadingItem(
    modifier: Modifier = Modifier
)
{
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingItemPrev()
{
    ThemedPreview {
        LoadingItem()
    }
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
)
{
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.error
        )
        OutlinedButton(
            onClick = onClickRetry,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.default_small_padding))
        ) {
            Text(text = stringResource(id = R.string.try_again))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorItemPrev()
{
    ThemedPreview {
        Column {
            ErrorItem(
                message = "It is a long established fact that a reader will be distracted.",
                onClickRetry = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            ErrorItem(
                message = "The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here',",
                onClickRetry = {}
            )
        }
    }
}


@Composable
fun EmptyItem(
    message: String,
    modifier: Modifier = Modifier,
)
{
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyItemPrev()
{
    ThemedPreview {
        Column {
            EmptyItem(
                message = stringResource(id = R.string.empty_result_repos),
            )
        }
    }
}


@Composable
fun EmptySearchScreen(
    text: String,
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
            text = text,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
        Icon(
            asset = vectorResource(id = R.drawable.ic_git),
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_big_padding))
        )
    }
}

@Preview
@Composable
fun EmptySearchScreenPrev()
{
    ThemedPreview {
        EmptySearchScreen(stringResource(id = R.string.search_info_repos))
    }
}