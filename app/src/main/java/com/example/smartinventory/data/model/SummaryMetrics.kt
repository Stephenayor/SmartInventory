package com.example.smartinventory.data.model

data class SummaryMetrics(
  val totalItems: Int,
  val outOfStock: Int,
  val recentActivity: String
)