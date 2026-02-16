package fr.leboncoin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.leboncoin.ui.albums.detail.AlbumDetailScreen
import fr.leboncoin.ui.albums.list.AlbumsScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {

        composable(Routes.LIST) {
            AlbumsScreen (
                onItemClick = { item ->
                    navController.navigate(
                        Routes.detail(item.id, item.albumId)
                    )
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument(ARG_ID) {
                    type = NavType.IntType
                },
                navArgument(ARG_ALBUM_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            AlbumDetailScreen {
                navController.popBackStack()
            }
        }
    }
}
