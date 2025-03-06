package com.tricode.easyinventory

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavController, viewModel: InventoryViewModel) {
    val products by viewModel.products.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Productos",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_product") },
                containerColor = Color(0xFF652C12),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(products) { product ->
                ProductItem(product = product, onEdit = { productId ->
                    navController.navigate("edit_product/$productId")
                })
            }
        }
    }
}


@Composable
fun ProductItem(product: Product, onEdit: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF652C12))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Nombre: ${product.name}", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(text = "Marca: ${product.brandName}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(text = "Categoría: ${product.categoryName}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(text = "Proveedor: ${product.providerName}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(text = "Cantidad: ${product.quantity}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(text = "Caducidad: ${product.expirationDate}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(text = "Precio: $${product.price}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
                IconButton(
                    onClick = { onEdit(product.id) } // Llama a la función de edición con el ID del producto
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar Producto", tint = Color.White)
                }
            }
        }
    }
}

