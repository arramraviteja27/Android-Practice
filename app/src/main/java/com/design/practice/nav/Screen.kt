package com.design.practice.nav

sealed class Screen(
    val route : String
) {

    data object RestaurantList : Screen("restaurant_list")

    data object RestaurantDetails : Screen("restaurant_details/{placeId}") {

        fun createRoute(placeId : String) : String {

            return  "restaurant_details/$placeId"
        }
    }
}