package com.example.smartinventory.utils.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(): ViewModel() {
  private val _isProductAction = MutableStateFlow(false)
  val isProductAction: StateFlow<Boolean> = _isProductAction.asStateFlow()

  fun setIsProductAction(isFromAddProduct: Boolean) {
    _isProductAction.value = isFromAddProduct
  }
}
