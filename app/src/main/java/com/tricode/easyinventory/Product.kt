package com.tricode.easyinventory

import com.google.firebase.Timestamp

data class Product(
    val id: String = "",
    val name: String = "",
    val brand: String = "",
    val brandID: String = "",
    val brandName: String = "",
    val categoryID: String = "",
    val categoryName: String = "",
    val quantity: Int = 0,
    val expirationDate: String = Timestamp.now().toString(),
    val price: Double = 0.0,
    val providerId: String = "",
    val providerName: String = ""
)