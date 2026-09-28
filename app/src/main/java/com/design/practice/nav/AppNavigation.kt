package com.design.practice.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.design.practice.presentation.Details.RestaurantDetailsScreen
import com.design.practice.presentation.home.RestaurantScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(

        navController = navController,
        startDestination = Screen.RestaurantList.route
    ) {

          composable(
              route = Screen.RestaurantList.route
          ){

              RestaurantScreen(
                  onRestaurantClick = { placeId ->

                      navController.navigate(
                          Screen.RestaurantDetails
                              .createRoute(placeId)
                      )
                  }
              )
          }

        composable(
            route = Screen.RestaurantDetails.route,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val placeId =
                backStackEntry.arguments
                    ?.getString("placeId")
                    .orEmpty()

            RestaurantDetailsScreen(
                placeId = placeId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }




        }


}


