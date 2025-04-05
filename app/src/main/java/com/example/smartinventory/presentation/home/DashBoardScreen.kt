package com.example.smartinventory.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartinventory.R
import com.example.smartinventory.data.model.Product
import com.example.smartinventory.data.model.ProductSales
import com.example.smartinventory.utils.ApiResponse
import com.example.smartinventory.utils.CustomLoadingBar
import com.example.smartinventory.utils.Tools
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.delay


@Composable
fun DashBoardScreen(modifier: Modifier = Modifier,
                    navController: NavController,
                    dashBoardViewModel: DashBoardViewModel = hiltViewModel()
) {

    //Products
    val productsListState by dashBoardViewModel.productsState.collectAsState()
    val products by dashBoardViewModel.dashBoardItems.collectAsState()
    val metricsSummaryState by dashBoardViewModel.summary.collectAsState()



//
//    // Products
//    var productsList by remember { mutableStateOf( Product()) }
//    var mainCategoriesList = listOf<Categories.CategoriesData>()
//
//
    var productsList by remember { mutableStateOf(emptyList<Product>()) }
//    var filteredFoodsList = listOf<Food.FoodData>()

    // UI states
    var isLoading by remember { mutableStateOf(false) }
    var isLoadingMetricsSummary by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showRetryText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dashBoardViewModel.getProductsList()
    }


    Scaffold(
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    navController.navigate(Route.CREATE_FOOD_DETAILS_SCREEN)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


//
//            when (productsListState) {
//                is ApiResponse.Loading -> {
//                    isLoading = true
//                }
//                is ApiResponse.Success -> {
//                    isLoading = false
//                    // Update the local product list from API response
//                    productsList = (productsListState as ApiResponse.Success<List<Product>>).data ?: emptyList()
//                }
//                is ApiResponse.Failure -> {
//                    isLoading = false
//                    dialogMessage = (productsListState as ApiResponse.Failure).message ?: "Something went wrong!"
//                    isSuccess = false
//                    showDialog = true
//                    if ((productsListState as ApiResponse.Failure).message == "No Internet"){
//                        Tools.showToast(LocalContext.current, "No Internet \nYou may be getting outdated data")
//                    }
//                }
//                else -> Unit
//            }
//
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.padding(horizontal = 16.dp)
//            ) {
//                items(productsList) { product ->
//                    ProductsItem(product, navController)
//                }
//            }

//            when (val state = metricsSummaryState) {
//                is ApiResponse.Idle -> {
//
//                }
//
//                is ApiResponse.Loading -> {
//                    isLoadingCategories = true
//
//                }
//
//                is ApiResponse.Success -> {
//                    isLoadingCategories = false
//                    productsList = state.data?.data!!
//                    mainCategoriesList = productsList
//                    foodHomeViewModel.getAllFood()
//
//                }
//
//                is ApiResponse.Failure -> {
//                    showRetryText = true
//                    isLoadingCategories = false
//                    errorMessage = state.message ?: "An error occurred"
//                }
//            }


            if (isLoading) {
                CustomLoadingBar(
                    "Please wait...",
                    imageResId = R.drawable.loading
                )
                dashBoardViewModel.clearLoadingState()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.useravatar),
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(width = 42.dp, height = 42.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {


                Text(
                    text = "Hey there, ${Tools.getSalutationBasedOnTime()}!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D2433)
                )

                if (isLoadingMetricsSummary) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
//                    foodHomeViewModel.clearLoadingState()
                }

                Column(Modifier.height(300.dp).padding(16.dp)) {
                    // ─── Summary Metrics ───────────────────────
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard("Total Items", metricsSummaryState.totalItems.toString())
                        SummaryCard("Out of Stock", metricsSummaryState.outOfStock.toString())
                        SummaryCard("Recent Activity", metricsSummaryState.recentActivity)
                    }

                    Spacer(Modifier.height(24.dp))

                    LaunchedEffect(Unit) {
                        delay(4000)
                    }

                    //Pie Chart
//                    Text("Category Breakdown", style = MaterialTheme.typography.titleMedium)
//                    PieChartView(products)



                    Spacer(Modifier.height(24.dp))

                    // Bar Chart
                    Text("Stock Levels by Category", style = MaterialTheme.typography.titleMedium)
                    BarChartView(products)
                }


//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = { newQuery ->
//                        searchQuery = newQuery
//
//                        val filteredMeals = foodsList.filter {
//                            it.name?.contains(
//                                newQuery,
//                                ignoreCase = true
//                            ) == true
//                        }
//                        filteredFoodsList = if (newQuery.isEmpty()) foodsList else filteredMeals
//
//                        val filtered = productsList.filter {
//                            it.name?.contains(
//                                newQuery,
//                                ignoreCase = true
//                            ) == true
//                        }
//                        productsList = if (newQuery.isEmpty()) mainCategoriesList else filtered
//                    },
//                    placeholder = {
//                        Text(
//                            "Search foods...",
//                            fontFamily = FontFamily.SansSerif,
//                            color = Color(0xFF9D9EA1)
//
//                        )
//                    },
//                    leadingIcon = {
//                        Icon(Icons.Default.Search, contentDescription = "Search Icon")
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color(0xFFF5F6FA), shape = RoundedCornerShape(8.dp))
//                )

                Spacer(modifier = Modifier.height(16.dp))


