package com.example.smartinventory.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.smartinventory.R
import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.utils.ApiResponse
import com.example.smartinventory.utils.Tools
import com.example.smartinventory.utils.base.BaseViewModel

@Composable
fun ProductDetailsScreen(
    productId: Int,
    navController: NavController,
    baseViewModel: BaseViewModel,
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel()
) {

    val productDetailsState by productDetailsViewModel.productDetails.collectAsState()
    var productDetails by remember { mutableStateOf<ProductsEntity?>(null) }
    val deleteProductResponseState by productDetailsViewModel.deleteProductState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    //UI States
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(true) }
    var context = LocalContext.current


    Scaffold (snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
    { innerPadding ->

        LaunchedEffect(Unit) {
            productDetailsViewModel.getProductsById(productId)
        }



    when (val state = productDetailsState) {
        is ApiResponse.Idle -> {

        }

        is ApiResponse.Loading -> {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator()
                }
            }

        }

        is ApiResponse.Success -> {
            productDetails = state.data
        }

        is ApiResponse.Failure -> {
            showDialog = true
            isSuccess = false
            dialogMessage = state.message ?: "Something went wrong!"

        }
    }

        LaunchedEffect(deleteProductResponseState) {
            when (val deleteResponse = deleteProductResponseState) {
                is ApiResponse.Idle -> {

                }

                is ApiResponse.Loading -> {

                }

                is ApiResponse.Success -> {
                    val result = snackBarHostState.showSnackbar(
                        message = "Product deleted",
                        actionLabel = "OK"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        navController.popBackStack()
                    } else {
                        navController.popBackStack()
                    }
                    baseViewModel.setIsProductAction(true)
                }

                is ApiResponse.Failure -> {
                    showDialog = true
                    isSuccess = false
                    dialogMessage = deleteResponse.message ?: "Failed to Delete!"

                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(innerPadding)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Row {
                IconButton(onClick = {
                    productDetails?.id?.let { productDetailsViewModel.deleteProductById(it) }
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                IconButton(onClick = {
                    navController.navigate("updateFoodScreen/${productDetails?.id}")
                }) {
                    Icon(painterResource(R.drawable.ic_edit), contentDescription = "Edit")
                }
            }
        }

        // Product Image
        Box {
            AsyncImage(
                model = productDetails?.image,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Name
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = productDetails?.name.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D2433),
                fontFamily = FontFamily.Monospace
            )


            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = productDetails?.description.toString(),
                fontSize = 16.sp,
                color = Color(0xFF1D2433),
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFFBF1F1))
                    .padding(6.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "Quantity: ${productDetails?.quantity} Units,",
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(Modifier.width(12.dp))

                androidx.compose.material3.Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                Tools.formatPrice(
                                    LocalContext.current,
                                    productDetails?.price.toString()
                                ).toString()
                            )
                        }
                    },
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = productDetails?.supplierInfo.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
    }
}
