package com.example.smartinventory.presentation.add

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
class AddProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {

    private val _addProduct =
        MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Idle)
    val addProductResponseState: StateFlow<ApiResponse<Boolean>> = _addProduct

    private val _updateProduct =
        MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Idle)
    val updateProductResponseState: StateFlow<ApiResponse<Boolean>> = _updateProduct

    private val _productsDetails =
        MutableStateFlow<ApiResponse<ProductsEntity>>(ApiResponse.Idle)
    val productDetails: StateFlow<ApiResponse<ProductsEntity>> = _productsDetails


    fun addProduct(productsEntity: ProductsEntity){
        _addProduct.value = ApiResponse.Loading
        viewModelScope.launch {
            productsRepository.addProducts(listOf(productsEntity)).collect{ isAddedSuccessfully ->
                _addProduct.value = isAddedSuccessfully
            }
        }
    }

    fun updateProduct(productsEntity: ProductsEntity){
        _updateProduct.value = ApiResponse.Loading
        viewModelScope.launch {
            productsRepository.updateProducts(productsEntity).collect{ update ->
                _updateProduct.value = update
            }
        }
    }

    fun getProductsById(productId: Int) {
        _productsDetails.value = ApiResponse.Loading
        viewModelScope.launch {
            productsRepository.getProductsById(productId).collect { response ->
                _productsDetails.value = response
            }
        }
    }



}