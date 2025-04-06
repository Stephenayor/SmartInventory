package com.example.smartinventory.presentation.add

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartinventory.R
import com.example.smartinventory.data.database.ProductsEntity
import com.example.smartinventory.utils.ApiResponse
import com.example.smartinventory.utils.CustomLoadingBar
import com.example.smartinventory.utils.Route
import com.example.smartinventory.utils.Tools
import com.example.smartinventory.utils.base.BaseViewModel
import com.example.smartinventory.utils.dialogs.SuccessDialog
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun UpdateProductScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    productId: Int,
    baseViewModel: BaseViewModel,
    addProductsViewModel: AddProductsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    //Product
    val updateProductResponse by addProductsViewModel.updateProductResponseState.collectAsState()
    var productDetails by remember { mutableStateOf<ProductsEntity?>(null) }
    //Form fields
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var supplierInfo by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }


    val productDetailsState by addProductsViewModel.productDetails.collectAsState()
    var hasPrefilled by remember { mutableStateOf(false) }

    LaunchedEffect(productDetailsState) {
        if (!hasPrefilled && productDetailsState is ApiResponse.Success) {
            (productDetailsState as ApiResponse.Success<ProductsEntity>).data?.let { p ->
                name = TextFieldValue(p.name)
                description = TextFieldValue(p.description)
                quantity = p.quantity.toString()
                price = p.price.toString()
                supplierInfo = TextFieldValue(p.supplierInfo)
                date = Tools.reformatDate(p.lastUpdated)
                // If you stored URIs as strings in the entity:
                selectedImages = p.image
                    .split(",")
                    .mapNotNull { Uri.parse(it) }
                hasPrefilled = true
            }
        }
    }


    val isButtonEnabled = name.text.isNotEmpty() &&
            description.text.isNotEmpty() && quantity.isNotEmpty()
            && date.isNotEmpty()
            && supplierInfo.text.isNotEmpty() && price.isNotEmpty()
    selectedImages.isNotEmpty()

    val launcherCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val uri = Tools.saveImageToInternalStorage(context, it)
                selectedImages = selectedImages + uri
            }
        }

    val launcherGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImages = selectedImages + it
            }
        }

    LaunchedEffect(Unit) {
        addProductsViewModel.getProductsById(productId)
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(top = 20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_square_right),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                text = "Update Products",
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF1D2433),
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 20.sp,
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { launcherCamera.launch(null) }
                    .padding(8.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Take photo"
                    )
                    Text(
                        text = "Take photo",
                        color = Color(0xFF1D2433),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { launcherGallery.launch("image/*") }
                    .padding(8.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.uploadsimple),
                        contentDescription = "Upload"
                    )
                    Text(
                        text = "Upload",
                        color = Color(0xFF1D2433),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedImages) { uri ->
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .fillMaxSize()
                    )
                    IconButton(
                        onClick = { selectedImages = selectedImages - uri },
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 5.dp, end = 5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.closeselectedimage),
                            contentDescription = "Remove Image"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (updateProductResponse is ApiResponse.Loading) {
            CustomLoadingBar(
                message = "Update in Progress",
                imageResId = R.drawable.loading
            )
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
                Tools.showToast(
                    LocalContext.current,
                    "Could not retrieve product, Please try again"
                )

            }
        }

        when (updateProductResponse) {
            is ApiResponse.Success -> {
                Box(
                    modifier = modifier
                        .fillMaxHeight()
                        .background(Color(0xFFF5F7FB)),
                    contentAlignment = Alignment.Center
                ) {
                    SuccessDialog(
                        title = "Successful",
                        subtitle = "",
                        buttonText = "Continue",
                        onButtonClick = {
                            baseViewModel.setIsFromAddProduct(true)
                            navController.navigate(Route.DASHBOARD_SCREEN)
                        }
                    )
                }
            }

            is ApiResponse.Failure -> {
                Tools.showToast(context, (updateProductResponse as ApiResponse.Failure).message)
            }

            else -> Unit
        }


        // Name Field
        Text(
            text = "Name",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter food name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Description Field
        Text(
            text = "Description",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Enter product description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Price Field
        Text(
            text = "Price",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        PriceField(
            priceRaw = price,
            onPriceRawChange = { price = it }
        )
        Spacer(modifier = Modifier.height(20.dp))


        // Quantity Field
        Text(text = "Quantity")
        OutlinedTextField(
            value = quantity,
            onValueChange = { new ->
                quantity = new.filter { it.isDigit() }
            },
            label = { Text("Enter quantity") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(20.dp))


        // Supplier info
        Text(text = "Supplier info")
        OutlinedTextField(
            value = supplierInfo,
            onValueChange = { supplierInfo = it },
            label = { Text("Enter supplier info") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Date
        Text(text = "Date")
        DatePicker(
            date = date,
            onDateChange = { date = it }
        )
        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                addProductsViewModel.updateProduct(
                    productsEntity = ProductsEntity(
                        id = productId,
                        name = name.text,
                        image =  selectedImages.joinToString(",") { it.toString() },
                        description = description.text,
                        quantity = quantity.toInt(),
                        price = price.toDouble(),
                        supplierInfo = supplierInfo.text,
                        lastUpdated = date
                    )
                )
                baseViewModel.setIsFromAddProduct(true)
            },
            enabled = true,
            colors = buttonColors(
                containerColor = if (isButtonEnabled) Color.Red else Color(0xFF0E7F0FF),
                contentColor = Color.White
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 15.dp)
        ) {
            Text(text = "Update")
        }
    }


}

@Composable
fun DatePicker(
    date: String,
    onDateChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateChange(formatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    OutlinedTextField(
        value = date,
        onValueChange = { date },
        label = { Text("Select Date") },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select date"
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { datePicker.show() }
    )
}


@Composable
fun PriceField(
    priceRaw: String,
    onPriceRawChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatted = remember(priceRaw) {
        if (priceRaw.isEmpty()) ""
        else {
            try {
                NumberFormat.getNumberInstance(Locale("en", "NG"))
                    .format(priceRaw.toLong())
            } catch (e: Exception) {
                priceRaw
            }
        }
    }

    OutlinedTextField(
        value = formatted,
        onValueChange = { input ->
            val digits = input.filter { it.isDigit() }
            onPriceRawChange(digits)
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text("Enter the product price") },
        leadingIcon = {
            Text(
                text = "₦",
                fontSize = 18.sp,
                color = Color.Gray,
                fontFamily = FontFamily.SansSerif
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}