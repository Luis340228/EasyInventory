package com.tricode.easyinventory

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBrandScreen(navController: NavController, viewModel: InventoryViewModel, brandId: String?) {
    var name by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val existingBrand = viewModel.brands.value?.find { it.id == brandId }

    LaunchedEffect(brandId) {
        existingBrand?.let { brand ->
            name = brand.name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (brandId == null) "Agregar Marca" else "Editar Marca", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre de la Marca") })

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        val brand = Brand(
                            id = brandId ?: UUID.randomUUID().toString(),
                            name = name
                        )
                        viewModel.addBrand(brand)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF652C12))
                ) {
                    Text("Guardar", color = Color.White)
                }

                if (brandId != null) {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar Marca") },
            text = { Text("¿Estás seguro de que deseas eliminar esta marca? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        brandId?.let {
                            viewModel.deleteBrand(it) // Llamar a la función en el ViewModel
                            showDialog = false
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

}
