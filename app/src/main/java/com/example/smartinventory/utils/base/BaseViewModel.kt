package com.example.smartinventory.utils.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(): ViewModel() {
  private val _isFromAddProduct = MutableStateFlow(false)
  val isFromAddProduct: StateFlow<Boolean> = _isFromAddProduct.asStateFlow()

  fun setIsFromAddProduct(isFromAddProduct: Boolean) {
    _isFromAddProduct.value = isFromAddProduct
  }
}
