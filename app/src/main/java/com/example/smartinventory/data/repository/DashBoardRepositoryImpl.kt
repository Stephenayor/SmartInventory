package com.example.smartinventory.data.repository

import android.content.Context
import com.example.smartinventory.data.WebService
import com.example.smartinventory.data.database.ProductDao
import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.data.model.Product
import com.example.smartinventory.domain.DashBoardRepository
import com.example.smartinventory.utils.ApiResponse
import com.example.smartinventory.utils.AppConstants
import com.example.smartinventory.utils.Tools
import com.example.smartinventory.utils.Tools.Companion.isInternetAvailable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class DashBoardRepositoryImpl @Inject constructor(
    private val webService: WebService,
    private val productDao: ProductDao,
    private val context: Context
) : DashBoardRepository {


    override suspend fun getProductsList(): Flow<ApiResponse<List<Product>>> = flow {
        emit(ApiResponse.Loading)
        try {
            if (isInternetAvailable(context)) {
                val response =
                    webService.getProducts(AppConstants.PRODUCT_PATH)
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    // Update the DB
                    productDao.insertProducts(products.toProductEntityList())
                    emit(ApiResponse.Success(products))
                } else {
                    val errorMessage = handleError(response)
                    emit(
                        ApiResponse.Failure(
                            error = Exception("Error code: ${response.code()}"),
                            message = errorMessage,
                            errorCode = response.code()
                        )
                    )
                    // We fallback to local storage
                    val localProducts = productDao.getAllProducts().first()
                    emit(ApiResponse.Success(localProducts.toProduct()))
                }
            } else {
                emit(ApiResponse.Failure(Exception(), "No Internet", errorCode = 0))
                val localProducts = productDao.getAllProducts().first()
                emit(ApiResponse.Success(localProducts.toProduct()))
            }
        }
        catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

    override suspend fun getProductsFromLocal(): Flow<ApiResponse<List<Product>>> = flow {
        val product = productDao.getAllProducts()
        emit(ApiResponse.Loading)
        try {
            emit(ApiResponse.Success(product.first().toProduct()))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }


    private fun <T : Any?> handleError(response: Response<T>): String? {
        val errorBody = response.errorBody()?.string()
        val errorMessage = if (!errorBody.isNullOrEmpty()) {
            try {
                val errorResponse = Tools.handleErrorResponse(errorBody)
                errorResponse?.message
            } catch (e: Exception) {
                response.message()
            }
        } else {
            response.message()
        }
        return errorMessage
    }

    private fun List<Product>.toProductEntityList(): List<ProductsEntity> {
        return this.map { product ->
            ProductsEntity(
                id = product.id,
                name = product.name,
                image = product.image,
                description = product.description,
                quantity = product.quantity,
                price = product.price,
                supplierInfo = product.supplierInfo,
                lastUpdated = product.lastUpdated
            )
        }
    }

    private fun List<ProductsEntity>.toProduct(): List<Product> {
        return this.map { products ->
            Product(
                id = products.id,
                name = products.name,
                image = products.image,
                description = products.description,
                quantity = products.quantity,
                price = products.price,
                supplierInfo = products.supplierInfo,
                lastUpdated = products.lastUpdated
            )
        }
    }


}