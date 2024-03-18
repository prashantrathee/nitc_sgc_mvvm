package com.nitc.projectsgc.composable.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.nitc.projectsgc.composable.navigation.NavigationScreen
import com.nitc.projectsgc.composable.news.NewsViewModel
import com.nitc.projectsgc.composable.news.screens.NewsScreen
import com.nitc.projectsgc.composable.util.UserRole


fun NavGraphBuilder.newsGraph(
    topBarState: MutableState<Boolean>,
    navController: NavController,
    titleState: MutableState<String>,
    newsViewModel: NewsViewModel
) {
    navigation(
        startDestination = "${NavigationScreen.NewsScreen.route}/{userType}/{username}",
        route = "news/{userType}/{username}"
    ) {
        composable(route = "${NavigationScreen.NewsScreen.route}/{userType}/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("userType") { type = NavType.StringType },
            )) { navBackStackEntry ->
            topBarState.value = true
            titleState.value = stringResource(id = NavigationScreen.NewsScreen.resID)
            navBackStackEntry.arguments?.let { args ->
                val username = args.getString("username") ?: ""
                val userType = (args.getString("userType")?:"${UserRole.Student}").toInt()
                NewsScreen(
                    newsViewModel = newsViewModel,
                    userType = userType,
                    username = username
                )
            }
        }
    }
}