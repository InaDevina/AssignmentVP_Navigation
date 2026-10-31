package com.vp.assignmentvp_navigation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vp.assignmentvp_navigation.ui.screen.ClubProjectScreen
import com.vp.assignmentvp_navigation.ui.screen.CommunitiesScreen
import com.vp.assignmentvp_navigation.ui.screen.DashboardScreen
import com.vp.assignmentvp_navigation.ui.screen.ProjectDetailScreen
import com.vp.assignmentvp_navigation.ui.theme.BlueLight
import com.vp.assignmentvp_navigation.ui.theme.BluePrimary
import com.vp.assignmentvp_navigation.ui.viewmodel.CampusViewModel


sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Communities : Screen("communities")
    object ClubProjects : Screen("club_projects/{clubId}") {
        fun createRoute(clubId: String) = "club_projects/$clubId"
    }
    object ProjectDetail : Screen("project_detail/{projectId}") {
        fun createRoute(projectId: String) = "project_detail/$projectId"
    }
}

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(Screen.Dashboard.route, "Dashboard", Icons.Default.Dashboard)
    object Communities : BottomNavItem(Screen.Communities.route, "Communities", Icons.Default.Groups)
}

@Composable
fun CampusApp(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: CampusViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isTopLevelRoute = currentDestination?.route in listOf(
        Screen.Dashboard.route,
        Screen.Communities.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isTopLevelRoute) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    val items = listOf(
                        BottomNavItem.Dashboard,
                        BottomNavItem.Communities
                    )

                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BluePrimary,
                                selectedTextColor = BluePrimary,
                                indicatorColor = BlueLight
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = sharedViewModel,
                    onClubClick = { clubId ->
                        navController.navigate(Screen.ClubProjects.createRoute(clubId))
                    },
                    onProjectClick = { projectId ->
                        navController.navigate(Screen.ProjectDetail.createRoute(projectId))
                    }
                )
            }
            composable(route = Screen.Communities.route) {
                CommunitiesScreen(
                    viewModel = sharedViewModel,
                    onOpenClubProjects = { clubId ->
                        navController.navigate(Screen.ClubProjects.createRoute(clubId))
                    }
                )
            }
            composable(
                route = Screen.ClubProjects.route,
                arguments = listOf(
                    navArgument("clubId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val clubId = backStackEntry.arguments?.getString("clubId") ?: ""
                ClubProjectScreen(
                    clubId = clubId,
                    viewModel = sharedViewModel,
                    onBackClick = { navController.popBackStack() },
                    onProjectClick = { projectId ->
                        navController.navigate(Screen.ProjectDetail.createRoute(projectId))
                    }
                )
            }
            composable(
                route = Screen.ProjectDetail.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
                ProjectDetailScreen(
                    projectId = projectId,
                    viewModel = sharedViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
