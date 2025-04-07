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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
fun AddProductScreen(
    modifier: Modifier = Modifier, navController: NavController,
    baseViewModel: BaseViewModel,
    addProductsViewModel: AddProductsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var quantity by remember { mutableStateOf(("")) }
    var price by remember { mutableStateOf("") }
    var supplierInfo by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    val addProductResponse by addProductsViewModel.addProductResponseState.collectAsState()


    val enableDisableButton = name.text.isNotEmpty() &&
            description.text.isNotEmpty() && quantity.isNotEmpty()
            && date.isNotEmpty()
            && supplierInfo.text.isNotEmpty() && price.isNotEmpty() &&
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
                text = "Add Products",
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF1D2433),
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 20.sp,
            )
        }


        if (selectedImages.isEmpty()) {
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
    }

        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(modifier = Modifier.wrapContentWidth()) {
                items(selectedImages) { uri ->
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(4.dp)
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
                                .size(30.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 5.dp, end = 5.dp)
                        ) {
                            Image(
                                Icons.Default.Cancel,
                                contentDescription = "Remove Image"
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (addProductResponse is ApiResponse.Loading) {
            CustomLoadingBar(
                message = "Adding Item",
                imageResId = R.drawable.loading
            )
        }

        when (addProductResponse) {
            is ApiResponse.Success -> {
                isSuccess = true
            }

            is ApiResponse.Failure -> {
                isSuccess = false
                Tools.showToast(context, (addProductResponse as ApiResponse.Failure).message)
            }

            else -> Unit
        }

        if (isSuccess) {
            Dialog(
                onDismissRequest = {  },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    SuccessDialog(
                        title = "Successful",
                        subtitle = "",
                        buttonText = "Continue",
                        onButtonClick = {
                            baseViewModel.setIsProductRelatedAction(true)
                            navController.navigate(Route.DASHBOARD_SCREEN)
                            navController.popBackStack(Route.ADD_PRODUCT_SCREEN, true)
                        }
                    )
                }
            }
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
            label = { Text("Enter product name") },
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
        PriceTextField(
            priceRaw = price,
            onPriceRawChange = { price = it }
        )
        Text(text = "*Digits only", color = Color.Red, fontWeight = FontWeight.Light
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
        Text(text = "*Digits only", color = Color.Red, fontWeight = FontWeight.Light
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
        DatePickerField(
            date = date,
            onDateChange = { date = it }
        )
        Spacer(modifier = Modifier.height(30.dp))


        Text(
            text = "All Fields are Required and Ensure to Add Image*",
            fontWeight = FontWeight.SemiBold,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(10.dp))


        Button(
            onClick = {
                addProductsViewModel.addProduct(
                    productsEntity = ProductsEntity(
                        id = Tools.generateRandomId(),
                        name = name.text,
                        image = selectedImages.joinToString(",") { it.toString() },
                        description = description.text,
                        quantity = quantity.toInt(),
                        price = price.toDouble(),
                        supplierInfo = supplierInfo.text,
                        lastUpdated = date
                    )
                )
            },
            enabled = enableDisableButton,
            colors = buttonColors(
                containerColor = if (enableDisableButton) Color.Red else Color(0xFF0E7F0FF),
                contentColor = Color.White
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 15.dp)
        ) {
            Text(text = "Add Item")
        }
    }


}

@Composable
fun DatePickerField(
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { datePicker.show() }
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = {  },
            textStyle = TextStyle(
                color = Color.Black
            ),
            label = { Text("Select Date") },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select date"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@Composable
fun PriceTextField(
    priceRaw: String,
    onPriceRawChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
            if (input != digits && digits.isNotEmpty()) {
                Tools.showToast(context, "Only numbers are allowed")
            }
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