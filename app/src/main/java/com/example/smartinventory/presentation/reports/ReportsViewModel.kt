package com.example.smartinventory.presentation.reports

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(): ViewModel() {
  // Sample trend: quantity over 7 days
  val quantityTrend = listOf(
    Pair("Mon", 50f),
    Pair("Tue", 45f),
    Pair("Wed", 60f),
    Pair("Thu", 30f),
    Pair("Fri", 75f),
    Pair("Sat", 20f),
    Pair("Sun", 90f)
  )

  // Sample category breakdown
  val categoryBreakdown = mapOf(
    "Fruits" to 120f,
    "Vegetables" to 80f,
    "Dairy" to 50f,
    "Snacks" to 30f
  )

  // Low stock threshold
  val lowStockWarnings = listOf(
    "Tomatoes (5 units)",
    "Milk (2 units)",
    "Chips (3 units)"
  )
}
