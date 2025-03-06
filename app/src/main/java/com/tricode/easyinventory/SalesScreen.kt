package com.tricode.easyinventory

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(navController: NavController, viewModel: InventoryViewModel) {
    val context = LocalContext.current
    val products by viewModel.products.observeAsState(initial = emptyList())

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantitySold by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Venta", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DropdownMenuBox(
                label = "Seleccionar Producto",
                options = products,
                selectedItem = selectedProduct,
                onItemSelected = { selectedProduct = it },
                modifier = Modifier.fillMaxWidth(),
                itemLabel = { it.name } // Para asegurarse de que muestra el nombre del producto
            )


            OutlinedTextField(
                value = quantitySold,
                onValueChange = { quantitySold = it },
                label = { Text("Cantidad Vendida") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val product = selectedProduct
                    val quantity = quantitySold.toIntOrNull()

                    when {
                        product == null -> {
                            Toast.makeText(context, "Selecciona un producto", Toast.LENGTH_SHORT).show()
                        }
                        quantity == null || quantity <= 0 -> {
                            Toast.makeText(context, "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show()
                        }
                        quantity > product.quantity -> {
                            Toast.makeText(context, "Stock insuficiente. Solo hay ${product.quantity} unidades disponibles", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.registerSale(product, quantity)
                            Toast.makeText(context, "Venta registrada correctamente", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF652C12))
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Confirmar Venta")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirmar Venta", color = Color.White)
            }
        }
    }
}
