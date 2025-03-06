package com.tricode.easyinventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSellingProductsScreen(navController: NavController, viewModel: InventoryViewModel) {

    val topSellingProducts by viewModel.topSellingProducts.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Más Vendidos", color = Color.White, style = MaterialTheme.typography.titleLarge)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(8.dp)) {
            items(topSellingProducts) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF652C12))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Producto: ${product.productName}", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Text(text = "Unidades Vendidas: ${product.totalQuantitySold}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                        Text(text = "Total Generado: $${product.totalRevenue}", style = MaterialTheme.typography.bodyMedium, color = Color.White) // 🔹 Mostrar total de dinero vendido
                    }
                }
            }
        }
    }
}


@Composable
fun SaleItem(sale: SaleRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF652C12))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Producto: ${sale.productName}", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Text(text = "Unidades Vendidas: ${sale.quantitySold}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}
