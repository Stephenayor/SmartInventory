package com.example.smartinventory.data.repository

import android.content.Context
import com.example.smartinventory.data.database.ProductDao
import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.domain.ProductsRepository
import com.example.smartinventory.utils.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val context: Context
): ProductsRepository {
    override suspend fun getProductsById(productId: Int): Flow<ApiResponse<ProductsEntity>> = callbackFlow {
        send(ApiResponse.Loading)
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val product = productDao.getProductById(productId)
                trySend(ApiResponse.Success(product.first()))
            } catch (exception: Exception) {
                trySend(ApiResponse.Failure(exception, exception.message))
            }
        }

        awaitClose { job.cancel() }
    }

    override suspend fun addProducts(products: List<ProductsEntity>): Flow<ApiResponse<Boolean>> = flow {
        val addProduct = productDao.insertProducts(products)
        emit(ApiResponse.Loading)
        try {
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

    override suspend fun updateProducts(products: ProductsEntity): Flow<ApiResponse<Boolean>> = flow {
        val updateProduct = productDao.updateProduct(products)
        emit(ApiResponse.Loading)
        try {
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

    override suspend fun deleteProduct(productId: Int): Flow<ApiResponse<Boolean>>  = flow {
        val deleteProduct = productDao.deleteProductById(productId)
        emit(ApiResponse.Loading)
        try {
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

}