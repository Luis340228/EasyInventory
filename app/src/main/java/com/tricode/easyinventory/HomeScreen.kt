 package com.tricode.easyinventory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "EasyInventory",
                        color = Color.White,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF652C12)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(menuItems) { item ->
                    MenuItemCard(item, navController)
                }
            }
        }
    }
}

data class MenuItem(val title: String, val icon: ImageVector, val route: String)

val menuItems = listOf(
    MenuItem(title = "Productos", Icons.Outlined.ShoppingCart, route = "product_list"),
    MenuItem(title = "Ventas", Icons.Outlined.PointOfSale, route = "sales"),
    MenuItem(title = "Resumen", Icons.Outlined.List, route = "inventory_count"),
    MenuItem(title = "Más Vendidos", Icons.Filled.Star, route = "top_selling_products"),
    MenuItem(title = "Por Caducar", Icons.Outlined.DateRange, route = "expiring_products"),
    MenuItem(title = "Caducados", Icons.Filled.Warning, route = "expired_products"),
    MenuItem(title = "Proveedores", Icons.Outlined.Business, route = "provider_list"),
    MenuItem(title = "Categorías", Icons.Outlined.Category, route = "categories"),
    MenuItem(title = "Marcas", Icons.Outlined.LocalOffer, route = "brand_list")
 )

@Composable
fun MenuItemCard(item: MenuItem, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate(item.route) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF652C12)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}
