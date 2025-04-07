package com.example.smartinventory.presentation.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun ReportsScreen(
    reportsViewModel: ReportsViewModel = hiltViewModel()
) {
    val trend by remember { mutableStateOf(reportsViewModel.quantityTrend) }
    val breakdown by remember { mutableStateOf(reportsViewModel.categoryBreakdown) }
    val warnings by remember { mutableStateOf(reportsViewModel.lowStockWarnings) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Quantity Trends (Last 7 Days)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LineChartView(trend)

            Spacer(Modifier.height(24.dp))
            Text("Top Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            PieChartView(breakdown)

            Spacer(Modifier.height(24.dp))
            Text("Low Stock Warnings", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(warnings) { warning ->
                    Text("• $warning", Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun LineChartView(data: List<Pair<String, Float>>) {
    AndroidView(factory = { ctx ->
        LineChart(ctx).apply {
            description.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
            legend.isEnabled = false
        }
    }, update = { chart ->
        val entries = data.mapIndexed { i, (label, value) -> Entry(i.toFloat(), value) }
        val set = LineDataSet(entries, "Qty").apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 4f
            valueTextSize = 10f
        }
        chart.data = LineData(set)
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.first })
        chart.invalidate()
    }, modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    )
}


@Composable
fun PieChartView(data: Map<String, Float>) {
    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setUsePercentValues(false)
                setDrawEntryLabels(true)
                setEntryLabelTextSize(12f)
                setEntryLabelColor(android.graphics.Color.BLACK)
            }
        },
        update = { chart ->
            val entries = data.map { (category, value) ->
                PieEntry(value, category)
            }
            val set = PieDataSet(entries, "").apply {
                colors = ColorTemplate.COLORFUL_COLORS.toList()
                valueTextSize = 12f
                setDrawValues(false)
            }
            chart.data = PieData(set)
            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
