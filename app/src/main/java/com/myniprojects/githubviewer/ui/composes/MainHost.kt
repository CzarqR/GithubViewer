package com.myniprojects.githubviewer.ui.composes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.vm.LikedReposViewModel
import com.myniprojects.githubviewer.vm.PublicReposViewModel
import com.myniprojects.githubviewer.vm.UserReposViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun MainHost(
    publicReposViewModel: PublicReposViewModel,
    userReposViewModel: UserReposViewModel,
    likedReposViewModel: LikedReposViewModel,
    modifier: Modifier = Modifier
)
{
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val screens = listOf(
        BottomNavigationScreens.Repos,
        BottomNavigationScreens.Users,
        BottomNavigationScreens.Liked,
    )

    Scaffold(
        modifier = modifier,
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(asset = vectorResource(id = screen.icon))
                        },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.popBackStack(navController.graph.startDestination, false)

                            if (currentRoute != screen.route)
                            {
                                navController.navigate(screen.route)
                            }
                        }
                    )
                }
            }
        }
    ) {
        MainBody(
            navController = navController,
            publicReposViewModel = publicReposViewModel,
            userReposViewModel = userReposViewModel,
            likedReposViewModel = likedReposViewModel,
            snackbarHostState = snackbarHostState
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun MainBody(
    navController: NavHostController,
    publicReposViewModel: PublicReposViewModel,
    userReposViewModel: UserReposViewModel,
    likedReposViewModel: LikedReposViewModel,
    snackbarHostState: SnackbarHostState,
    publicReposState: LazyListState = rememberLazyListState()
)
{
    Timber.d("STATE. HASH ${publicReposState.hashCode()} MainBody ${publicReposState.firstVisibleItemIndex} ${publicReposState.firstVisibleItemScrollOffset}")


    NavHost(
        navController,
        startDestination = BottomNavigationScreens.Repos.route
    ) {
        composable(route = BottomNavigationScreens.Repos.route) {
            PublicReposScreen(
                viewModel = publicReposViewModel,
                snackbarHostState = snackbarHostState,
                state = publicReposState //state doesn't work, it isn't saved after changing screen at BottomNav
            )
        }

        composable(
            route = BottomNavigationScreens.Users.route
        ) {
            UserReposScreen(
                viewModel = userReposViewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable(
            route = BottomNavigationScreens.Liked.route
        ) {
            LikedReposScreen(
                viewModel = likedReposViewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int
)
{
    object Repos : BottomNavigationScreens(
        "REPOS_ROUTE",
        R.string.repos,
        R.drawable.ic_git_repo
    )

    object Users : BottomNavigationScreens(
        "USER_ROUTE",
        R.string.users,
        R.drawable.ic_outline_group_24
    )

    object Liked : BottomNavigationScreens(
        "LIKED_ROUTE",
        R.string.liked,
        R.drawable.ic_outline_thumb_up_24
    )
}