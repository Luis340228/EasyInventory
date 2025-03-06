package com.tricode.easyinventory

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun InventoryNavHost(navController: NavHostController, viewModel: InventoryViewModel) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("product_list") { ProductListScreen(navController, viewModel) }
        composable("add_product") { AddEditProductScreen(navController, viewModel, null) }
        composable("edit_product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            AddEditProductScreen(navController, viewModel, productId)
        }
        composable("expiring_products") { ExpiringProductsScreen(viewModel) }
        composable("expired_products") { ExpiredProductsScreen(viewModel) }
        composable("inventory_count") { InventoryCountScreen(navController, viewModel) }
        composable("sales") { SalesScreen(navController, viewModel) }
        composable("top_selling_products") { TopSellingProductsScreen(navController, viewModel) }
        composable(route = "provider_list") { ProviderListScreen(navController, viewModel) }
        composable(route = "add_provider") { AddEditProviderScreen(navController, viewModel, providerId = null) }
        composable(route = "edit_provider/{providerId}") { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId")
            AddEditProviderScreen(navController, viewModel, providerId)
        }
        composable("categories") { CategoryListScreen(navController, viewModel) }
        composable("add_category") { AddEditCategoryScreen(navController, viewModel, null) }
        composable(route = "edit_category/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            AddEditCategoryScreen(navController, viewModel, categoryId)
        }
        composable(route = "brand_list") { BrandListScreen(navController, viewModel) }
        composable(route = "add_brand") { AddEditBrandScreen(navController, viewModel, null) }
        composable(route = "edit_brand/{brandId}") { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            AddEditBrandScreen(navController, viewModel, brandId)
        }

    }
}
