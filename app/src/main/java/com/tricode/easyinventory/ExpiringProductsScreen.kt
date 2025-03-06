package com.tricode.easyinventory

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiringProductsScreen(viewModel: InventoryViewModel) {
    val expiringProducts by viewModel.expiringProducts.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchExpiringProducts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Por Caducar",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(8.dp)) {
            items(expiringProducts) { product ->
                ProductItem(product = product, onEdit = {})
            }
        }
    }
}