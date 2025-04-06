package com.example.smartinventory.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartinventory.presentation.add.AddProductScreen
import com.example.smartinventory.presentation.add.UpdateProductScreen
import com.example.smartinventory.presentation.details.ProductDetailsScreen
import com.example.smartinventory.presentation.home.DashBoardScreen
import com.example.smartinventory.presentation.home.DashBoardViewModel
import com.example.smartinventory.presentation.reports.ReportsScreen
import com.example.smartinventory.utils.Route
import com.example.smartinventory.utils.base.BaseViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val baseViewModel: BaseViewModel = hiltViewModel()
    val dashBoardViewModel: DashBoardViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Route.DASHBOARD_SCREEN) {
        composable(Route.DASHBOARD_SCREEN) {
            DashBoardScreen(modifier, navController, baseViewModel, dashBoardViewModel)
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
            ProductDetailsScreen(productId = productId, navController = navController, baseViewModel)
        }

        composable(Route.ADD_PRODUCT_SCREEN) {
            AddProductScreen(modifier, navController, baseViewModel)
        }

        composable(
            route = "updateFoodScreen/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments!!.getInt("productId")
            UpdateProductScreen(productId = productId,
                navController = navController,
                baseViewModel = baseViewModel)
        }

        composable(Route.REPORT_SCREEN) {
            ReportsScreen()
        }

    }

}