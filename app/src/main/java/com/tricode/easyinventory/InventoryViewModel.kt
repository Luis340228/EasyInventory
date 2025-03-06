package com.tricode.easyinventory

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db: FirebaseFirestore

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _sales = MutableLiveData<List<SaleRecord>>()
    val sales: LiveData<List<SaleRecord>> get() = _sales

    private val _expiringProducts = MutableLiveData<List<Product>>()
    val expiringProducts: LiveData<List<Product>> get() = _expiringProducts

    private val _expiredProducts = MutableLiveData<List<Product>>()
    val expiredProducts: LiveData<List<Product>> get() = _expiredProducts

    private val _topSellingProducts = MutableLiveData<List<TopSellingProduct>>()
    val topSellingProducts: LiveData<List<TopSellingProduct>> get() = _topSellingProducts

    private val _providers = MutableLiveData<List<Provider>>()
    val providers: LiveData<List<Provider>> get() = _providers

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands

    init {
        FirebaseApp.initializeApp(application)
        db = FirebaseFirestore.getInstance()

        fetchProducts()
        fetchSales()
        fetchExpiringProducts()
        fetchTopSellingProducts()
        fetchProviders()
        fetchCategories()
        fetchBrands()
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            db.collection("products").document(product.id).set(product)
        }
    }

    private fun fetchProducts() {
        db.collection("products").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("Firestore", "Error obteniendo productos", e)
                return@addSnapshotListener
            }
            val productList = snapshot?.documents?.mapNotNull { it.toObject(Product::class.java) } ?: emptyList()

            Log.d("Firestore", "Productos obtenidos: ${productList.size}")
            productList.forEach { Log.d("Firestore", "Producto: ${it.name}, Marca: ${it.brand}") } // 🔹 Mostrar marca en logs

            _products.value = productList
            filterExpiringProducts(productList)
            filterExpiredProducts(productList)
            checkAndSendExpiringProductsNotification()
        }
    }

    private fun filterExpiringProducts(products: List<Product>) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance()
        val sevenDaysLater = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }

        val filteredProducts = products.filter { product ->
            try {
                val expirationDate = Calendar.getInstance().apply {
                    time = sdf.parse(product.expirationDate) ?: return@filter false
                }
                expirationDate in currentDate..sevenDaysLater
            } catch (e: Exception) {
                false
            }
        }
        _expiringProducts.value = filteredProducts

        if (filteredProducts.isNotEmpty()) {
            sendNotification("Producto proximo a caducar!!", "Tienes un producto próximo a caducar en tu inventario")
        }
    }

    private fun filterExpiredProducts(products: List<Product>) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance()

        val filteredProducts = products.filter { product ->
            try {
                val expirationDate = Calendar.getInstance().apply {
                    time = sdf.parse(product.expirationDate) ?: return@filter false
                }
                expirationDate.before(currentDate) // 🔹 Si ya caducó y aún tiene stock
            } catch (e: Exception) {
                false
            }
        }
        _expiredProducts.value = filteredProducts

        if (filteredProducts.isNotEmpty()) {
            sendNotification("¡¡Producto caducado!!", "Tienes un producto caducado en tu inventario")
        }
    }

    private fun sendNotification(title: String, message: String) {
        val context = getApplication<Application>().applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal de notificación si es Android 8.0 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "inventory_channel",
                "Notificaciones de Inventario",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para alertas de productos próximos a caducar y caducados"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "inventory_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun fetchSales() {
        db.collection("sales")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val salesList = snapshot?.documents?.mapNotNull { it.toObject(SaleRecord::class.java) } ?: emptyList()
                _sales.value = salesList
            }
    }

    fun fetchExpiringProducts() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance()
        val sevenDaysLater = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }

        db.collection("products").get().addOnSuccessListener { snapshot ->
            val productList = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }

            val filteredProducts = productList.filter { product ->
                try {
                    val expirationDate = Calendar.getInstance().apply {
                        time = sdf.parse(product.expirationDate) ?: return@filter false
                    }
                    expirationDate in currentDate..sevenDaysLater // Solo productos que vencen en los próximos 7 días
                } catch (e: Exception) {
                    false
                }
            }

            _expiringProducts.value = filteredProducts

        }
    }

    fun fetchTopSellingProducts() {
        db.collection("sales")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Error obteniendo ventas", e)
                    return@addSnapshotListener
                }

                val salesList = snapshot?.documents?.mapNotNull { it.toObject(SaleRecord::class.java) } ?: emptyList()

                val aggregatedSales = salesList.groupBy { it.productName }
                    .map { (productName, sales) ->
                        TopSellingProduct(
                            productName = productName,
                            totalQuantitySold = sales.sumOf { it.quantitySold },
                            totalRevenue = sales.sumOf { it.totalPrice }
                        )
                    }
                    .sortedByDescending { it.totalQuantitySold }
                    .take(5)

                _topSellingProducts.value = aggregatedSales
            }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            db.collection("products").document(productId)
                .delete()
                .addOnSuccessListener { Log.d("Firestore", "Producto eliminado exitosamente.") }
                .addOnFailureListener { e -> Log.e("Firestore", "Error al eliminar producto", e) }
        }
    }

    fun registerSale(product: Product, quantitySold: Int) {
        viewModelScope.launch {
            val sale = SaleRecord(
                id = UUID.randomUUID().toString(),
                productId = product.id,
                productName = product.name,
                quantitySold = quantitySold,
                totalPrice = product.price * quantitySold, // 🔹 Guardamos el total al momento de la venta
                timestamp = System.currentTimeMillis()
            )

            db.collection("sales").document(sale.id).set(sale)
                .addOnSuccessListener {
                    Log.d("Firestore", "Venta registrada: ${sale.productName}, Cantidad: ${sale.quantitySold}, Total: ${sale.totalPrice}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al registrar venta", e)
                }

            // Actualizar stock en la base de datos
            val newStock = product.quantity - quantitySold
            db.collection("products").document(product.id)
                .update("quantity", newStock)
                .addOnSuccessListener {
                    Log.d("Firestore", "Stock actualizado: ${product.name} -> $newStock unidades")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al actualizar stock", e)
                }
        }
    }

    fun fetchProviders() {
        db.collection("providers").addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener
            val providerList = snapshot?.documents?.mapNotNull { it.toObject(Provider::class.java) } ?: emptyList()
            _providers.value = providerList
        }
    }

    fun addProvider(provider: Provider) {
        db.collection("providers").document(provider.id).set(provider)
    }

    fun deleteProvider(providerId: String) {
        db.collection("providers").document(providerId).delete()
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            db.collection("categories").document(category.id).set(category)
        }
    }

    fun deleteCategory(brandId: String) {
        db.collection("categories").document(brandId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Categoria eliminada correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al eliminar la categoria", e)
            }
    }

    fun fetchCategories() {
        db.collection("categories").addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener
            val categoryList = snapshot?.documents?.mapNotNull { it.toObject(Category::class.java) } ?: emptyList()
            _categories.value = categoryList
        }
    }

    fun checkAndSendExpiringProductsNotification() {
        val expiringProducts = _expiringProducts.value ?: emptyList()
        if (expiringProducts.isNotEmpty()) {
            val message = RemoteMessage.Builder("expiring_products@fcm.googleapis.com")
                .setMessageId(System.currentTimeMillis().toString())
                .addData("title", "Productos por caducar")
                .addData("body", "Tienes ${expiringProducts.size} productos que caducan pronto.")
                .build()

            FirebaseMessaging.getInstance().send(message)
        }
    }

    fun fetchBrands() {
        db.collection("brands")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val brandList = snapshot?.documents?.mapNotNull { it.toObject(Brand::class.java) } ?: emptyList()
                _brands.value = brandList
            }
    }

    fun addBrand(brand: Brand) {
        db.collection("brands").document(brand.id).set(brand)
    }

    fun deleteBrand(brandId: String) {
        db.collection("brands").document(brandId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Marca eliminada correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al eliminar la marca", e)
            }
    }

}
