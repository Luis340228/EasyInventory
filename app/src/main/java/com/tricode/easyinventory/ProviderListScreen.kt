package com.tricode.easyinventory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ProviderListScreen(navController: NavController, viewModel: InventoryViewModel) {
    val providers by viewModel.providers.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proveedores", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_provider") },
                containerColor = Color(0xFF652C12),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Proveedor")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(8.dp)) {
            items(providers) { provider ->
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
                        Column {
                            Text(
                                text = "Nombre: ${provider.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Text(
                                text = "Contacto: ${provider.contact}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                        IconButton(
                            onClick = { navController.navigate("edit_provider/${provider.id}") } // 🔹 Navega a edición
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
fun ProviderItem(provider: Provider) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF652C12))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${provider.name}", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Text(text = "Contacto: ${provider.contact}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}
