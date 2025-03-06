package com.tricode.easyinventory

data class SaleRecord(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantitySold: Int = 0,
    val totalPrice: Double = 0.0, // 🔹 Nuevo campo para almacenar el total de la venta
    val timestamp: Long = System.currentTimeMillis()
)

data class TopSellingProduct(
    val productName: String,
    val totalQuantitySold: Int,
    val totalRevenue: Double
)