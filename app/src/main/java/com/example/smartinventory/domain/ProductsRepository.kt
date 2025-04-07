package com.example.smartinventory.domain

import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProductsById(productId: Int): Flow<ApiResponse<ProductsEntity>>

    suspend fun addProducts(products: List<ProductsEntity>): Flow<ApiResponse<Boolean>>

    suspend fun updateProducts(products: ProductsEntity): Flow<ApiResponse<Boolean>>

    suspend fun deleteProduct(productId: Int): Flow<ApiResponse<Boolean>>
}