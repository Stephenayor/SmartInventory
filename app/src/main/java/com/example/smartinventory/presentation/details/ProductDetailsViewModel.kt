package com.example.smartinventory.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.domain.ProductsRepository
import com.example.smartinventory.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel(){

    private val _productsDetails =
        MutableStateFlow<ApiResponse<ProductsEntity>>(ApiResponse.Idle)
    val productDetails: StateFlow<ApiResponse<ProductsEntity>> = _productsDetails

    private val _deleteProductState =
        MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Idle)
    val deleteProductState: StateFlow<ApiResponse<Boolean>> = _deleteProductState




    fun getProductsById(productId: Int) {
        _productsDetails.value = ApiResponse.Loading
        viewModelScope.launch {
            productsRepository.getProductsById(productId).collect { response ->
                _productsDetails.value = response
            }
        }
    }

    fun deleteProductById(productId: Int) {
        _deleteProductState.value = ApiResponse.Loading
        viewModelScope.launch {
            productsRepository.deleteProduct(productId).collect { response ->
                _deleteProductState.value = response
            }
        }
    }
}