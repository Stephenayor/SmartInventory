package com.example.smartinventory.presentation.reports

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(): ViewModel() {
  // Sample trend
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
    "Fuel" to 120f,
    "Diesel" to 80f,
    "Petrol" to 50f,
    "Gas" to 30f
  )

  // Low stock threshold mock data
  val lowStockWarnings = listOf(
    "Diesel (5 units)",
    "Premium Motor Spirit (2 units)",
    "LPG (3 units)"
  )
}
