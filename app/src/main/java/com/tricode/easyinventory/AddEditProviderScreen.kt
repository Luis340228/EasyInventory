package com.tricode.easyinventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProviderScreen(navController: NavController, viewModel: InventoryViewModel, providerId: String?) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val existingProvider = viewModel.providers.value?.find { it.id == providerId }

    LaunchedEffect(providerId) {
        existingProvider?.let { provider ->
            name = provider.name
            contact = provider.contact
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (providerId == null) "Agregar Proveedor" else "Editar Proveedor", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre del Proveedor") })
            OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Contacto") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        val provider = Provider(
                            id = providerId ?: UUID.randomUUID().toString(),
                            name = name,
                            contact = contact
                        )
                        viewModel.addProvider(provider)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF652C12))
                ) {
                    Text("Guardar", color = Color.White)
                }

                if (providerId != null) {
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
            title = { Text("Eliminar proveedor") },
            text = { Text("¿Estás seguro de que deseas eliminar este proveedor? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        providerId?.let { viewModel.deleteProvider(it) }
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
