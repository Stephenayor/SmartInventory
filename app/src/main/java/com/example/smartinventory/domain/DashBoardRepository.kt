package com.example.smartinventory.domain

import com.example.smartinventory.data.model.Product
import com.example.smartinventory.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface DashBoardRepository {

    suspend fun getProductsList(): Flow<ApiResponse<List<Product>>>
}