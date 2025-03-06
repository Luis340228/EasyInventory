package com.tricode.easyinventory

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(navController: NavController, viewModel: InventoryViewModel, productId: String?) {
    val context = LocalContext.current
    var selectedProvider by remember { mutableStateOf<Provider?>(null) }
    val providers by viewModel.providers.observeAsState(initial = emptyList())
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val categories by viewModel.categories.observeAsState(initial = emptyList())
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedBrand by remember { mutableStateOf<Brand?>(null) }
    val brands by viewModel.brands.observeAsState(initial = emptyList())

    val existingProduct = viewModel.products.value?.find { it.id == productId }

    LaunchedEffect(productId) {
        existingProduct?.let { product ->
            name = product.name
            brand = product.brand
            selectedCategory = categories.find { it.id == product.categoryID }
            quantity = product.quantity.toString()
            expirationDate = product.expirationDate
            price = product.price.toString()
            selectedProvider = providers.find { it.id == product.providerId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) "Agregar Producto" else "Editar Producto", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF652C12))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })

            DropdownMenuBox(
                label = "Seleccionar Marca",
                options = brands,
                selectedItem = selectedBrand,
                onItemSelected = { selectedBrand = it },
                itemLabel = { it.name }
            )

            DropdownMenuBox(
                label = "Seleccionar Categoría",
                options = categories,
                selectedItem = selectedCategory,
                onItemSelected = { selectedCategory = it },
                itemLabel = { it.name }
            )

            DropdownMenuBox(
                label = "Seleccionar Proveedor",
                options = providers,
                selectedItem = selectedProvider,
                onItemSelected = { selectedProvider = it },
                itemLabel = { it.name }
            )

            // ✅ Cantidad (Solo números)
            OutlinedTextField(
                value = quantity,
                onValueChange = { if (it.all { char -> char.isDigit() }) quantity = it },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            // ✅ Selector de Fecha de Caducidad
            DatePickerField(context, expirationDate) { selectedDate -> expirationDate = selectedDate }

            // ✅ Precio (Solo números y punto decimal)
            OutlinedTextField(
                value = price,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) price = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        val product = Product(
                            id = productId ?: UUID.randomUUID().toString(),
                            name = name,
                            brandID = selectedBrand?.id ?: "",
                            brandName = selectedBrand?.name ?: "",
                            categoryID = selectedCategory?.id ?: "",
                            categoryName = selectedCategory?.name ?: "",
                            quantity = quantity.toIntOrNull() ?: 0,
                            expirationDate = expirationDate,
                            price = price.toDoubleOrNull() ?: 0.0,
                            providerId = selectedProvider?.id ?: "",
                            providerName = selectedProvider?.name ?: ""
                        )
                        viewModel.addProduct(product)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF652C12))
                ) {
                    Text("Guardar", color = Color.White)
                }

                if (productId != null) {
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
            title = { Text("Eliminar producto") },
            text = { Text("¿Estás seguro de que deseas eliminar este producto? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        productId?.let { viewModel.deleteProduct(it) }
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

// ✅ Composable para Selector de Fecha
@Composable
fun DatePickerField(context: Context, selectedDate: String, onDateSelected: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha de Caducidad") },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.CalendarMonth, contentDescription = "Seleccionar Fecha")
            }
        }
    )

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = "%02d/%02d/%d".format(dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
