package com.example.smartinventory.data.model

data class ProductSales(
    val id: Int,
    val name: String,
    val quantity: Int,
    val category: String,
    val price: Double,
    val lastUpdated: String
)