//                if (showRetryText) {
//                    val annotatedText = buildAnnotatedString {
//                        append(errorMessage)
//                        append("\n")
//                        pushStringAnnotation(tag = "retry", annotation = "retry")
//                        withStyle(
//                            style = SpanStyle(
//                                color = Color.Blue,
//                                textDecoration = TextDecoration.Underline
//                            )
//                        ) {
//                            append("Please Retry")
//                        }
//                        pop()
//                    }
//
//                    ClickableText(
//                        text = annotatedText,
//                        onClick = { offset ->
//                            annotatedText.getStringAnnotations(
//                                tag = "retry",
//                                start = offset,
//                                end = offset
//                            )
//                                .firstOrNull()?.let {
//                                    // Trigger the retry action
//                                    foodHomeViewModel.getCategories()
//                                }
//                        },
//                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
//                    )
//                }

//                FoodListScreen(filteredFoodsList, navController)


                when (val productsState = productsListState) {
                    is ApiResponse.Idle -> {

                    }

                    is ApiResponse.Loading -> {
                        isLoading = true
                    }

                    is ApiResponse.Success -> {
                        isLoading = false
                        productsList = productsState.data ?: emptyList()
                    }

                    is ApiResponse.Failure -> {
                        isLoading = false
                        dialogMessage = productsState.message ?: "Something went wrong!"
                        isSuccess = false
                        showDialog = true
                        if (productsState.message == "No Internet"){
                            Tools.showToast(LocalContext.current, "No Internet \nYou may be getting outdated data")
                        }

                    }
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    items(productsList) { product ->
                        ProductsItem(product, navController)
                    }
                }

            }
        }
    }
}


@Composable
fun ProductsItem(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("productDetailsScreen/${product.id}") },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
    ) {
        Column(modifier = Modifier
                .fillMaxWidth()
            .padding(16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    rememberVectorPainter(Icons.Default.Delete),
                    contentDescription = "Delete",
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity: ${product.quantity} Units,",
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(Tools.formatPrice(LocalContext.current,
                                product.price.toString()).toString())
                        }
                    },
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    append("Last updated: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(Tools.reformatDate(product.lastUpdated))
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp).weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}



@Composable
fun PieChartView(products: List<ProductSales>) {
    val counts = products.groupingBy { it.category }.eachCount()
    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setDrawEntryLabels(false)
                isDrawHoleEnabled = true
            }
        },
        update = { chart ->
            val counts = products.groupingBy { it.category }.eachCount()
            val entries = counts.map { PieEntry(it.value.toFloat(), it.key) }
            val set = PieDataSet(entries, "").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 12f
                setDrawValues(false)      // no values on slices
            }
            chart.data = PieData(set)

            chart.renderer = PieChartRenderer(chart, chart.animator, chart.viewPortHandler)

            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun BarChartView(products: List<ProductSales>) {
    // Sum quantities per category
    val sums = products.groupBy { it.category }
        .mapValues { it.value.sumOf { p -> p.quantity } }

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
            }
        },
        update = { chart ->
            val entries = sums.entries.mapIndexed { i, (cat, sum) ->
                BarEntry(i.toFloat(), sum.toFloat())
            }
            val set = BarDataSet(entries, "").apply {
                colors = ColorTemplate.COLORFUL_COLORS.toList()
                valueTextSize = 12f
            }
            chart.data = BarData(set).apply { barWidth = 0.5f }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(sums.keys.toList())
            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}




@Composable
fun BottomNavigationBar() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Reports")
    val icons = listOf(
        rememberVectorPainter(image = Icons.Default.Home),
        rememberVectorPainter(image = Icons.Default.List),
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = {
                    Icon(
                        painter = icons[index],
                        contentDescription = item,
                        tint = if (selectedItem == index)  Color.Red else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem == index) Color.Red else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Red,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Red,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}