package com.myniprojects.githubviewer.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.myniprojects.githubviewer.R
import com.myniprojects.githubviewer.ui.screens.LikedReposScreen
import com.myniprojects.githubviewer.ui.screens.PublicReposScreen
import com.myniprojects.githubviewer.ui.screens.UserReposScreen
import com.myniprojects.githubviewer.vm.LikedReposViewModel
import com.myniprojects.githubviewer.vm.PublicReposViewModel
import com.myniprojects.githubviewer.vm.UserReposViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    val screens = remember {
        listOf(
            BottomNavigationScreens.Repos,
            BottomNavigationScreens.Users,
            BottomNavigationScreens.Liked,
        )
    }


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
                        },
                        alwaysShowLabels = false
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
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .padding(bottom = 56.dp) // height of bottom navigation , value is private
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
    modifier: Modifier = Modifier
)
{
    NavHost(
        navController,
        startDestination = BottomNavigationScreens.Repos.route
    ) {
        composable(route = BottomNavigationScreens.Repos.route) {
            PublicReposScreen(
                viewModel = publicReposViewModel,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            )
        }

        composable(
            route = BottomNavigationScreens.Users.route
        ) {
            UserReposScreen(
                viewModel = userReposViewModel,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            )
        }

        composable(
            route = BottomNavigationScreens.Liked.route
        ) {
            LikedReposScreen(
                viewModel = likedReposViewModel,
                snackbarHostState = snackbarHostState,
                modifier = modifier
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