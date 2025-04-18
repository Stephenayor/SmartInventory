package com.example.smartinventory.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartinventory.data.model.Product
import com.example.smartinventory.data.model.ProductSales
import com.example.smartinventory.data.model.SummaryMetrics
import com.example.smartinventory.domain.DashBoardRepository
import com.example.smartinventory.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val dashBoardRepository: DashBoardRepository
) : ViewModel() {

    private val _productsState = MutableStateFlow<ApiResponse<List<Product>>>(ApiResponse.Idle)
    val productsState: StateFlow<ApiResponse<List<Product>>> = _productsState

    private val _productsFromLocalState =
        MutableStateFlow<ApiResponse<List<Product>>>(ApiResponse.Idle)
    val productsFromLocalState: StateFlow<ApiResponse<List<Product>>> = _productsFromLocalState

    private val _productSales = MutableStateFlow<List<ProductSales>>(emptyList())
    val productSales: StateFlow<List<ProductSales>> = _productSales

    private val _metricsSummary = MutableStateFlow(SummaryMetrics(0, 0, ""))
    val metricsSummary: StateFlow<SummaryMetrics> = _metricsSummary

    init {
        // Mock data
        val itemsMockData = listOf(
            ProductSales(1, "Gasoline", 50, "Crude", 1000.0, "2025-04-01T12:00:00Z"),
            ProductSales(2, "Premium motor spirit", 0, "Crude", 2000.0, "2025-04-02T08:30:00Z"),
            ProductSales(3, "Liquefied Fuel", 20, "Fuel", 3000.0, "2025-04-02T08:30:00Z")
        )
        _productSales.value = itemsMockData
        _metricsSummary.value = SummaryMetrics(
            totalItems = itemsMockData.size,
            outOfStock = itemsMockData.count { it.quantity == 0 },
            recentActivity = "Last week"
        )
        getProductsList()
    }

     private fun getProductsList() {
        _productsState.value = ApiResponse.Loading
        viewModelScope.launch {
            dashBoardRepository.getProductsList().collect { response ->
                _productsState.value = response
            }
        }
    }

    fun getProductsFromLocal() {
        _productsFromLocalState.value = ApiResponse.Loading
        viewModelScope.launch {
            dashBoardRepository.getProductsFromLocal().collect { response ->
                _productsFromLocalState.value = response
            }
        }
    }

    fun clearLoadingState() {
        _productsState.value = ApiResponse.Idle
        _productsFromLocalState.value = ApiResponse.Idle
    }


}