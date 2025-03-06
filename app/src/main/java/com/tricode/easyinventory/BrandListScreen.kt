package com.tricode.easyinventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandListScreen(navController: NavController, viewModel: InventoryViewModel) {
    val brands by viewModel.brands.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Marcas", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_brand") },
                containerColor = Color(0xFF652C12),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Marca")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(8.dp)) {
            items(brands) { brand ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF652C12))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Marca: ${brand.name}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        IconButton(
                            onClick = { navController.navigate("edit_brand/${brand.id}") } // 🔹 Navega a edición
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BrandItem(brand: Brand, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF652C12))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Marca: ${brand.name}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            IconButton(onClick = onEdit) { // 🔹 Icono de edición
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.White
                )
            }
        }
    }
}
