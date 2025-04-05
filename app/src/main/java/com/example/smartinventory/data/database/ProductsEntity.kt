package com.example.smartinventory.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class ProductsEntity (
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val quantity: Int,
    val price: Double,
    @SerializedName("supplierInfo")
    val supplierInfo: String,
    val lastUpdated: String
)
