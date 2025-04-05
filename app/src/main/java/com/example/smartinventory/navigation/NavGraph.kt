package com.example.smartinventory.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartinventory.presentation.details.ProductDetailsScreen
import com.example.smartinventory.presentation.home.DashBoardScreen
import com.example.smartinventory.utils.Route

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.DASHBOARD_SCREEN){
        composable(Route.DASHBOARD_SCREEN) {
            DashBoardScreen(modifier, navController)
        }

        composable(
            route = "productDetailsScreen/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments!!.getInt("productId")
            ProductDetailsScreen(productId = productId, navController = navController)
        }

    }

}