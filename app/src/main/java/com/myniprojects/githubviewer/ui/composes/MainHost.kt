package com.myniprojects.githubviewer.ui.composes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import com.myniprojects.githubviewer.vm.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun MainHost(
    viewModel: MainViewModel,
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
            viewModel = viewModel,
            snackbarHostState = snackbarHostState
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun MainBody(
    navController: NavHostController,
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState
)
{
    NavHost(
        navController,
        startDestination = BottomNavigationScreens.Repos.route
    ) {
        composable(route = BottomNavigationScreens.Repos.route) {
            SearchScreen(
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable(
            route = BottomNavigationScreens.Users.route
        ) {
            Text(text = "Second screen users")
        }

        composable(
            route = BottomNavigationScreens.Liked.route
        ) {
            Text(text = "Third screen liked")
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