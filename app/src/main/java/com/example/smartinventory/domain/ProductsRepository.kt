package com.example.smartinventory.domain

import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.data.model.Product
import com.example.smartinventory.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProductsById(productId: Int): Flow<ApiResponse<ProductsEntity>>
}