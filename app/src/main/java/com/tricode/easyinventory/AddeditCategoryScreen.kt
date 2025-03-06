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
fun AddEditCategoryScreen(navController: NavController, viewModel: InventoryViewModel, categoryId: String?) {
    var name by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val existingCategory = viewModel.categories.value?.find { it.id == categoryId }

    LaunchedEffect(categoryId) {
        existingCategory?.let { category ->
            name = category.name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (categoryId == null) "Agregar Categoría" else "Editar Categoría", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de Categoría") }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        val category = Category(
                            id = categoryId ?: UUID.randomUUID().toString(),
                            name = name
                        )
                        viewModel.addCategory(category)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF652C12))
                ) {
                    Text("Guardar", color = Color.White)
                }

                if (categoryId != null) {
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
            title = { Text("Eliminar categoría") },
            text = { Text("¿Estás seguro de que deseas eliminar esta categoría? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        categoryId?.let { viewModel.deleteCategory(it) }
                        showDialog = false
                        navController.popBackStack()
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